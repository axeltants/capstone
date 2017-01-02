package com.example.capstone.redflow.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.redflow.R;
import com.example.capstone.redflow.request;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

public class user_profile_admin extends AppCompatActivity {

    private String userID;

    private Firebase mRootRef;

    private TextView vCompleteName;
    private TextView vBdate;
    private TextView vGender;
    private TextView vEmail;
    private TextView vNationality;
    private TextView vAddress;
    private TextView vContact;
    private TextView vStatus;
    private TextView vBloodtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_admin);

        userID = getIntent().getStringExtra("userID");

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");

        vCompleteName = (TextView) findViewById(R.id.textview_CompleteName);
        vBdate = (TextView) findViewById(R.id.textview_age);
        vGender = (TextView) findViewById(R.id.textview_gender);
        vEmail = (TextView) findViewById(R.id.textview_email);
        vNationality = (TextView) findViewById(R.id.textview_nationality);
        vAddress = (TextView) findViewById(R.id.textview_address);
        vContact = (TextView) findViewById(R.id.textview_contact);
        vStatus = (TextView) findViewById(R.id.textview_status);
        vBloodtype = (TextView) findViewById(R.id.textview_bloodtype);

        mRootRef.child("User").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);

                vCompleteName.setText(map.get("lname") + ", " + map.get("fname"));
                vBdate.setText(map.get("bday"));
                vGender.setText(map.get("gender"));
                vEmail.setText(map.get("email"));
                vNationality.setText(map.get("nationality"));
                vAddress.setText(map.get("home"));
                vContact.setText(map.get("contact"));
                vStatus.setText(map.get("status"));
                vBloodtype.setText(map.get("bloodtype"));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void add_donation(View view) {
        Intent intent = new Intent(this, Add_blood_donation.class);
        startActivity(intent);
    }
}
