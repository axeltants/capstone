package com.example.capstone.redflow.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.capstone.redflow.R;

public class Add_blood_donation extends AppCompatActivity {

    private EditText serial;
    private String serialC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_blood_donation);

        serial = (EditText) findViewById(R.id.eddittext_donation_serial);
    }

    public void add_donation_now(View view) {
        serialC = serial.getText().toString();
        new AlertDialog.Builder(this)
                .setTitle("Please Recheck Serial")
                .setMessage("Is this serial correct ("+serialC+")?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do something
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
