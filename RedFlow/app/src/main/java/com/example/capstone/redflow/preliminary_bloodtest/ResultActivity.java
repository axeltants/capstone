package com.example.capstone.redflow.preliminary_bloodtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.redflow.R;


public class ResultActivity extends Activity {
    public ResultActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.prelimtest_result);
        RatingBar bar = (RatingBar)this.findViewById(R.id.ratingBar);
        bar.setNumStars(20);
        bar.setMax(5);
        bar.setStepSize(0.5F);
        TextView t2 = (TextView)this.findViewById(R.id.textResult1);
        TextView t = (TextView)this.findViewById(R.id.textResult);
        Bundle b = this.getIntent().getExtras();
        int score = b.getInt("score");
        float score2 = ((float)score/20)*100;
        String scoreF = Float.toString((int)score2)+"%";
        bar.setRating(score);
        switch(score) {
            case 1: t.setText("sorry, You have fail the preliminary test, but if you are willing to donate blood you can still proceed to red cross and file a request to donate a blood");
                t2.setText(scoreF);
                break;
            case 2:t.setText("sorry, You have fail the preliminary test, but if you are willing to donate blood you can still proceed to red cross and file a request to donate a blood");
                t2.setText(scoreF);
                break;
            case 3:t.setText("sorry, You have fail the preliminary test, but if you are willing to donate blood you can still proceed to red cross and file a request to donate a blood");
                t2.setText(scoreF);
                break;
            case 4:t.setText("sorry, You have fail the preliminary test, but if you are willing to donate blood you can still proceed to red cross and file a request to donate a blood");
                t2.setText(scoreF);
                break;
            case 5:t.setText("sorry, You have fail the preliminary test, but if you are willing to donate blood you can still proceed to red cross and file a request to donate a blood");
                t2.setText(scoreF);
                break;
            case 6:t.setText("sorry, You have fail the preliminary test, but if you are willing to donate blood you can still proceed to red cross and file a request to donate a blood");
                t2.setText(scoreF);
                break;
            case 7:t.setText("sorry, You have fail the preliminary test, but if you are willing to donate blood you can still proceed to red cross and file a request to donate a blood");
                t2.setText(scoreF);
                break;
            case 8:t.setText("sorry, You have fail the preliminary test, but if you are willing to donate blood you can still proceed to red cross and file a request to donate a blood");
                t2.setText(scoreF);
                break;
            case 9:t.setText("sorry, You have fail the preliminary test, but if you are willing to donate blood you can still proceed to red cross and file a request to donate a blood");
                t2.setText(scoreF);
                break;
            case 10:t.setText("Congratulations, You have passed the inapp preliminary test, You are healthy enough to donate blood");
                t2.setText(scoreF);
                break;
            case 11:t.setText("Congratulations, You have passed the inapp preliminary test, You are healthy enough to donate blood");
                t2.setText(scoreF);
                break;
            case 12:t.setText("Congratulations, You have passed the inapp preliminary test, You are healthy enough to donate blood");
                t2.setText(scoreF);
                break;
            case 13:t.setText("Congratulations, You have passed the inapp preliminary test, You are healthy enough to donate blood");
                break;
            case 14:t.setText("Congratulations, You have passed the inapp preliminary test, You are healthy enough to donate blood");
                t2.setText(scoreF);
                break;
            case 15:t.setText("Congratulations, You have passed the inapp preliminary test, You are healthy enough to donate blood");
                t2.setText(scoreF);
                break;
            case 16:t.setText("Congratulations, You have passed the inapp preliminary test, You are healthy enough to donate blood");
                t2.setText(scoreF);
                break;
            case 17:t.setText("Congratulations, You have passed the inapp preliminary test, You are healthy enough to donate blood");
                t2.setText(scoreF);
                break;
            case 18:t.setText("Congratulations, You have passed the inapp preliminary test, You are healthy enough to donate blood");
                t2.setText(scoreF);
                break;
            case 19:t.setText("Congratulations, You have passed the inapp preliminary test, You are healthy enough to donate blood");
                t2.setText(scoreF);
                break;
            case 20:t.setText("Congratulations, You have passed the inapp preliminary test, You are healthy enough to donate blood");
                t2.setText(scoreF);
                break;


        }
        Toast toast = Toast.makeText(getApplicationContext(), String.valueOf(score2)+'%', Toast.LENGTH_SHORT);
        toast.show();
    }
}
