package com.example.capstone.redflow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class proceed_to_RedCross extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proceed_to__red_cross);
    }

    public void location(View view) {
        Intent intent = new Intent(this, redcross_location.class);
        startActivity(intent);
    }
}
