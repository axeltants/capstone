package com.example.capstone.redflow.admin;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.redflow.R;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.Map;

public class search_blood_result extends AppCompatActivity {

    private String serial_number;
    private String userID;

    private Firebase mRootRef;
    private Query query;
    private Query query2;

    TextView vCompleteName;
    TextView vBdate;
    TextView vGender;
    TextView vEmail;
    TextView vNationality;
    TextView vAddress;
    TextView vContact;
    TextView vStatus;
    TextView vBloodtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_blood_result);

        serial_number = getIntent().getStringExtra("serial_number");

        Toast.makeText(this, "Welcome, " + serial_number, Toast.LENGTH_SHORT).show();

        vCompleteName = (TextView) findViewById(R.id.textview_CompleteName);
        vBdate = (TextView) findViewById(R.id.textview_age);
        vGender = (TextView) findViewById(R.id.textview_gender);
        vEmail = (TextView) findViewById(R.id.textview_email);
        vNationality = (TextView) findViewById(R.id.textview_nationality);
        vAddress = (TextView) findViewById(R.id.textview_address);
        vContact = (TextView) findViewById(R.id.textview_contact);
        vStatus = (TextView) findViewById(R.id.textview_status);
        vBloodtype = (TextView) findViewById(R.id.textview_bloodtype);

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");
        query = mRootRef.child("Blood").orderByChild("serial").equalTo(serial_number);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                userID = map.get("userID");
                setProfile(userID, map.get("bloodtype"));
                //Toast.makeText(search_blood_result.this, "Welcome, " + userID, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void user_blood(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Give the blood to patient")
                .setMessage("Are u sure you want to use Brown's blood?")
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

    public void setProfile(String userID, final String bloodtype) {
        query2 = mRootRef.child("User").child(userID);
        query2.addValueEventListener(new ValueEventListener() {
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
                vBloodtype.setText(bloodtype);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
