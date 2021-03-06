package com.example.capstone.redflow.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;



import com.example.capstone.redflow.R;
import com.example.capstone.redflow.admin.statistics.*;

public class supply_demmand_menu extends AppCompatActivity {

    private String turf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_demmand_menu);

        turf = getIntent().getStringExtra("turf");
    }

    public void oplus(View view) {
        Intent intent = new Intent(this,  blood_supply_demmand_statistic.class);
        intent.putExtra("bloodtype", "O+");
        intent.putExtra("turf", turf);
        startActivity(intent);
    }

    public void ominus(View view) {
        Intent intent = new Intent(this,  blood_supply_demmand_statistic.class);
        intent.putExtra("bloodtype", "O-");
        intent.putExtra("turf", turf);
        startActivity(intent);
    }

    public void aplus(View view) {
        Intent intent = new Intent(this,  blood_supply_demmand_statistic.class);
        intent.putExtra("bloodtype", "A+");
        intent.putExtra("turf", turf);
        startActivity(intent);
    }

    public void aminus(View view) {
        Intent intent = new Intent(this,  blood_supply_demmand_statistic.class);
        intent.putExtra("bloodtype", "A-");
        intent.putExtra("turf", turf);
        startActivity(intent);
    }

    public void bplus(View view) {
        Intent intent = new Intent(this,  blood_supply_demmand_statistic.class);
        intent.putExtra("bloodtype", "B+");
        intent.putExtra("turf", turf);
        startActivity(intent);
    }

    public void bminus(View view) {
        Intent intent = new Intent(this,  blood_supply_demmand_statistic.class);
        intent.putExtra("bloodtype", "B-");
        intent.putExtra("turf", turf);
        startActivity(intent);
    }

    public void abplus(View view) {
        Intent intent = new Intent(this,  blood_supply_demmand_statistic.class);
        intent.putExtra("bloodtype", "AB+");
        intent.putExtra("turf", turf);
        startActivity(intent);
    }

    public void abminus(View view) {
        Intent intent = new Intent(this,  blood_supply_demmand_statistic.class);
        intent.putExtra("bloodtype", "AB-");
        intent.putExtra("turf", turf);
        startActivity(intent);
    }
}
