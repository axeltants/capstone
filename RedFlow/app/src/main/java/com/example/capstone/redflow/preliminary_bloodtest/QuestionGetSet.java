package com.example.capstone.redflow.preliminary_bloodtest;

/**
 * Created by axel on 12/14/16.
 */

public class QuestionGetSet {
    private int ID;
    private String QUESTION;
    private String OPTA;
    private String OPTB;
    private String OPTC;
    private String ANSWER;

    public QuestionGetSet() {
        this.ID = 0;
        this.QUESTION = "";
        this.OPTA = "";
        this.OPTB = "";
        this.OPTC = "";
        this.ANSWER = "";
    }

    public QuestionGetSet(String qUESTION, String oPTA, String oPTB, String oPTC, String aNSWER) {
        this.QUESTION = qUESTION;
        this.OPTA = oPTA;
        this.OPTB = oPTB;
        this.OPTC = oPTC;
        this.ANSWER = aNSWER;
    }

    public int getID() {
        return this.ID;
    }

    public String getQUESTION() {
        return this.QUESTION;
    }

    public String getOPTA() {
        return this.OPTA;
    }

    public String getOPTB() {
        return this.OPTB;
    }

    public String getOPTC() {
        return this.OPTC;
    }

    public String getANSWER() {
        return this.ANSWER;
    }

    public void setID(int id) {
        this.ID = id;
    }

    public void setQUESTION(String qUESTION) {
        this.QUESTION = qUESTION;
    }

    public void setOPTA(String oPTA) {
        this.OPTA = oPTA;
    }

    public void setOPTB(String oPTB) {
        this.OPTB = oPTB;
    }

    public void setOPTC(String oPTC) { this.OPTC = oPTC; }

    public void setANSWER(String aNSWER) {
        this.ANSWER = aNSWER;
    }
}

