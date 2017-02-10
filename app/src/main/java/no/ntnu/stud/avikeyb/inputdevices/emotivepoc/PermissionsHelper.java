package no.ntnu.stud.avikeyb.inputdevices.emotivepoc;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Helper class that can be used to request and enable the required permissions (bluetooth, location)
 * and hardware (bluetooth) required to talk to the headset.
 */
public class PermissionsHelper {


    private Activity mActivity;
    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;
    private int PERMISSIONS_REQUEST_BLUETOOTH = 2;


    /**
     * Create a new permissions checker object
     *
     * @param activity the activity that is running the permission checks
     */
    public PermissionsHelper(Activity activity) {

        mActivity = activity;

        BluetoothManager bm = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bm.getAdapter();

    }

    /**
     * Call this to check if all the requirements are met
     *
     * @return true if all requirements are met
     */
    public boolean hasRequirements() {

        if (!mBluetoothAdapter.isEnabled()) {
            return false;
        } else if (!hasBluetoothPermission()) {
            return false;
        }
        return true;
    }

    /**
     * Call this to ask for the required permissions and enabling of hardware
     */
    public void requestRequirements() {

        if (!mBluetoothAdapter.isEnabled()) {
            requestBluetooth();
            return;
        }

        if (!hasBluetoothPermission()) {
            requestLocation();
            return;
        }
    }


    /**
     * Call this from the activitie's onActivityResult method to forward the handling
     * of the result.
     * <p>
     * Takes the same arguments as the standard Activity.onActivityResult method.
     */
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(mActivity, "You must be turn on bluetooth to connect with the headset",
                        Toast.LENGTH_SHORT).show();
            } else if (!hasBluetoothPermission()) {
                requestLocation();
            }
        }
    }

    /**
     * Call this from the activitie's onRequestPermissionsResult method to forward the handling
     * to this method.
     * <p>
     * Takes the same arguments as the standard Activity.onRequestPermissionsResult method.
     */
    public void handlePermimssionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_BLUETOOTH) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(mActivity, "App can't run without this permission",
                        Toast.LENGTH_SHORT).show();
            } else {
                if (!mBluetoothAdapter.isEnabled()) {
                    requestBluetooth();
                }
            }
        }
    }


    private void requestBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        mActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    private void requestLocation() {
        ActivityCompat.requestPermissions(mActivity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_BLUETOOTH);
    }


    private boolean hasBluetoothPermission() {
        return ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


}
