package com.example.capstone.redflow.user_activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.capstone.redflow.R;
import com.example.capstone.redflow.SendRequest;
import com.example.capstone.redflow.ToolBox;
import com.example.capstone.redflow.common_activities.about;
import com.example.capstone.redflow.common_activities.LoginActivity;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class request extends AppCompatActivity {

    private Firebase mRootRef;
    private Firebase notify;
    private Firebase userRef;
    private Firebase supplyRef;
    private Firebase notifyRef;
    private Firebase historyRef;

    private ValueEventListener notifyListenerVE;
    private ValueEventListener userListenerVE;
    private ValueEventListener supplyListenerVE;

    private ChildEventListener userListenerCE;

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

    private String mail;

    private int date;
    private int time;
    private double datetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(com.example.capstone.redflow.R.layout.request);

        userID = getIntent().getStringExtra("userID");
        mail = getIntent().getStringExtra("mail");


        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");
        userRef = mRootRef.child("User");
        supplyRef = mRootRef.child("Supply");
        notifyRef = mRootRef.child("Notify");

        tools = new ToolBox();

        date = tools.getCurrentDate();
        time = tools.getCurrentTime();
        datetime = tools.getDateTime();

        vBloodtype = (Spinner) findViewById(R.id.spinnr_bloodtype);
        vLocation = (Spinner) findViewById(R.id.spinnr_location);
        vBagqty = (EditText) findViewById(R.id.edittext_bagqntty);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

/**************************************************************************************************/

        notifyquery = notifyRef.child("count");
        //First use of notifyListenerVE.
        notifyListenerVE = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                priority = dataSnapshot.getValue(Long.class);
                notifyquery.removeEventListener(notifyListenerVE);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };

        notifyquery.addValueEventListener(notifyListenerVE);

/**************************************************************************************************/

        quser = userRef.child(userID);
        //First use of userListenerVE.
        userListenerVE = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);

                iBloodtype = map.get("bloodtype");
                iLocation = map.get("province");

                vBloodtype.setSelection(tools.getIndex(vBloodtype, iBloodtype));
                vLocation.setSelection(tools.getIndex(vLocation, iLocation));

                progressDialog.dismiss();

                quser.removeEventListener(userListenerVE);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        quser.addValueEventListener(userListenerVE);

/**************************************************************************************************/

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

        progressDialog.setMessage("Requesting...");
        progressDialog.show();

        sQuery = supplyRef.child(bloodtype).child("count");
        //First use of supplyListenerVE.
        supplyListenerVE = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bloodcount = dataSnapshot.getValue(Integer.class);
                if(sBagqty.trim().equals("")) {
                    Toast.makeText(request.this, "Please enter quantity of blood bag needed.", Toast.LENGTH_SHORT).show();
                }
                else {
                    bagqty = Integer.parseInt(sBagqty);

                    historyRef = mRootRef.child("History").child(userID).push();
                    historyRef.child("content").setValue("Requested " + bagqty + " " + bloodtype + " blood bag.");
                    historyRef.child("date").setValue(date);
                    historyRef.child("time").setValue(time);
                    historyRef.child("datetime").setValue(datetime);

                    message = "Someone is in need of " + bagqty + " bag(s) of blood type " + bloodtype + ". Please help us save this person's life.";
                    message2 = message;

                    if(bloodcount > bagqty) {
                        sQuery.removeEventListener(supplyListenerVE);
                        Intent intent = new Intent(request.this, proceed_to_RedCross.class);
                        intent.putExtra("bloodtype", bloodtype);
                        intent.putExtra("bloodcount", bloodcount);
                        startActivity(intent);
                        request.this.finish();
                    }
                    else {
                        userquery = userRef.child(userID).child("contact");
                        //Second use of userListenerVE;
                        userListenerVE = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                notify = mRootRef.child("Notify").child(bloodtype).child(dataSnapshot.getValue(String.class));
                                notify.child("priority").setValue(priority+1);
                                notify.child("qty").setValue(bagqty);
                                mRootRef.child("Notify").child("count").setValue(priority+1);
                                userquery.removeEventListener(userListenerVE);
                                sQuery.removeEventListener(supplyListenerVE);
                                sendSMSRequest();
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        };
                        userquery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                userRef.removeEventListener(userListenerVE);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                        userquery.addValueEventListener(userListenerVE);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        sQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                supplyRef.removeEventListener(supplyListenerVE);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        sQuery.addValueEventListener(supplyListenerVE);


    }


    public void sendSMSRequest() {

        query = userRef.orderByChild("bloodtype").equalTo(bloodtype);
        //First use of userListenerCE;
        userListenerCE = new ChildEventListener() {
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
        };
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(bloodcount == 0) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(request.this, zero_supply_request.class);
                    intent.putExtra("bloodtype", bloodtype);
                    intent.putExtra("qtty", sBagqty);
                    intent.putExtra("mail", mail);

                    query.removeEventListener(userListenerCE);

                    startActivity(intent);
                    request.this.finish();
                }
                else {
                    Intent intent = new Intent(request.this, proceed_to_RedCross.class);
                    intent.putExtra("bloodtype", bloodtype);
                    intent.putExtra("qtty", bagqty);
                    intent.putExtra("bloodcount", bloodcount);
                    intent.putExtra("mail", mail);

                    query.removeEventListener(userListenerCE);

                    startActivity(intent);
                    request.this.finish();
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        query.addChildEventListener(userListenerCE);
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
