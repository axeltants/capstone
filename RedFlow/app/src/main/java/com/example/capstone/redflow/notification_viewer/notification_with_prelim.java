package com.example.capstone.redflow.notification_viewer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.redflow.R;
import com.example.capstone.redflow.preliminary_bloodtest.TestActivity;

import java.util.Calendar;

import static com.example.capstone.redflow.common_activities.LoginActivity.Uid;

public class notification_with_prelim extends AppCompatActivity {


    TextView title;
    TextView content;
    TextView date;

    private String notiftitle;
    private String notifcontent;
    private String notifdate;
    private String uid;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_with_prelim);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        uid = sharedpreferences.getString(Uid, ""); // MAU NI NGA VARIABLE GAMITA TER

        title = (TextView) findViewById(R.id.notifTitle);
        content = (TextView) findViewById(R.id.notifContent);
        date = (TextView) findViewById(R.id.ndate);

        notifdate = getIntent().getStringExtra("date");
        notiftitle = getIntent().getStringExtra("title");
        notifcontent = getIntent().getStringExtra("content");

        title.setText(notiftitle);
        content.setText(notifcontent);
        date.setText(notifdate);
    }

    public void prelim_test(View view) {
        Toast.makeText(getApplicationContext(), "Health test", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }

    public void bloodcompat(View view) {
        Toast.makeText(getApplicationContext(), "Blood compatibility", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Blood_Type_Compat.class);
        startActivity(intent);
    }
}
