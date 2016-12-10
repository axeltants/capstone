package com.example.capstone.redflow.notification_list;

/**
 * Created by axel on 11/30/16.
 */

public class notificationGetSet {

    private String notifID;
    private String mssge;
    private String date;

    public String getNotifID(){
        return notifID;
    }

    public String getMssge(){
        return mssge;
    }

    public String getDate(){
        return date;
    }

    public notificationGetSet(String notifID, String mssge, String date){
        this.notifID = notifID;
        this.mssge = mssge;
        this.date = date;

    }
}
