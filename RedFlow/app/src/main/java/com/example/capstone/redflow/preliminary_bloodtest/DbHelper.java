package com.example.capstone.redflow.preliminary_bloodtest;

/**
 * Created by axel on 12/14/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "triviaQuiz";
    private static final String TABLE_QUEST = "quest";
    private static final String KEY_ID = "id";
    private static final String KEY_QUES = "question";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_OPTA = "opta";
    private static final String KEY_OPTB = "optb";
    private static final String KEY_OPTC = "optc";
    private SQLiteDatabase dbase;

    public DbHelper(Context context) {
        super(context, "triviaQuiz", (CursorFactory)null, 2);
    }

    public void onCreate(SQLiteDatabase db) {
        this.dbase = db;
        String sql = "CREATE TABLE IF NOT EXISTS quest ( id INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT, answer TEXT, opta TEXT, optb TEXT, optc TEXT)";
        db.execSQL(sql);
        this.addQuestions();
    }

    private void addQuestions() {
        QuestionGetSet q1 = new QuestionGetSet("Did you get 6 hours of sleep?", "Yes", "No","(1 / 20)","Yes");
        this.addQuestion(q1);
        QuestionGetSet q2 = new QuestionGetSet("In the past 12 to 24 hours have you taken an alcoholic drinks?", "Yes", "No","(2 / 20)","No");
        this.addQuestion(q2);
        QuestionGetSet q3 = new QuestionGetSet("In the past 3 to 4 hours did you smoke?",  "Yes", "No","(3 / 20)","No");
        this.addQuestion(q3);
        QuestionGetSet q4 = new QuestionGetSet("In the past 4 hours did you eat your Breakfast/Dinner/Lunch?",  "Yes", "No","(4 / 20)","Yes");
        this.addQuestion(q4);
        QuestionGetSet q5 = new QuestionGetSet("Are you feeling well and healthy today?",  "Yes", "No","(5 / 20)","Yes");
        this.addQuestion(q5);
        QuestionGetSet q6 = new QuestionGetSet("In the pas3 3-4 months have you ever been injected by a vaccine?",  "Yes", "No","(6 / 20)","No");
        this.addQuestion(q6);
        QuestionGetSet q7 = new QuestionGetSet("Have you been taking an aspirin or other drugs that contains aspirin?",  "Yes", "No","(7 / 20)","No");
        this.addQuestion(q7);
        QuestionGetSet q8 = new QuestionGetSet("Have you ever had blood transfusion last 3-4months?",  "Yes", "No","(8 / 20)","No");
        this.addQuestion(q8);
        QuestionGetSet q9 = new QuestionGetSet("Have you ever had a surgery or removal of tooth?",  "Yes", "No","(9 / 20)","No");
        this.addQuestion(q9);
        QuestionGetSet q10 = new QuestionGetSet("Do you have Tatoo, piercing, or tried accupuncture?",  "Yes", "No","(10 / 20)","No");
        this.addQuestion(q10);
        QuestionGetSet q11 = new QuestionGetSet("Have you ever had sex for money",  "Yes", "No","(11 / 20)","No");
        this.addQuestion(q11);
        QuestionGetSet q12 = new QuestionGetSet("Do you have someone close to you that is infected by a chronic hepatitis?",  "Yes", "No","(12 / 20)","No");
        this.addQuestion(q12);
        QuestionGetSet q13 = new QuestionGetSet("Have you ever used a needle to inject a drugs/ steroids or other drugs that is not advised by your doctor?",  "Yes", "No","(13 / 20)","No");
        this.addQuestion(q13);
        QuestionGetSet q14 = new QuestionGetSet("Do you have lung or heart problem?",  "Yes", "No","(14 / 20)","No");
        this.addQuestion(q14);
        QuestionGetSet q15 = new QuestionGetSet("Have you ever been infected by a hepatitis?",  "Yes", "No","(15 / 20)","No");
        this.addQuestion(q15);
        QuestionGetSet q16 = new QuestionGetSet("Have you ever been infected by malaria virus?",  "Yes", "No","(16 / 20)","No");
        this.addQuestion(q16);
        QuestionGetSet q17 = new QuestionGetSet("Do you want to donate blood to test if you are Hepatitis or HIV positive?",  "Yes", "No","(17 / 20)","No");
        this.addQuestion(q17);
        QuestionGetSet q18 = new QuestionGetSet("Have you ever tried having unplanned or sex without your consent?",  "Yes", "No","(18 / 20)","No");
        this.addQuestion(q18);
        QuestionGetSet q19 = new QuestionGetSet("Have you ever tried donating blood?",  "Yes", "No","(19 / 20)","Yes");
        this.addQuestion(q19);
        QuestionGetSet q20 = new QuestionGetSet("If Red Cross needs a donor for blood, do you want to donate?",  "Yes", "No","(20 / 20)","Yes");
        this.addQuestion(q20);

    }

    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS quest");
        this.onCreate(db);
    }

    public void addQuestion(QuestionGetSet quest) {
        ContentValues values = new ContentValues();
        values.put("question", quest.getQUESTION());
        values.put("answer", quest.getANSWER());
        values.put("opta", quest.getOPTA());
        values.put("optb", quest.getOPTB());
        values.put("optc", quest.getOPTC());
        this.dbase.insert("quest", (String)null, values);
    }

    public List<QuestionGetSet> getAllQuestions() {
        ArrayList quesList = new ArrayList();
        String selectQuery = "SELECT  * FROM quest";
        this.dbase = this.getReadableDatabase();
        Cursor cursor = this.dbase.rawQuery(selectQuery, (String[])null);
        if(cursor.moveToFirst()) {
            do {
                QuestionGetSet quest = new QuestionGetSet();
                quest.setID(cursor.getInt(0));
                quest.setQUESTION(cursor.getString(1));
                quest.setANSWER(cursor.getString(2));
                quest.setOPTA(cursor.getString(3));
                quest.setOPTB(cursor.getString(4));
                quest.setOPTC(cursor.getString(5));
                quesList.add(quest);
            } while(cursor.moveToNext());
        }

        return quesList;
    }

    public int rowcount() {
        boolean row = false;
        String selectQuery = "SELECT  * FROM quest";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, (String[])null);
        int row1 = cursor.getCount();
        return row1;
    }
}

