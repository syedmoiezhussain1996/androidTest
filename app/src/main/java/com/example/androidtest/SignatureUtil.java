package com.example.androidtest;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import java.security.MessageDigest;

public class SignatureUtil {
    public static String getSignatureChecksum(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 
                    PackageManager.GET_SIGNING_CERTIFICATES);

            if (packageInfo != null && packageInfo.signingInfo != null) {
                Signature[] signatures = packageInfo.signingInfo.getSigningCertificateHistory();
                if (signatures != null && signatures.length > 0) {
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] signatureBytes = digest.digest(signatures[0].toByteArray());
                    String checksum = Base64.encodeToString(signatureBytes, Base64.NO_WRAP);
                    System.out.println("Signature Checksum: " + checksum);
                    return checksum;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
} 