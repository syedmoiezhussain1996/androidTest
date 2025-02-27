package com.example.androidtest;

import java.util.Base64;

public class ChecksumUtil {
    public static void main(String[] args) {
        // The SHA-256 fingerprint from keytool (remove colons and spaces)
        String hexFingerprint = "F3009F534D164DE7AFA90F9DF44EB04E6AFADB79BF1886CE8129BF44C4883CAA";
        
        // Convert hex string to byte array
        byte[] bytes = new byte[hexFingerprint.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hexFingerprint.substring(i * 2, i * 2 + 2), 16);
        }
        
        // Convert to Base64
        String base64Checksum = Base64.getEncoder().encodeToString(bytes);
        System.out.println("Base64 Checksum for QR code: " + base64Checksum);
    }
} 