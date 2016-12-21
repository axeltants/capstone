package com.example.capstone.redflow.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.capstone.redflow.R;

public class blood_supply_record extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blood_supply_record);
    }

    public void oplus(View view) {
        Toast.makeText(getApplicationContext(), "O+", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, blood_supply_info.class);
        startActivity(intent);
    }

    public void ominus(View view) {
        Toast.makeText(getApplicationContext(), "O-", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, blood_supply_info.class);
        startActivity(intent);
    }

    public void aplus(View view) {
        Toast.makeText(getApplicationContext(), "A+", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, blood_supply_info.class);
        startActivity(intent);
    }

    public void aminus(View view) {
        Toast.makeText(getApplicationContext(), "A-", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, blood_supply_info.class);
        startActivity(intent);
    }

    public void bplus(View view) {
        Toast.makeText(getApplicationContext(), "B+", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, blood_supply_info.class);
        startActivity(intent);
    }

    public void bminus(View view) {
        Toast.makeText(getApplicationContext(), "B-", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, blood_supply_info.class);
        startActivity(intent);
    }

    public void abplus(View view) {
        Toast.makeText(getApplicationContext(), "AB+", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, blood_supply_info.class);
        startActivity(intent);
    }

    public void abminus(View view) {
        Toast.makeText(getApplicationContext(), "AB-", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, blood_supply_info.class);
        startActivity(intent);
    }
}
