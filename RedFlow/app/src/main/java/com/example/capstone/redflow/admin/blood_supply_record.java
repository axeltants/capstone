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
        Intent intent = new Intent(this, blood_supply_info.class);
        intent.putExtra("blood_type", "O+");
        startActivity(intent);
    }

    public void ominus(View view) {
        Intent intent = new Intent(this, blood_supply_info.class);
        intent.putExtra("blood_type", "O-");
        startActivity(intent);
    }

    public void aplus(View view) {
        Intent intent = new Intent(this, blood_supply_info.class);
        intent.putExtra("blood_type", "A+");
        startActivity(intent);
    }

    public void aminus(View view) {
        Intent intent = new Intent(this, blood_supply_info.class);
        intent.putExtra("blood_type", "A-");
        startActivity(intent);
    }

    public void bplus(View view) {
        Intent intent = new Intent(this, blood_supply_info.class);
        intent.putExtra("blood_type", "B+");
        startActivity(intent);
    }

    public void bminus(View view) {
        Intent intent = new Intent(this, blood_supply_info.class);
        intent.putExtra("blood_type", "B-");
        startActivity(intent);
    }

    public void abplus(View view) {
        Intent intent = new Intent(this, blood_supply_info.class);
        intent.putExtra("blood_type", "AB+");
        startActivity(intent);
    }

    public void abminus(View view) {
        Intent intent = new Intent(this, blood_supply_info.class);
        intent.putExtra("blood_type", "AB-");
        startActivity(intent);
    }
}
