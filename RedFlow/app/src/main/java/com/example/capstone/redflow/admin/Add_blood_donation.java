package com.example.capstone.redflow.admin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.capstone.redflow.Firebasenotification.EndPoints;
import com.example.capstone.redflow.Firebasenotification.MyVolley;
import com.example.capstone.redflow.SendRequest;
import com.example.capstone.redflow.common_activities.LoginActivity;
import com.example.capstone.redflow.R;
import com.example.capstone.redflow.ToolBox;
import com.example.capstone.redflow.common_activities.about;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Add_blood_donation extends AppCompatActivity {

    private String sDateDonated;
    private String sSerial;
    private String blood_type;
    private String userID;
    private String fullname;
    private String contact;
    private String message;
    private String messageDB;

    private EditText vDateDonated;
    private EditText vSerial;
    private TextView vFullname;

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private int mYear, mMonth, mDay;
    private int bloodcount;
    private int date;

    private double time;
    private double datetime;

    private ProgressDialog progressDialog;

    private String mail;
    private String email;

    private Firebase mRootRef;
    private Firebase historyRef;
    private Firebase notifRef;
    private Firebase offsms;

    private Query qnotify;

    private ChildEventListener notifyListenerCE;

    private ToolBox tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_blood_donation);

        blood_type = getIntent().getStringExtra("blood_type");
        userID = getIntent().getStringExtra("userID");
        fullname = getIntent().getStringExtra("fullname");
        mail = getIntent().getStringExtra("mail");

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

        tools = new ToolBox();

        time = tools.getCurrentTime();
        datetime = tools.getDateTime();

        showDate(year, month+1, day);
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
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

                        date = (mYear * 10000) + ((mMonth + 1) * 100) + (mDay);

                        blood.child("bloodtype").setValue(blood_type);
                        blood.child("serial").setValue(sSerial.toUpperCase());
                        blood.child("userID").setValue(userID);
                        blood.child("date").setValue(date);

                        Intent intent = new Intent(Add_blood_donation.this, blood_supply_info.class);
                        intent.putExtra("blood_type", blood_type);

                        mRootRef.child("Supply").child(blood_type).child("count").setValue(bloodcount+1);
                        mRootRef.child("Supply").child(blood_type).child("recent").setValue(sSerial.toUpperCase());

                        final Calendar c = Calendar.getInstance();
                        c.add(Calendar.DATE, 32);  // number of days to add
                        int newDate =   (c.get(Calendar.YEAR) * 10000) +
                                ((c.get(Calendar.MONTH) + 1) * 100) +
                                (c.get(Calendar.DAY_OF_MONTH));

                        offsms = mRootRef.child("OffSMS").push();
                        offsms.child("userID").setValue(userID);
                        offsms.child("duedate").setValue(newDate);

                        historyRef = mRootRef.child("History").child(userID).push();
                        historyRef.child("content").setValue("Donated blood.");
                        historyRef.child("date").setValue(date);
                        historyRef.child("time").setValue(time);
                        historyRef.child("datetime").setValue(datetime);

                        qnotify = mRootRef.child("Notify").child(blood_type).orderByChild("priority").limitToFirst(1);
                        notifyListenerCE = new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                Map<String, String> map = dataSnapshot.getValue(Map.class);

                                contact = dataSnapshot.getKey();
                                messageDB = "Someone donated " + blood_type + " blood bag.\nNote: This is first come first serve.";
                                message = "Someone donated " + blood_type + " blood bag.\nNote: This is first come first serve.\n\nDon't reply.\n\n";

                                mRootRef.child("Notify").child(blood_type).child(contact).removeValue();

                                new SendRequest(contact, message).execute();

                                notifRef = mRootRef.child("Notification").child(map.get("userID")).push();
                                notifRef.child("content").setValue(messageDB);
                                notifRef.child("date").setValue(date);
                                notifRef.child("time").setValue(time);
                                notifRef.child("datetime").setValue(datetime);

                                email = map.get("email");
                                sendSinglePush();
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
                        qnotify.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                qnotify.removeEventListener(notifyListenerCE);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                        qnotify.addChildEventListener(notifyListenerCE);

                        startActivity(intent);
                        Add_blood_donation.this.finish();
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

    private void sendSinglePush() {
        final String title = "RedFlow: Good Day!";
        final String message2 = message;
        final String image = null;
        final String email2 = email;

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
                params.put("message", message2);

                if (!TextUtils.isEmpty(image))
                    params.put("image", image);

                params.put("email", email2);
                return params;
            }
        };

        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }


    ///////////////////*FOR ACTION BAR EVENTS*/
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
