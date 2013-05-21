package com.hastarin.android.udpsender;

import java.util.Locale;

public class HexHelper {
    public static byte[] hexStringToBytes(String input) {
        input = input.toLowerCase(Locale.US);
        int n = input.length() / 2;
        byte[] output = new byte[n];
        int l = 0;
        for (int k = 0; k < n; k++) {
            char c = input.charAt(l++);
            byte b = (byte) ((c >= 'a' ? (c - 'a' + 10) : (c - '0')) << 4);
            c = input.charAt(l++);
            b |= (byte) (c >= 'a' ? (c - 'a' + 10) : (c - '0'));
            output[k] = b;
        }
        return output;
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
