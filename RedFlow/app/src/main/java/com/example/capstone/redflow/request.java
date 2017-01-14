package com.example.capstone.redflow;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.capstone.redflow.Firebasenotification.EndPoints;
import com.example.capstone.redflow.Firebasenotification.MyVolley;
import com.example.capstone.redflow.admin.announcement;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class request extends AppCompatActivity {

    private Firebase mRootRef;
    private Firebase notify;
    private Firebase userRef;
    private Firebase supplyRef;
    private Firebase notifyRef;

    private Query query;
    private Query sQuery;
    private Query notifyquery;
    private Query quser;
    private Query userquery;

    private ProgressDialog progressDialog;

    private Spinner vBloodtype;
    private Spinner vLocation;
    private EditText vBagqty;

    private String bloodtype;
    private String location;
    private String sBagqty;

    private int bagqty;
    private int bloodcount;
    private long priority;

    private String contact;
    private String message;
    private String message2;
    private String notif;
    private String province;
    private String userID;
    private String user;
    private String iBloodtype;
    private String iLocation;

    private ToolBox tools;

    private List<String> devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(com.example.capstone.redflow.R.layout.request);

        userID = getIntent().getStringExtra("userID");

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");
        userRef = mRootRef.child("User");
        supplyRef = mRootRef.child("Supply");
        notifyRef = mRootRef.child("Notify");

        tools = new ToolBox();

        vBloodtype = (Spinner) findViewById(R.id.spinnr_bloodtype);
        vLocation = (Spinner) findViewById(R.id.spinnr_location);
        vBagqty = (EditText) findViewById(R.id.edittext_bagqntty);

        notifyquery = notifyRef.child("count");
        notifyquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notifyRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        notifyquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                priority = dataSnapshot.getValue(Long.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        quser = userRef.child(userID);
        quser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        quser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);

                iBloodtype = map.get("bloodtype");
                iLocation = map.get("province");

                //Toast.makeText(request.this, "Bloodtype: " + iBloodtype + "\n Location: " + iLocation, Toast.LENGTH_LONG).show();
                vBloodtype.setSelection(tools.getIndex(vBloodtype, iBloodtype));
                vLocation.setSelection(tools.getIndex(vLocation, iLocation));

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        progressDialog = new ProgressDialog(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onSubmitButton(View view) {

        bloodtype = vBloodtype.getSelectedItem().toString();
        location = vLocation.getSelectedItem().toString();
        sBagqty = vBagqty.getText().toString();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Requesting..");
        progressDialog.show();

        sQuery = supplyRef.child(bloodtype).child("count");
        sQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                supplyRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        sQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bloodcount = dataSnapshot.getValue(Integer.class);
                if(sBagqty.trim().equals("")) {
                    Toast.makeText(request.this, "Please enter quantity of blood bag needed.", Toast.LENGTH_SHORT).show();
                }
                else {
                    bagqty = Integer.parseInt(sBagqty);

                    message = "Someone is in need of " + bagqty + " bag(s) of blood type " + bloodtype + ". Please help us save this person's life.";
                    message2 = message;
                    if(bloodcount > bagqty) {
                        //Toast.makeText(request.this, "Count: " + bloodcount, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(request.this, "There are available supply. Please visit any RedCross blood facility to get blood.", Toast.LENGTH_SHORT).show();
                        supplyRef.removeEventListener(this);
                        Intent intent = new Intent(request.this, proceed_to_RedCross.class);
                        intent.putExtra("bloodtype", bloodtype);
                        intent.putExtra("bloodcount", bloodcount);
                        startActivity(intent);
                        request.this.finish();
                    }
                    else {
                        userquery = userRef.child(userID).child("contact");
                        userquery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                userRef.removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                        userquery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                notify = mRootRef.child("Notify").child(bloodtype).child(dataSnapshot.getValue(String.class));
                                notify.child("priority").setValue(priority+1);
                                notify.child("qty").setValue(bagqty);
                                mRootRef.child("Notify").child("count").setValue(priority+1);
                                userRef.removeEventListener(this);
                                sendSMSRequest();
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }


    public void sendSMSRequest() {

        query = userRef.orderByChild("bloodtype").equalTo(bloodtype);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(bloodcount == 0) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(request.this, zero_supply_request.class);
                    intent.putExtra("bloodtype", bloodtype);
                    intent.putExtra("qtty", sBagqty);
                    userRef.removeEventListener(this);
                    startActivity(intent);
                    request.this.finish();
                }
                else {
                    Intent intent = new Intent(request.this, proceed_to_RedCross.class);
                    intent.putExtra("bloodtype", bloodtype);
                    intent.putExtra("bloodcount", bloodcount);
                    userRef.removeEventListener(this);
                    startActivity(intent);
                    request.this.finish();
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                Firebase offsms;

                offsms = mRootRef.child("OffSMS").push();

                contact = map.get("contact");
                notif = map.get("request");
                province = map.get("province");
                user = dataSnapshot.getKey();


                if (notif.equals("on") && province.equals(location) && !userID.equals(user)) {
                    int myDays = 1;

                    final Calendar c = Calendar.getInstance();
                    c.add(Calendar.DATE, myDays);  // number of days to add
                    int newDate =   (c.get(Calendar.YEAR) * 10000) +
                                    ((c.get(Calendar.MONTH) + 1) * 100) +
                                    (c.get(Calendar.DAY_OF_MONTH));

                    offsms.child("userID").setValue(user);
                    offsms.child("duedate").setValue(newDate);

                    new SendRequest(contact, message).execute();

                    //Toast.makeText(request.this, "Request sent.", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(request.this, "Hey, " + user, Toast.LENGTH_LONG).show();
                    //Toast.makeText(request.this, "Date is, " + newDate, Toast.LENGTH_LONG).show();

                    mRootRef.child("User").child(user).child("request").setValue("off");
                }


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
