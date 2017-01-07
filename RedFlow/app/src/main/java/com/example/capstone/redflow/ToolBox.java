package com.example.capstone.redflow;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

/**
 * Created by jp on 12/3/16.
 */

public class ToolBox {

    public String nameFormatter(String name) {
        String result;

        name = name.toLowerCase();

        result = name.substring(0, 1).toUpperCase() + name.substring(1);

        return result;
    }

    /**
     * Returns the SHA1 hash for the provided String
     * @param text
     * @return the SHA1 hash or null if an error occurs
     */
    public static String SHA1(String text) {

        try {

            MessageDigest md;
            md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("UTF-8"),
                    0, text.length());
            byte[] sha1hash = md.digest();

            return toHex(sha1hash);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String toHex(byte[] buf) {

        if (buf == null) return "";

        int l = buf.length;
        StringBuffer result = new StringBuffer(2 * l);

        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }

        return result.toString();

    }

    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {

        sb.append(HEX.charAt((b >> 4) & 0x0f))
                .append(HEX.charAt(b & 0x0f));

    }

    public int getCurrentDate() {
        final Calendar c = Calendar.getInstance();

        int date =   (c.get(Calendar.YEAR) * 10000) +
                        ((c.get(Calendar.MONTH) + 1) * 100) +
                        (c.get(Calendar.DAY_OF_MONTH));

        return date;
    }
}
