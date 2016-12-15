package com.example.capstone.redflow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class profile extends AppCompatActivity {

    String userID;

    private static profile prof;

    private Firebase mRootRef;

    TextView vCompleteName;
    TextView vBdate;
    TextView vGender;
    TextView vEmail;
    TextView vNationality;
    TextView vAddress;
    TextView vContact;
    TextView vStatus;
    TextView vBloodtype;

    /*String sCompleteName;
    String sBdate;
    String sGender;
    String sEmail;
    String sNationality;
    String sAddress;
    String sContact;
    String sStatus;
    String sBloodtype;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.capstone.redflow.R.layout.profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prof = this;

        userID = getIntent().getStringExtra("userID");

        //Toast.makeText(this, "Welcome " + userID, Toast.LENGTH_SHORT).show();

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

        /*sCompleteName = vCompleteName.getText().toString();
        sBdate = vBdate.getText().toString();
        sGender = vGender.getText().toString();
        sEmail = vEmail.getText().toString();
        sNationality = vNationality.getText().toString();
        sAddress = vAddress.getText().toString();
        sContact = vContact.getText().toString();
        sStatus = vStatus.getText().toString();
        sBloodtype = vBloodtype.getText().toString();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static profile getinstance(){return prof;}
}
