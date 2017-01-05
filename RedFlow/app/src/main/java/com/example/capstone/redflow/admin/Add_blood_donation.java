package com.example.capstone.redflow.admin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.redflow.LoginActivity;
import com.example.capstone.redflow.R;
import com.example.capstone.redflow.about;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class Add_blood_donation extends AppCompatActivity {

    private String sDateDonated;
    private String sSerial;
    private String blood_type;
    private String userID;
    private String fullname;

    private EditText vDateDonated;
    private EditText vSerial;
    private TextView vFullname;

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private int mYear, mMonth, mDay;
    private int bloodcount;

    private Firebase mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.add_blood_donation);

        blood_type = getIntent().getStringExtra("blood_type");
        userID = getIntent().getStringExtra("userID");
        fullname = getIntent().getStringExtra("fullname");

        vDateDonated = (EditText) findViewById(R.id.iedittext_date_donated);
        vSerial = (EditText) findViewById(R.id.eddittext_donation_serial);
        vFullname = (TextView) findViewById(R.id.textview_donors_name);

        vFullname.setText(fullname);

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");

        dateView = (TextView) findViewById(R.id.iedittext_date_donated);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "birthdate",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day

                    mYear = arg1;
                    mMonth = arg2;
                    mDay = arg3;

                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    public void add_donation_now(View view) {
        sSerial = vSerial.getText().toString();
        new AlertDialog.Builder(this)
                .setTitle("Please Recheck Serial")
                .setMessage("Is this serial correct (" + sSerial + ")?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Firebase blood = mRootRef.child("Blood").push();

                        getBloodCount();

                        if(mYear == 0) {
                            mYear = year;
                            mMonth = month;
                            mDay = day;
                        }

                        blood.child("bloodtype").setValue(blood_type);
                        blood.child("serial").setValue(sSerial.toUpperCase());
                        blood.child("userID").setValue(userID);
                        blood.child("donateDay").setValue(mDay);
                        blood.child("donateMonth").setValue(mMonth+1);
                        blood.child("donateYear").setValue(mYear);

                        Intent intent = new Intent(Add_blood_donation.this, blood_supply_info.class);
                        intent.putExtra("blood_type", blood_type);
                        Add_blood_donation.this.finish();
                        mRootRef.child("Supply").child(blood_type).child("count").setValue(bloodcount+1);
                        mRootRef.child("Supply").child(blood_type).child("recent").setValue(sSerial.toUpperCase());
                        Toast.makeText(Add_blood_donation.this, "Successfully added 1 "+ blood_type +" blood bag.", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        user_profile_admin.getInstance().finish();
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
    public void getBloodCount() {
        Query query;

        query = mRootRef.child("Supply").child(blood_type).child("count");
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
                        Toast.makeText(getApplicationContext(), "successfully logged out", Toast.LENGTH_SHORT).show();
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
