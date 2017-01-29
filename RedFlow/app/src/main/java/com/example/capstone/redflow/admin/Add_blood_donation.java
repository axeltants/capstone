package com.example.capstone.redflow.admin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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

    private long duplicate;

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
    private Query queryBlood;

    private ChildEventListener notifyListenerCE;

    private ToolBox tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_blood_donation);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

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
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        sSerial = vSerial.getText().toString();

        duplicate = 0;

        if(sSerial.trim().equals("") ) {
            progressDialog.dismiss();
            Toast toast = Toast.makeText(this, "Please enter a serial number.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 88);
            toast.show();
        }else if(tools.isSerialValid(sSerial) == 0){
            progressDialog.dismiss();
            Toast toast = Toast.makeText(this, "Invalid serial number.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 88);
            toast.show();
        }else{

            new AlertDialog.Builder(this)
                    .setTitle("Please Recheck Serial")
                    .setMessage("Is this serial correct (" + sSerial + ")?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                                    if(isInternetAvailable()){

                                        queryBlood = mRootRef.child("Blood").orderByChild("serial").equalTo(sSerial.toUpperCase());
                                        queryBlood.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                duplicate = dataSnapshot.getChildrenCount();
                                                if(duplicate == 0) {
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
                                                    mRootRef.child("Supply").child(blood_type).child("added").setValue(sSerial.toUpperCase());

                                                    final Calendar c = Calendar.getInstance();
                                                    c.add(Calendar.DATE, 32);  // number of days to add
                                                    int newDate =   (c.get(Calendar.YEAR) * 10000) +
                                                            ((c.get(Calendar.MONTH) + 1) * 100) +
                                                            (c.get(Calendar.DAY_OF_MONTH));

                                                    offsms = mRootRef.child("OffSMS").push();
                                                    offsms.child("userID").setValue(userID);
                                                    offsms.child("duedate").setValue(newDate);

                                                    mRootRef.child("User").child(userID).child("request").setValue("off");

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
                                                            progressDialog.dismiss();
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
                                                else {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast toast = Toast.makeText(Add_blood_donation.this, "Serial number already exists.", Toast.LENGTH_SHORT);
                                                            toast.setGravity(Gravity.TOP, 0, 88);
                                                            toast.show();
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {

                                            }
                                        });
                                    }
                                }

                            }).start();
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
                        sendSinglePush();
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

    public  boolean isInternetAvailable(){
        if(test()){
            try {
                HttpURLConnection urlConnection = (HttpURLConnection)
                        (new URL("https://clients3.google.com/generate_204")
                                .openConnection());
                urlConnection.setRequestProperty("User-Agent", "Android");
                urlConnection.setRequestProperty("Connection", "close");
                urlConnection.setConnectTimeout(1500);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 204 &&
                        urlConnection.getContentLength() == 0) {
                    Log.d("Network Checker", "Successfully connected to internet");
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                            progressDialog.dismiss();
                        }

                    }).start();
                    return true;
                }
            } catch (IOException e) {
                Log.e("Network Checker", "Error checking com.example.capstone.redflow.internet connection", e);
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                                progressDialog.dismiss();
                    }

                }).start();
            }
        }
        final Snackbar snackBar = Snackbar.make(findViewById(R.id.activity_add_blood_donation), "Poor internet connection. To continue using RedFlow, please check your internet connection or turn on your wifi/data..", Snackbar.LENGTH_INDEFINITE);
        View v = snackBar.getView();
        TextView textView = (TextView) v.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)v.getLayoutParams();
        params.gravity = Gravity.CENTER;
        v.setLayoutParams(params);
        snackBar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
            }
        });
        snackBar.show();
        return false;
    }

    public boolean test(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private BroadcastReceiver networkStateReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                    isInternetAvailable();
                }
            }).start();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        unregisterReceiver(networkStateReceiver);
        super.onPause();
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
