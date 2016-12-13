package com.example.capstone.redflow.users_list;

/**
 * Created by axel on 12/13/16.
 */

public class usersGetSet {
    private String userID;
    private String fullname;
    private String history;

    public String getuserID(){return userID;}

    public String getFullname(){return fullname;}

    public String gethistory(){return history;}

    public usersGetSet(String userID, String fullname, String date){
        this.userID = userID;
        this.fullname = fullname;
        this.history = date;
    }
}
