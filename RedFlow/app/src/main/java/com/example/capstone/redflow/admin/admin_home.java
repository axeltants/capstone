package com.example.capstone.redflow.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.capstone.redflow.R;

public class admin_home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void statistics(View view) {
        Intent intent = new Intent(this,  statistics_home.class);
        startActivity(intent);
    }

    public void donors(View view) {
        Intent intent = new Intent(this, registered_users.class);
        startActivity(intent);
    }

    public void blood_supply_records(View view) {
        Intent intent = new Intent(this,  blood_supply_record.class);
        startActivity(intent);
    }


    public void search(View view) {
        Intent intent = new Intent(this, search_menu.class);
        startActivity(intent);
    }
}
