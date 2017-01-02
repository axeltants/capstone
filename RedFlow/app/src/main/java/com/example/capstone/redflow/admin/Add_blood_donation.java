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
import com.firebase.client.Firebase;

public class Add_blood_donation extends AppCompatActivity {

    private String sDateDonated;
    private String sSerial;

    private EditText vDateDonated;
    private EditText vSerial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.add_blood_donation);

        vDateDonated = (EditText) findViewById(R.id.iedittext_date_donated);
        vSerial = (EditText) findViewById(R.id.eddittext_donation_serial);
    }

    public void add_donation_now(View view) {
        sSerial = vSerial.getText().toString();
        new AlertDialog.Builder(this)
                .setTitle("Please Recheck Serial")
                .setMessage("Is this serial correct (" + sSerial + ")?")
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
