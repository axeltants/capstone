package com.example.capstone.redflow.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.redflow.LoginActivity;
import com.example.capstone.redflow.R;
import com.example.capstone.redflow.SendRequest;
import com.example.capstone.redflow.about;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class search_blood_result extends AppCompatActivity {

    private String serial_number;
    private String userID;
    private String contact;
    private String message;
    private String status;
    private String bloodtype;
    private String bloodID;

    private int bloodcount;

    private Firebase mRootRef;
    private Firebase userRef;
    private Firebase bloodRef;
    private Firebase supplyRef;
    private Query query;
    private Query query2;
    private Query query3;

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
        Firebase.setAndroidContext(this);
        setContentView(R.layout.search_blood_result);

        serial_number = getIntent().getStringExtra("serial_number");

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
        userRef = mRootRef.child("User");
        bloodRef = mRootRef.child("Blood");
        supplyRef = mRootRef.child("Supply");

        query = bloodRef.orderByChild("serial").equalTo(serial_number);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bloodRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                userID = map.get("userID");
                bloodtype = map.get("bloodtype");
                bloodID = dataSnapshot.getKey();
                setProfile(userID, bloodtype);
                getBloodCount();
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
                .setMessage("Are you sure you want to use the blood?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(status.equals("Unknown")) {
                            Toast.makeText(search_blood_result.this, bloodtype + " blood supply reduced by 1 bag.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            message = "Your blood has just been donated. Thank you for saving a life.";
                            new SendRequest(contact, message).execute();
                        }
                        mRootRef.child("Supply").child(bloodtype).child("count").setValue(bloodcount-1);
                        mRootRef.child("Blood").child(bloodID).removeValue();
                        Intent intent = new Intent(search_blood_result.this, blood_supply_info.class);
                        intent.putExtra("blood_type", bloodtype);
                        startActivity(intent);
                        search_blood_result.this.finish();
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
        query2 = userRef.child(userID);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);

                contact = map.get("contact");
                status = map.get("status");

                vCompleteName.setText(map.get("lname") + ", " + map.get("fname"));
                vBdate.setText(map.get("bday"));
                vGender.setText(map.get("gender"));
                vEmail.setText(map.get("email"));
                vNationality.setText(map.get("nationality"));
                vAddress.setText(map.get("home"));
                vContact.setText(contact);
                vStatus.setText(status);
                vBloodtype.setText(bloodtype);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    public void getBloodCount() {
        Query query;

        query = supplyRef.child(bloodtype).child("count");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                supplyRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bloodcount = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    /*FOR ACTION BAR EVENTS*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionlogout:
                Logout();
                return true;
            case R.id.actionabout:
                about();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void Logout(){
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Do you really want to logout?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        backtologin();
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
    public void backtologin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void about(){
        Intent intent = new Intent(this, about.class);
        startActivity(intent);
    }
/////////////////////////////////////////////////////
}
