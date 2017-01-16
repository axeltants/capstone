package com.example.capstone.redflow.user_activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.capstone.redflow.R;
import com.example.capstone.redflow.common_activities.about;
import com.example.capstone.redflow.common_activities.LoginActivity;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

public class profile extends AppCompatActivity {

    private String userID;
    private String notif;

    private static profile prof;

    private Firebase mRootRef;

    TextView vCompleteName;
    TextView vBdate;
    TextView vGender;
    TextView vEmail;
    TextView vNationality;
    TextView vAddress;
    TextView vContact;
    TextView vBloodtype;

    private TextView switchStatus;
    private Switch mySwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.capstone.redflow.R.layout.profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prof = this;

        userID = getIntent().getStringExtra("userID");

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");

        vCompleteName = (TextView) findViewById(R.id.textview_CompleteName);
        vBdate = (TextView) findViewById(R.id.textview_age);
        vGender = (TextView) findViewById(R.id.textview_gender);
        vEmail = (TextView) findViewById(R.id.textview_email);
        vNationality = (TextView) findViewById(R.id.textview_nationality);
        vAddress = (TextView) findViewById(R.id.textview_address);
        vContact = (TextView) findViewById(R.id.textview_contact);
        vBloodtype = (TextView) findViewById(R.id.textview_bloodtype);

        switchStatus = (TextView) findViewById(R.id.smsnotiflabel);
        mySwitch = (Switch) findViewById(R.id.sms_switch);

        mRootRef.child("User").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                Map<String, Integer> map2 = dataSnapshot.getValue(Map.class);

                vCompleteName.setText(map.get("lname") + ", " + map.get("fname"));
                vBdate.setText(map2.get("birthmonth") + "/" + map2.get("birthday") + "/" + map2.get("birthyear"));
                vGender.setText(map.get("gender"));
                vEmail.setText(map.get("email"));
                vNationality.setText(map.get("nationality"));
                vAddress.setText(map.get("home"));
                vContact.setText(map.get("contact"));
                vBloodtype.setText(map.get("bloodtype"));
                notif = map.get("sms");

                if(notif.equals("on")) {
                    mySwitch.setChecked(true);
                    switchStatus.setText("SMS notification: ON");
                }
                else {
                    mySwitch.setChecked(false);
                    switchStatus.setText("SMS notification: OFF");
                }
                //attach a listener to check for changes in state
                mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(isChecked){
                            switchStatus.setText("SMS notification: ON");
                            mRootRef.child("User").child(userID).child("sms").setValue("on");
                            mRootRef.child("User").child(userID).child("request").setValue("on");
                        }else{
                            switchStatus.setText("SMS notification: OFF");
                            mRootRef.child("User").child(userID).child("sms").setValue("off");
                            mRootRef.child("User").child(userID).child("request").setValue("off");
                        }
                    }
                });
                mRootRef.child("User").child(userID).removeEventListener(this);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static profile getinstance(){return prof;}

    public void switchhh(View view) {
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
