package no.ntnu.stud.avikeyb.inputdevices.emotivepoc;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * A continent helper class that takes care of running the headset communication in
 * a separate thread.
 * <p>
 * The user of the class can listen for event with the EventListener interface and
 * can interact with the headset without having to care about the multithreading.
 * <p>
 * This class is implemented as a runnable and should be run in a new thread:
 * <p>
 * (new Thread(new EpocHeadset(...))).start();
 * <p>
 * The thread must be started before running calling any methods on the EpocHeadset object
 */
public class EpocHeadset implements Runnable {


    /**
     * Interface used to listen for events from the headset
     */
    public interface EventListener {
        /**
         * Called when the headset has connected
         */
        void headsetConnected();

        /**
         * Called when the headset has disconnected
         */
        void headsetDisconnected();

        /**
         * Called after login was successful and available training profiles has been loaded
         */
        void profileNamesLoaded(List<String> names);

        /**
         * Called when a mental action is detected from the headset
         *
         * @param action the mantal action that is detected
         */
        void actionDetected(EpocEngine.Action action);

    }

    private Handler mUIThreadHandler; // Handler used to communicate with the UI thread
    private Handler mWorkerThreadHandler; // Handler used to communicate the with worker thread

    private CloudProfile mCloudProfile;
    private Activity mActivity;
    private EventListener mEventListener;


    /**
     * Creates a new headset object. The runnable should be created on the UI thread.
     *
     * @param activity the activity that communicates with the headset
     * @param listener a listener that will be notified of headset events
     */
    public EpocHeadset(Activity activity, EventListener listener) {
        initUIThreadHandler();
        mActivity = activity;
        mEventListener = listener;
    }


    /**
     * Login to the emotive cloud.
     * <p>
     * If the login is successful the EventListener.profileNamesLoaded will be called with
     * the available profile names
     *
     * @param username the username
     * @param password the password
     */
    public void login(String username, String password) {

        // We just pass the credentials in an arraylist
        ArrayList<String> creds = new ArrayList<>();
        creds.add(username);
        creds.add(password);

        sendMessageToWorkerThead(Event.USER_LOGIN.ordinal(), creds);
    }

    /**
     * Load and activate the specified training profile
     *
     * @param profileName the profile to load
     */
    public void loadProfile(String profileName) {
        sendMessageToWorkerThead(Event.LOAD_PROFILE.ordinal(), profileName);
    }

    /**
     * Disconnect from the headset
     * <p>
     * This will stop the headset thread and disconnect from the cloud service and the headset
     */
    public void disconnect() {
        sendMessageToWorkerThead(Event.QUIT.ordinal());
    }


    @Override
    public void run() {
        Looper.prepare();
        initWorkerThreadHandler();

        final EpocEngine epocEngine = new EpocEngine(mActivity, new EpocEngine.HeadsetEvents() {

            @Override
            public void onHeadsetConnected() {
                sendMessageToUIThead(Event.HEADSET_CONNECTED.ordinal());
            }

            @Override
            public void onHeadsetDisconnected() {
                sendMessageToUIThead(Event.HEADSET_DISCONNECTED.ordinal());
            }

            @Override
            public void onMentalCommand(EpocEngine.Action action) {
                sendMessageToUIThead(Event.MENTAL_COMMAND.ordinal(), action);

            }
        });

        mCloudProfile = new CloudProfile(mActivity);

        Looper.loop();


        // The looper has quit so we logout and disconnect
        mCloudProfile.logout();
        epocEngine.disconnect();
    }


    private void initUIThreadHandler() {

        mUIThreadHandler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {

                if (msg.what == Event.HEADSET_CONNECTED.ordinal()) {
                    mEventListener.headsetConnected();
                } else if (msg.what == Event.HEADSET_DISCONNECTED.ordinal()) {
                    mEventListener.headsetDisconnected();
                } else if (msg.what == Event.USER_LOGIN.ordinal()) {
                    mEventListener.profileNamesLoaded((List<String>) msg.obj);
                } else if (msg.what == Event.MENTAL_COMMAND.ordinal()) {
                    mEventListener.actionDetected((EpocEngine.Action) msg.obj);
                }
            }
        };

    }

    private void initWorkerThreadHandler() {

        mWorkerThreadHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                if (msg.what == Event.USER_LOGIN.ordinal()) {
                    ArrayList<String> creds = (ArrayList<String>) msg.obj;
                    mCloudProfile.login(creds.get(0), creds.get(1));
                    sendMessageToUIThead(Event.USER_LOGIN.ordinal(), mCloudProfile.getProfileNames());
                } else if (msg.what == Event.LOAD_PROFILE.ordinal()) {
                    mCloudProfile.loadProfile((String) msg.obj);
                } else if (msg.what == Event.QUIT.ordinal()) {
                    Looper.myLooper().quit();
                }
            }
        };

    }


    private void sendMessageToWorkerThead(int what, Object obj) {
        mWorkerThreadHandler.sendMessage(Message.obtain(mWorkerThreadHandler, what, obj));
    }

    private void sendMessageToWorkerThead(int what) {
        mWorkerThreadHandler.sendEmptyMessage(what);
    }


    private void sendMessageToUIThead(int what, Object obj) {
        mUIThreadHandler.sendMessage(Message.obtain(mUIThreadHandler, what, obj));
    }

    private void sendMessageToUIThead(int what) {
        mUIThreadHandler.sendEmptyMessage(what);
    }


    // Different event used for internal message passing
    private enum Event {
        HEADSET_CONNECTED,
        HEADSET_DISCONNECTED,
        USER_LOGIN,
        LOAD_PROFILE,
        MENTAL_COMMAND,
        QUIT
    }
}
