package com.example.capstone.redflow.Blood_history.donation_lists;

/**
 * Created by axel on 12/18/16.
 */

public class donationGetSet {
    private String notifID;
    private String date;

    public String getdonationID(){
        return notifID;
    }


    public String getDate(){
        return date;
    }

    public donationGetSet(String notifID, String date){
        this.notifID = notifID;
        this.date = date;

    }
}
