package com.example.capstone.redflow.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.capstone.redflow.R;

public class statistics_home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void age_stat(View view) {
        /*Intent intent = new Intent(this, statistics_home.class);
        startActivity(intent);*/
        Toast.makeText(getApplicationContext(), "age statistics", Toast.LENGTH_SHORT).show();
    }

    public void bloodtype_stat(View view) {
        /*Intent intent = new Intent(this, statistics_home.class);
        startActivity(intent);*/
        Toast.makeText(getApplicationContext(), "blood type statistics", Toast.LENGTH_SHORT).show();
    }

    public void gender_stat(View view) {
        /*Intent intent = new Intent(this, statistics_home.class);
        startActivity(intent);*/
        Toast.makeText(getApplicationContext(), "gender statistics", Toast.LENGTH_SHORT).show();
    }

    public void province_stat(View view) {
        /*Intent intent = new Intent(this, statistics_home.class);
        startActivity(intent);*/
        Toast.makeText(getApplicationContext(), "province statistics", Toast.LENGTH_SHORT).show();
    }
}
