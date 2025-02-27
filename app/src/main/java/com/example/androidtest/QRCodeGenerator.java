package com.example.androidtest;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.Uri;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import android.graphics.Color;
import android.util.Base64;
import java.security.MessageDigest;
import android.content.pm.ApplicationInfo;
import android.widget.Toast;
import android.util.Log;
import java.util.TimeZone;
import java.util.Locale;

public class QRCodeGenerator {
    public static String generateProvisioningData(Context context, String packageName) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject adminExtras = new JSONObject();
            
            // Required parameters
            jsonObject.put("android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_NAME", packageName);
            jsonObject.put("android.app.extra.PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME", 
                    packageName + "/.MyDeviceAdminReceiver");

            // Add package signature checksum
            String signature = SignatureUtil.getSignatureChecksum(context);
            if (signature != null) {
                jsonObject.put("android.app.extra.PROVISIONING_DEVICE_ADMIN_SIGNATURE_CHECKSUM", signature);
            }

            // APK download location
            jsonObject.put("android.app.extra.PROVISIONING_DOWNLOAD_LOCATION", 
                    "https://github.com/syedmoiezhussain1996/myfirstapp/releases/download/v1.0-beta.1/app-release.apk");

            // Optional parameters to make setup smoother
            jsonObject.put("android.app.extra.PROVISIONING_SKIP_ENCRYPTION", true);
            jsonObject.put("android.app.extra.PROVISIONING_SKIP_USER_CONSENT", true);
            jsonObject.put("android.app.extra.PROVISIONING_SKIP_EDUCATION_SCREENS", true);
            jsonObject.put("android.app.extra.PROVISIONING_SKIP_USER_SETUP", true);
            jsonObject.put("android.app.extra.PROVISIONING_LEAVE_ALL_SYSTEM_APPS_ENABLED", true);

            // System settings
            jsonObject.put("android.app.extra.PROVISIONING_LOCALE", Locale.getDefault().toString());
            jsonObject.put("android.app.extra.PROVISIONING_TIME_ZONE", TimeZone.getDefault().getID());
            jsonObject.put("android.app.extra.PROVISIONING_LOCAL_TIME", String.valueOf(System.currentTimeMillis()));

            // WiFi settings (optional, remove if not needed)
            jsonObject.put("android.app.extra.PROVISIONING_WIFI_SSID", "WiFi");

            // Admin extras bundle
            adminExtras.put("com.example.androidtest.extra.PROVISION_MODE", "qr_code");
            jsonObject.put("android.app.extra.PROVISIONING_ADMIN_EXTRAS_BUNDLE", adminExtras);

            // Log the JSON for debugging
            Log.d("QRCodeGenerator", "Generated JSON: " + jsonObject.toString());
            
            // Create the QR code content in the format Android expects
            return "QR:" + jsonObject.toString();
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap generateQRCode(String content, int width, int height) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                content, 
                BarcodeFormat.QR_CODE, 
                width, 
                height
            );

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
} 