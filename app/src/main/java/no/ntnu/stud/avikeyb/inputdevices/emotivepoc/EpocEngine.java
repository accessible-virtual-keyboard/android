package no.ntnu.stud.avikeyb.inputdevices.emotivepoc;

import android.content.Context;
import android.os.Handler;

import com.emotiv.insight.IEdk;
import com.emotiv.insight.IEdkErrorCode;
import com.emotiv.insight.IEmoStateDLL;

/**
 * Manages the connection to the epoc headset and detects mental commands from the user
 */
public class EpocEngine {


    /**
     * The mental actions that we can detect. Hardcoded to some of the standard emotive
     * mental commands
     */
    public enum Action {
        NEUTRAL,
        ACTION_1, // Push
        ACTION_2, // Right
        ACTION_3, // Left
        ACTION_4  // Pull
    }


    /**
     * Interface that is used to listen for events from the headset
     */
    public interface HeadsetEvents {

        /**
         * Called when a connection to the headset is established
         */
        void onHeadsetConnected();

        /**
         * Called when the connected headset disconnects
         */
        void onHeadsetDisconnected();

        /**
         * Called when an action is detected
         *
         * @param action the detected action
         */
        void onMentalCommand(Action action);


    }

    private Context mContext;
    private boolean mConnectedToEngine = false;
    private boolean mConnectedToBluetooth = false;

    private int mDeviceUserId = 0; // Hard coded to the first user id

    private Handler mHandler; // Used to run some code at a fixed rate
    private Runnable mRunner; // Code that must be run repeatedly to read event from the headset

    private HeadsetEvents mEventListener; // Used to notify the user of the class

    private Action mCurrentAction = Action.NEUTRAL;

    public EpocEngine(Context context, HeadsetEvents eventListener) {
        mContext = context;
        mEventListener = eventListener;

        initEngine();
        setupEventCheckingLoop();
    }

    /**
     * Disconnect from the epoc engine
     */
    public void disconnect() {
        IEdk.IEE_EngineDisconnect();
    }

    /**
     * Returns the device user id. Currently hardcoded to 0
     *
     * @return the device user id
     */
    public int getmDeviceUserId() {
        return mDeviceUserId;
    }


    /**
     * Override this if the insight headset is used instead of the epoc
     *
     * @return true of connected
     */
    protected boolean connectToBluetoothDevice() {
        return IEdk.IEE_ConnectEpocPlusDevice(mDeviceUserId, false);
    }

    /**
     * Override this if the insight headset is used instead of the epoc
     *
     * @return the number of devices detected
     */
    protected int getDetectedDevicesCount() {
        return IEdk.IEE_GetEpocPlusDeviceCount();
    }


    // Code that schedules the event checking at a fixed rate
    private void setupEventCheckingLoop() {

        mHandler = new Handler();

        mRunner = new Runnable() {
            public void run() {

                if (!mConnectedToBluetooth) {
                    checkBluetoothConnection();
                }
                checkEvents();
                mHandler.postDelayed(mRunner, 100); // Repeat every 100 milli second
            }
        };

        mHandler.postDelayed(mRunner, 100); // Start the event checking code.

    }


    // Check for headset events
    private void checkEvents() {

        int nextEvent = IEdk.IEE_EngineGetNextEvent();

        if (nextEvent == IEdkErrorCode.EDK_NO_EVENT.ToInt()) {
            return;
        } else if (nextEvent == IEdkErrorCode.EDK_OK.ToInt()) { // Some event was successfully executed
            processNextEvent();
        } else {
            // Unknown event. Could be an error or something that probably should be handled.
        }
    }

    // Handle the next event
    private void processNextEvent() {

        int emoEvent = IEdk.IEE_EmoEngineEventGetType();

        if (emoEvent == IEdk.IEE_Event_t.IEE_UserAdded.ToInt()) { // EpocHeadset connected
            mEventListener.onHeadsetConnected();
        } else if (emoEvent == IEdk.IEE_Event_t.IEE_UserRemoved.ToInt()) { // EpocHeadset disconnected
            mConnectedToBluetooth = false;
            mEventListener.onHeadsetDisconnected();
        } else if (emoEvent == IEdk.IEE_Event_t.IEE_EmoStateUpdated.ToInt()) { // Mental command state changed;
            detectAction();
        } else {
            // Unknown event that we currently don't care about
        }
    }

    // Detect mental action
    private void detectAction() {

        IEdk.IEE_EmoEngineEventGetEmoState(); // Copy the state into memory

        int action = IEmoStateDLL.IS_MentalCommandGetCurrentAction();

        // Some hardcoded mapping from mental command to action
        if (action == IEmoStateDLL.IEE_MentalCommandAction_t.MC_NEUTRAL.ToInt()) {
            fireActionIfStrong(Action.NEUTRAL);
        } else if (action == IEmoStateDLL.IEE_MentalCommandAction_t.MC_PUSH.ToInt()) {
            fireActionIfStrong(Action.ACTION_1);
        } else if (action == IEmoStateDLL.IEE_MentalCommandAction_t.MC_RIGHT.ToInt()) {
            fireActionIfStrong(Action.ACTION_2);
        } else if (action == IEmoStateDLL.IEE_MentalCommandAction_t.MC_LEFT.ToInt()) {
            fireActionIfStrong(Action.ACTION_3);
        } else if (action == IEmoStateDLL.IEE_MentalCommandAction_t.MC_PULL.ToInt()) {
            fireActionIfStrong(Action.ACTION_4);
        } else {
            // A command that we currently don't care about
        }
    }

    // Fire taction of the signal is above a hardcoded threshold
    private void fireActionIfStrong(Action action) {
        float power = IEmoStateDLL.IS_MentalCommandGetCurrentActionPower();
        if (action == Action.NEUTRAL || (power > 0.5f && mCurrentAction != action)) {
            mEventListener.onMentalCommand(action);
            mCurrentAction = action;
        }
    }

    // returns true of ok
    private boolean connectToEmotiveEngine() {
        // The string "Epoc+" could be anything, it doesn't have to be "Epoc+"
        return IEdk.IEE_EngineConnect(mContext, "Epoc+") == IEdkErrorCode.EDK_OK.ToInt();
    }


    // Connect to the engine
    private void initEngine() {
        mConnectedToEngine = connectToEmotiveEngine();
    }


    // Check and try to connect to the headset via bluetooth
    private void checkBluetoothConnection() {

        int numberDevice = getDetectedDevicesCount();

        if (numberDevice > 0) {
            mConnectedToBluetooth = connectToBluetoothDevice();
        }
    }


}
