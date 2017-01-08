package com.example.capstone.redflow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class zero_supply_request extends AppCompatActivity {
    private String bloodtype;
    private TextView vBloodtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zero_supply_request);

        bloodtype = getIntent().getStringExtra("bloodtype");
        vBloodtype = (TextView) findViewById(R.id.btype);
        vBloodtype.setText(bloodtype);
    }
}
