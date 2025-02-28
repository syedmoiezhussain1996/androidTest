package com.example.androidtest;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.UserManager;
import android.widget.Toast;

public class MyDeviceAdminReceiver extends DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Toast.makeText(context, "Device Admin Enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Toast.makeText(context, "Device Admin Disabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProfileProvisioningComplete(Context context, Intent intent) {
        super.onProfileProvisioningComplete(context, intent);
        Toast.makeText(context, "onProfileProvisioningComplete", Toast.LENGTH_SHORT).show();
        // Get the admin component name
//        ComponentName adminComponent = getComponentName(context);
//
//        // Get the device policy manager
//        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
//
//        // Set policies for device owner mode
//        if (dpm.isDeviceOwnerApp(context.getPackageName())) {
//            // Set device owner policies
//            dpm.setLockTaskPackages(adminComponent, new String[]{context.getPackageName()});
//
//            // Optional: Set additional policies as needed
//            dpm.setStatusBarDisabled(adminComponent, false);
//            dpm.setKeyguardDisabled(adminComponent, false);
//
//            // Start your main activity
//            Intent launchIntent = new Intent(context, MainActivity.class);
//            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(launchIntent);
//
//            Toast.makeText(context, "Device Owner Provisioning Completed", Toast.LENGTH_LONG).show();
//        }
    }


}