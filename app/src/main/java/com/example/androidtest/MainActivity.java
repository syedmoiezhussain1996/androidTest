package com.example.androidtest;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.graphics.Bitmap;

public class MainActivity extends AppCompatActivity {

    private DevicePolicyManager devicePolicyManager;
    private ComponentName deviceAdminComponent;
    private TextView deviceOwnerStatusTextView;
    private Button btnToggleDeviceOwner;
    private Button btnToggleKioskMode;
    private boolean isKioskModeEnabled = false;
    private TextView qrContentTextView;
    private ImageView qrCodeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        deviceOwnerStatusTextView = findViewById(R.id.deviceOwnerStatus);
        Button btnLockScreen = findViewById(R.id.btnLockScreen);
        btnToggleDeviceOwner = findViewById(R.id.btnToggleDeviceOwner);
        btnToggleKioskMode = findViewById(R.id.btnToggleKioskMode);
        qrContentTextView = findViewById(R.id.qrContentTextView);
        qrCodeImageView = findViewById(R.id.qrCodeImageView);

        // Initialize DevicePolicyManager and ComponentName
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        deviceAdminComponent = new ComponentName(this, MyDeviceAdminReceiver.class);

        // Update Device Owner status
        updateDeviceOwnerStatus();

        // Lock Screen Button
        btnLockScreen.setOnClickListener(v -> lockScreen());

        // Toggle Device Owner Button
        btnToggleDeviceOwner.setOnClickListener(v -> toggleDeviceOwner());

        // Toggle Kiosk Mode Button
        btnToggleKioskMode.setOnClickListener(v -> toggleKioskMode());

        // Add QR code content display
        Button btnShowQRContent = findViewById(R.id.btnShowQRContent);
        
        btnShowQRContent.setOnClickListener(v -> generateAndShowQRCode());
    }

    // Lock the device screen
    private void lockScreen() {
        if (devicePolicyManager.isAdminActive(deviceAdminComponent)) {
            devicePolicyManager.lockNow();
            Toast.makeText(this, "Screen Locked", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Device Admin Not Active", Toast.LENGTH_SHORT).show();
        }
    }

    // Toggle Device Owner mode
    private void toggleDeviceOwner() {
        if (devicePolicyManager.isDeviceOwnerApp(getPackageName())) {
            // Disable Device Owner (this is not possible programmatically)
            Toast.makeText(this, "Cannot disable Device Owner programmatically", Toast.LENGTH_SHORT).show();
        } else {
            // Enable Device Owner (requires ADB command)
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdminComponent);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Enable Device Owner to use Kiosk Mode");
            startActivityForResult(intent, 1);
        }
    }

    // Toggle Kiosk Mode
    private void toggleKioskMode() {
        if (devicePolicyManager.isDeviceOwnerApp(getPackageName())) {
            if (isKioskModeEnabled) {
                // Disable Kiosk Mode
                devicePolicyManager.clearPackagePersistentPreferredActivities(
                        deviceAdminComponent, getPackageName());
                // Disable screen pinning
                devicePolicyManager.setLockTaskPackages(deviceAdminComponent, new String[]{});
                stopLockTask();
                Toast.makeText(this, "Kiosk Mode Disabled", Toast.LENGTH_SHORT).show();
                isKioskModeEnabled = false;
                btnToggleKioskMode.setText("Enable Kiosk Mode");
            } else {
                // Enable Kiosk Mode
                IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MAIN);
                intentFilter.addCategory(Intent.CATEGORY_HOME);
                intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

                devicePolicyManager.addPersistentPreferredActivity(
                        deviceAdminComponent, intentFilter, new ComponentName(this, MainActivity.class));
                
                // Enable screen pinning
                devicePolicyManager.setLockTaskPackages(deviceAdminComponent, new String[]{getPackageName()});
                startLockTask();
                
                Toast.makeText(this, "Kiosk Mode Enabled", Toast.LENGTH_SHORT).show();
                isKioskModeEnabled = true;
                btnToggleKioskMode.setText("Disable Kiosk Mode");
            }
        } else {
            Toast.makeText(this, "App is not the Device Owner", Toast.LENGTH_SHORT).show();
        }
    }

    // Update Device Owner status
    private void updateDeviceOwnerStatus() {
        boolean isDeviceOwner = devicePolicyManager.isDeviceOwnerApp(getPackageName());
        if (isDeviceOwner) {
            deviceOwnerStatusTextView.setText("Device Owner Status: YES");
            btnToggleDeviceOwner.setText("Disable Device Owner (Not Possible)");
        } else {
            deviceOwnerStatusTextView.setText("Device Owner Status: NO");
            btnToggleDeviceOwner.setText("Enable Device Owner");
        }
    }

    private void generateAndShowQRCode() {
        // Get and display the signature checksum
        String signatureChecksum = SignatureUtil.getSignatureChecksum(this);
        if (signatureChecksum != null) {
            Toast.makeText(this, "Signature Checksum: " + signatureChecksum, Toast.LENGTH_LONG).show();
        }

        String qrContent = QRCodeGenerator.generateProvisioningData(this, getPackageName());
        if (qrContent != null) {
            Bitmap qrCodeBitmap = QRCodeGenerator.generateQRCode(qrContent, 500, 500);
            if (qrCodeBitmap != null) {
                qrCodeImageView.setImageBitmap(qrCodeBitmap);
                qrContentTextView.setText("Signature Checksum:\n" + signatureChecksum + 
                    "\n\nScan this QR code during device setup to provision the device");
            } else {
                Toast.makeText(this, "Error generating QR code", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error generating QR content", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            updateDeviceOwnerStatus();
            Toast.makeText(this, "Device Owner Enabled", Toast.LENGTH_SHORT).show();
        }
    }
}