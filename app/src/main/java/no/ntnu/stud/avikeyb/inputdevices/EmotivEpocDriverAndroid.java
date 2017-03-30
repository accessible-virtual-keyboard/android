package no.ntnu.stud.avikeyb.inputdevices;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

import no.ntnu.stud.avikeyb.backend.InputInterface;
import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.inputdevices.emotivepoc.EpocEngine;
import no.ntnu.stud.avikeyb.inputdevices.emotivepoc.EpocHeadset;

/**
 * Input driver for the Emotiv Epoc+ headset
 * <p>
 * Connects to the Emotive cloud service to load the user profile and then sends the detected
 * signals to the input interface of the keyboard.
 */
public class EmotivEpocDriverAndroid {

    private static final String LOG_LABEL = "EPOC_HEADSET";

    private InputInterface inputInterface;
    private EpocHeadset headset;
    private Activity activity;
    private String username;
    private String password;

    public EmotivEpocDriverAndroid(Activity activity) {
        this.activity = activity;
    }

    /**
     * Connects to the headset
     * <p>
     * The username and password is used to connect to the Emotiv cloud service. The profile
     * name is the name of the profile from the cloud service that will be loaded when the
     * headset is ready.
     * <p>
     * Currently the loaded training profile can not be changed after the initial connection. If
     * needed this can be added later.
     *
     * @param username    the username of the Emotiv cloud profile
     * @param password    the password of the Emotiv cloud profile
     * @param profileName the training profile to load into the headset
     */
    public void connect(String username, String password, String profileName) {
        if (headset != null) {
            return; // Don't connect more than once
        }
        headset = new EpocHeadset(activity, createEventListener(profileName));

        this.username = username;
        this.password = password;
        new Thread(headset).start();
    }


    /**
     * Set the current active input interface that will receive the input signals from the headset
     *
     * @param inputInterface the input interface that should receive the input signals
     */
    public void setInputInterface(InputInterface inputInterface) {
        this.inputInterface = inputInterface;
    }

    /**
     * Disconnect from the headset
     * <p>
     * This will stop the headset thread and disconnect from the cloud service and the headset
     */
    public void disconnect() {
        headset.disconnect();
        headset = null;
    }

    // The listener that listens for inputs from the headset and sends them to the keyboard
    // input interface
    private EpocHeadset.EventListener createEventListener(final String profileName) {


        final HashMap<EpocEngine.Action, InputType> actionToInput = new HashMap<>();
        actionToInput.put(EpocEngine.Action.ACTION_1, InputType.INPUT1);
        actionToInput.put(EpocEngine.Action.ACTION_2, InputType.INPUT2);
        actionToInput.put(EpocEngine.Action.ACTION_3, InputType.INPUT3);
        actionToInput.put(EpocEngine.Action.ACTION_4, InputType.INPUT4);


        return new EpocHeadset.EventListener() {

            private EpocEngine.Action currentAction = EpocEngine.Action.NEUTRAL;

            @Override
            public void headsetConnected() {
                Log.d(LOG_LABEL, "Headset connected");
                headset.login(username, password);
            }

            @Override
            public void headsetDisconnected() {
                Log.d(LOG_LABEL, "Headset disconnected");
            }

            @Override
            public void profileNamesLoaded(List<String> names) {
                Log.d(LOG_LABEL, "Available profiles: " + names);
                Log.d(LOG_LABEL, "Loading profile: " + profileName);
                headset.loadProfile(profileName);
            }

            @Override
            public void actionDetected(EpocEngine.Action action) {

                if (action != currentAction) {

                    Log.d(LOG_LABEL, "Action detected " + action);

                    if (action != EpocEngine.Action.NEUTRAL) {
                        inputInterface.sendInputSignal(actionToInput.get(action));
                        Log.d(LOG_LABEL, "Sending input signal " + actionToInput.get(action));
                    }

                    currentAction = action;

                }
            }
        };


    }

}
