package com.example.capstone.redflow.Blood_history.claim_lists;

/**
 * Created by axel on 12/18/16.
 */

public class claimGetSet {
    private String notifID;
    private String cdate;

    public String getclaimID(){
        return notifID;
    }


    public String getDate(){
        return cdate;
    }

    public claimGetSet(String notifID, String date){
        this.notifID = notifID;
        this.cdate = date;

    }
}
