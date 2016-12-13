package com.example.capstone.redflow.Result_lists;

/**
 * Created by axel on 12/12/16.
 */

public class resultGetSet {
    private String resultID;
    private String fullname;
    private String accstatus;

    public String getResultID(){return resultID;}

    public String getFullname(){return fullname;}

    public String getAccstatuse(){
        return accstatus;
    }

    public resultGetSet(String resultID, String fullname, String date){
        this.resultID = resultID;
        this.fullname = fullname;
        this.accstatus = date;

    }

}
