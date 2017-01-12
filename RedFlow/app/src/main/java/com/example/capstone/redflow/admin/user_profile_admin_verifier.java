package com.example.capstone.redflow.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.redflow.LoginActivity;
import com.example.capstone.redflow.R;
import com.example.capstone.redflow.about;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

public class user_profile_admin_verifier extends AppCompatActivity {

    String userID;
    String bldtyp;
    int option = 0;

    Firebase mRootRef;

    TextView vCompleteName;
    TextView vBdate;
    TextView vGender;
    TextView vEmail;
    TextView vNationality;
    TextView vAddress;
    TextView vContact;
    TextView vStatus;
    Spinner vBloodtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.user_profile_admin_verifier);



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
        vBloodtype = (Spinner) findViewById(R.id.textview_bloodtype);


        mRootRef.child("User").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                Map<String, Integer> map2 = dataSnapshot.getValue(Map.class);

                bldtyp = (String)map.get("bloodtype");

                if(bldtyp.equals("A+")){
                    option = 1;
                }else if(bldtyp.equals("A-")){
                    option = 2;
                }else if(bldtyp.equals("B+")){
                    option = 3;
                }else if(bldtyp.equals("B-")){
                    option = 4;
                }else if(bldtyp.equals("O+")){
                    option = 5;
                }else if(bldtyp.equals("O-")){
                    option = 6;
                }else if(bldtyp.equals("AB+")){
                    option =7;
                }else if(bldtyp.equals("AB-")){
                    option = 8;
                }

                vCompleteName.setText(map.get("lname") + ", " + map.get("fname"));
                vBdate.setText(map2.get("birthmonth") + "/" + map2.get("birthday") + "/" + map2.get("birthyear"));
                vGender.setText(map.get("gender"));
                vEmail.setText(map.get("email"));
                vNationality.setText(map.get("nationality"));
                vAddress.setText(map.get("home"));
                vContact.setText(map.get("contact"));
                vStatus.setText(map.get("status"));
                vBloodtype.setSelection(option);
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

    public void verifyuser(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Verify User")
                .setMessage("Are you sure you want to verify this user?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mRootRef.child("User").child(userID).child("bloodtype").setValue(vBloodtype.getSelectedItem().toString());
                        mRootRef.child("User").child(userID).child("status").setValue("Verified");
                        Intent i = new Intent(user_profile_admin_verifier.this, user_profile_admin.class);
                        i.putExtra("userID", userID);
                        search_result.getInstance().finish();
                        startActivity(i);
                        finish();
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
