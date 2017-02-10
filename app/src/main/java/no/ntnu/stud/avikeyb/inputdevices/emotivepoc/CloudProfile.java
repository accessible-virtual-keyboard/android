package no.ntnu.stud.avikeyb.inputdevices.emotivepoc;


import android.app.Activity;

import com.emotiv.emotivcloud.EmotivCloudClient;
import com.emotiv.emotivcloud.EmotivCloudErrorCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the login and loading of profile data from the emotive cloud service.
 * <p>
 * After logging in a training profile can be loaded to the active headset by calling
 * the loadProfile method with the name of the profile to load.
 * <p>
 * The api of this class is blocking and must be run in a separate thread to not block the
 * UI thread.
 * <p>
 * A EpocEngine object must be created before this class can be used.
 */
public class CloudProfile {

    private Activity mActivity;
    private boolean mConnected = false;
    private boolean mLoggedIn = false;
    private int mCloudUserId = -1;
    private int mEngineId = 0; // Currently just hardcoded to the first headset

    /**
     * Creates a new cloud profile object.
     * <p>
     * The activity object is required by the emotive sdk
     *
     * @param activity an activity object
     */
    public CloudProfile(Activity activity) {
        mActivity = activity;
    }


    /**
     * Connect to the emotive cloud service
     * <p>
     * This must be called before logging in to the service.
     *
     * @return true if the login was successful
     */
    public boolean connect() {
        int err = EmotivCloudClient.EC_Connect(mActivity);
        mConnected = err == EmotivCloudErrorCode.EC_OK.ToInt();
        return mConnected;
    }


    /**
     * Login to the cloud service with the provided username and password
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return true if successful
     */
    public boolean login(String username, String password) {

        if (!mConnected && !connect()) {
            return false;
        }

        int err = EmotivCloudClient.EC_Login(username, password);

        mLoggedIn = err == EmotivCloudErrorCode.EC_OK.ToInt();

        if (!mLoggedIn) {
            return false;
        }

        mCloudUserId = EmotivCloudClient.EC_GetUserDetail();

        return mLoggedIn;
    }


    /**
     * Logout from the emotive cloud service
     */
    public void logout() {
        EmotivCloudClient.EC_LogOut(mCloudUserId);
        mCloudUserId = -1;
        mLoggedIn = false;
    }


    /**
     * Returns all the available training profiles for the logged in user
     *
     * @return a list of profile names
     */
    public List<String> getProfileNames() {
        List<String> list = new ArrayList<>();
        int count = EmotivCloudClient.EC_GetAllProfileName(mCloudUserId);
        for (int i = 0; i < count; i++) {
            list.add(EmotivCloudClient.EC_ProfileNameAtIndex(mCloudUserId, i));
        }
        return list;
    }

    /**
     * Loads and activates the specified profile name to the connected headset
     *
     * @param name the profile name to load
     * @return true if successful
     */
    public boolean loadProfile(String name) {
        int profileId = EmotivCloudClient.EC_GetProfileId(mCloudUserId, name);

        // -1 means the latest version of the profile
        int err = EmotivCloudClient.EC_LoadUserProfile(mCloudUserId, mEngineId, profileId, -1);

        return err == EmotivCloudErrorCode.EC_OK.ToInt();
    }


}
