package com.example.capstone.redflow.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.capstone.redflow.R;
import com.example.capstone.redflow.history;

public class admin_home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);
    }


    public void statistics(View view) {
        Toast.makeText(getApplicationContext(), "Statistics", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,  admin_home.class);
    }

    public void donors(View view) {
        Toast.makeText(getApplicationContext(), "Donors", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, admin_home.class);
    }

    public void records(View view) {
        Toast.makeText(getApplicationContext(), "records", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,  admin_home.class);
    }

    public void searchuser(View view) {
        Toast.makeText(getApplicationContext(), "search user", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, search_user.class);
        startActivity(intent);
    }
}
