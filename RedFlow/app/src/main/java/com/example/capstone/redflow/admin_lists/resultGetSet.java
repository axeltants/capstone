package com.example.capstone.redflow.admin_lists;

/**
 * Created by axel on 12/12/16.
 */

public class resultGetSet {
    private String resultID;
    private String fullname;
    private String accstatus;

    public String getResultID(){return resultID;}

    public String getFullname(){
        return fullname;
    }

    public String getAccstatuse(){
        return accstatus;
    }

    public resultGetSet(String resultID, String mssge, String date){
        this.resultID = resultID;
        this.fullname = mssge;
        this.accstatus = date;

    }

}
