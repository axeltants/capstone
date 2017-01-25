package com.example.capstone.redflow.admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.capstone.redflow.Firebasenotification.EndPoints;
import com.example.capstone.redflow.Firebasenotification.MyVolley;
import com.example.capstone.redflow.Firebasenotification.Send_Push_Notification;
import com.example.capstone.redflow.ToolBox;
import com.example.capstone.redflow.common_activities.LoginActivity;
import com.example.capstone.redflow.R;
import com.example.capstone.redflow.SendRequest;
import com.example.capstone.redflow.common_activities.about;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class search_blood_profile extends AppCompatActivity {

    private String serial_number;
    private String userID;
    private String contact;
    private String message;
    private String messageDB;
    private String status;
    private String bloodtype;
    private String bloodID;

    private int bloodcount;
    private int date;

    private double time;
    private double datetime;

    private Firebase mRootRef;
    private Firebase userRef;
    private Firebase bloodRef;
    private Firebase supplyRef;
    private Firebase notifRef;
    private Firebase historyRef;

    private ChildEventListener bloodListenerCE;

    private Query query;

    private TextView vCompleteName;
    private TextView vBdate;
    private TextView vGender;
    private TextView vEmail;
    private TextView vNationality;
    private TextView vAddress;
    private TextView vContact;
    private TextView vStatus;
    private TextView vBloodtype;

    private String mail;

    private ProgressDialog progressDialog;

    private ToolBox tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);


        serial_number = getIntent().getStringExtra("serial_number");

        tools = new ToolBox();

        date = tools.getCurrentDate();
        time = tools.getCurrentTime();
        datetime = tools.getDateTime();

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");
        userRef = mRootRef.child("User");
        bloodRef = mRootRef.child("Blood");
        supplyRef = mRootRef.child("Supply");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Searching...");
        progressDialog.show();

        query = bloodRef.orderByChild("serial").equalTo(serial_number);
        bloodListenerCE = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Map<String, String> map = dataSnapshot.getValue(Map.class);

                setContentView(R.layout.search_blood_result);

                vCompleteName = (TextView) findViewById(R.id.textview_CompleteName);
                vBdate = (TextView) findViewById(R.id.textview_age);
                vGender = (TextView) findViewById(R.id.textview_gender);
                vEmail = (TextView) findViewById(R.id.textview_email);
                vNationality = (TextView) findViewById(R.id.textview_nationality);
                vAddress = (TextView) findViewById(R.id.textview_address);
                vContact = (TextView) findViewById(R.id.textview_contact);
                vStatus = (TextView) findViewById(R.id.textview_status);
                vBloodtype = (TextView) findViewById(R.id.textview_bloodtype);

                userID = map.get("userID");
                bloodtype = map.get("bloodtype");
                bloodID = dataSnapshot.getKey();
                getBloodCount();
                setProfile(userID, bloodtype);
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
        };
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() == 0) {
                    setContentView(R.layout.empty_searchblood_result);
                }
                progressDialog.dismiss();
                query.removeEventListener(bloodListenerCE);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        query.addChildEventListener(bloodListenerCE);

    }

    public void user_blood(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Give the blood to patient")
                .setMessage("Are you sure you want to use the blood?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(status.equals("Unknown")) {
                            Toast.makeText(search_blood_profile.this, bloodtype + " blood supply reduced by 1 bag.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            messageDB = "Your blood has just been donated.\nThank you for saving a life.";
                            message = "Your blood has just been donated.\nThank you for saving a life.\n\nDon't reply.\n\n";
                            new SendRequest(contact, message).execute();

                            notifRef = mRootRef.child("Notification").child(userID).push();
                            notifRef.child("content").setValue(messageDB);
                            notifRef.child("date").setValue(date);
                            notifRef.child("time").setValue(time);
                            notifRef.child("datetime").setValue(datetime);

                            historyRef = mRootRef.child("History").child(userID).push();
                            historyRef.child("content").setValue("Someone has received your donated blood.");
                            historyRef.child("date").setValue(date);
                            historyRef.child("time").setValue(time);
                            historyRef.child("datetime").setValue(datetime);

                            sendSinglePush();
                        }
                        mRootRef.child("Supply").child(bloodtype).child("count").setValue(bloodcount-1);
                        mRootRef.child("Blood").child(bloodID).removeValue();

                        Intent intent = new Intent(search_blood_profile.this, blood_supply_info.class);
                        intent.putExtra("blood_type", bloodtype);
                        startActivity(intent);
                        search_blood_profile.this.finish();
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
        final Query query;

        query = userRef.child(userID);

        query.addValueEventListener(new ValueEventListener() {
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

                mail = map.get("email");

                query.removeEventListener(this);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    public void getBloodCount() {
        final Query query;

        query = supplyRef.child(bloodtype).child("count");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bloodcount = dataSnapshot.getValue(Integer.class);
                query.removeEventListener(this);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    private void sendSinglePush() {
        final String title = "RedFlow: Thanks!";
        final String message = "Your blood has just been donated. Thank you for saving a life.";
        final String image = null;
        final String email = mail;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SEND_SINGLE_PUSH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(search_blood_profile.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("message", message);

                if (!TextUtils.isEmpty(image))
                    params.put("image", image);

                params.put("email", email);
                return params;
            }
        };

        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
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
