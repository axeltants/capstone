package com.example.capstone.redflow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class proceed_to_RedCross extends AppCompatActivity {

    private String bloodtype;
    private int bloodcount;

    private TextView vBloodcount;
    private TextView vBloodtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proceed_to__red_cross);

        bloodtype = getIntent().getStringExtra("bloodtype");
        bloodcount = getIntent().getIntExtra("bloodcount", 0);

        vBloodcount = (TextView) findViewById(R.id.edittext_bloodcount);
        vBloodtype = (TextView) findViewById(R.id.edittext_bloodtype);

        vBloodcount.setText(" (" + bloodcount + ") ");
        vBloodtype.setText(" " + bloodtype + " ");

    }

    public void location(View view) {
        Intent intent = new Intent(this, redcross_location.class);
        startActivity(intent);
    }
}
