package com.example.capstone.redflow;

import android.widget.Spinner;

import com.example.capstone.redflow.user_activities.History;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;

public class ToolBox {

    public int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

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

    public double getCurrentTime() {

        Calendar c = Calendar.getInstance();

        int day;
        int time;
        int ampm;

        String temp;

        double datetime;


        ampm = c.get(Calendar.AM_PM);

        time =  (c.get(Calendar.HOUR) * 100000) +
                (c.get(Calendar.MINUTE) * 1000) +
                (c.get(Calendar.SECOND) * 10)   +
                ampm;

        if(c.get(Calendar.HOUR) < 1) {
            time += 1200000;
        }

        day = ampm;
        datetime = day + (time * 0.0000001);

        temp = String.format("%.7f", datetime);

        datetime = Double.parseDouble(temp);

        return datetime;
    }

    public double getDateTime() {
        Calendar c = Calendar.getInstance();

        int date;
        int time;
        int ampm;

        double datetime;


        date =   (c.get(Calendar.YEAR) * 10000) +
                ((c.get(Calendar.MONTH) + 1) * 100) +
                (c.get(Calendar.DAY_OF_MONTH));

        ampm = c.get(Calendar.AM_PM);

        time =  (c.get(Calendar.HOUR) * 100000) +
                (c.get(Calendar.MINUTE) * 1000) +
                (c.get(Calendar.SECOND) * 10)   +
                ampm;

        if(c.get(Calendar.HOUR) < 1) {
            time += 1200000;
        }

        datetime = date + (time * 0.0000001);

        return datetime;
    }

    public String timeFormatter(double time) {
        String hour;
        String minute;
        String period;
        String result;

        result = String.valueOf(time);

        hour = result.substring(2, 4);
        minute = result.substring(4, 6);
        period = result.substring(result.length()-1);

        if(period.equals("1")) {
            result = hour + ":" + minute + " PM";
        }
        else {
            result = hour + ":" + minute + " AM";
        }


        return result;
    }

    public String dateFormatter(int date) {
        int year;
        int month;
        int day;

        String result;

        year = date / 10000;
        date = date % (year * 10000);
        month = date / 100;
        date = date % (month * 100);
        day = date;

        switch(month) {
            case 1: result = "January ";
                    break;
            case 2: result = "February ";
                    break;

            case 3: result = "March ";
                    break;

            case 4: result = "April ";
                    break;

            case 5: result = "May ";
                    break;

            case 6: result = "June ";
                    break;

            case 7: result = "July ";
                    break;

            case 8: result = "August ";
                    break;

            case 9: result = "September ";
                    break;

            case 10: result = "October ";
                    break;

            case 11: result = "November ";
                    break;

            case 12: result = "December ";
                    break;

            default: result = "N/A";
        }

        result = result + day + ", " + year;

        return result;
    }

    public ArrayList<History> invertHistory(ArrayList<History> history, int size) {
        ArrayList<History> result = new ArrayList<>();

        for(int i = size-1; i >= 0; i--) {
            result.add(history.get(i));
        }

        return result;
    }

    public int isSerialValid(String serial){
        int result;
        String checkpoint1, checkpoint2;

        checkpoint1 = Character.toString(serial.charAt(4));
        checkpoint2 = Character.toString(serial.charAt(11));

        if(checkpoint1.equals("-") && checkpoint2.equals("-") && serial.matches("[0-9, -]+") && serial.length() == 13) {
            result = 1;
        }
        else {
            result = 0;
        }

        return result;
    }
}
