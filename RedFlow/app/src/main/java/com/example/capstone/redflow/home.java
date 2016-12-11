package com.example.capstone.redflow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class home extends AppCompatActivity {
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.capstone.redflow.R.layout.home);

        userID = getIntent().getStringExtra("userID");

        //Toast.makeText(this, "Welcome " + userID, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void request(View view) {
        Intent intent = new Intent(this, request.class);
        startActivity(intent);
    }

    public void history(View view) {
        Toast.makeText(getApplicationContext(), "HISTORY", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, history.class);
        startActivity(intent);
    }

    public void profile(View view) {
        Toast.makeText(getApplicationContext(), "PROFILE", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, profile.class);
        startActivity(intent);
    }

    public void notification(View view) {
        Toast.makeText(getApplicationContext(), "NOTIFICATION", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, notification.class);
        startActivity(intent);
    }

    public void donor(View view) {
        Toast.makeText(getApplicationContext(), "DONOR", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, beadonor.class);
        startActivity(intent);
    }

    public void location(View view) {
        Toast.makeText(getApplicationContext(), "Red Cross locations", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, redcross_location.class);
        startActivity(intent);
    }
}
