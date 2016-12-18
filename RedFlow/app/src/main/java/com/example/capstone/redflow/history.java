package com.example.capstone.redflow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class history extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.capstone.redflow.R.layout.history);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void blooddonation_list(View view) {
        Intent intent = new Intent(this, Donation_history.class);
        startActivity(intent);
    }

    public void bloodclaim_list(View view) {
        Intent intent = new Intent(this, Claim_history.class);
        startActivity(intent);
    }
}
