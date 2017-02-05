package com.example.capstone.redflow.admin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class blood_supply_info extends AppCompatActivity {

    private String blood_type;
    private String email;

    private Firebase mRootRef;
    private Firebase notifyRef;
    private Firebase notifRef;

    private ChildEventListener notifyListenerCE;

    private Query query;
    private Query qnotify;
    private Query queryBlood;

    private TextView bloodtype;
    private TextView bag_quantity;
    private TextView recentlyAdded;
    private TextView recentlyDonated;
    private EditText vBag_serial;

    private String turf;
    private String message;
    private String messageDB;
    private String contact;
    private String sBag_serial;
    private int count;
    private long duplicate;

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    private int mDay, mMonth, mYear, date;

    private double time;
    private double datetime;

    private ProgressDialog progressDialog;

    private ToolBox tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.blood_supply_info);
        getWindow().setBackgroundDrawableResource(R.drawable.bg);

        turf = getIntent().getStringExtra("turf");
        blood_type = getIntent().getStringExtra("blood_type");

        tools = new ToolBox();

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");
        notifyRef = mRootRef.child("Notify");

        bloodtype = (TextView) findViewById(R.id.bloodtype);
        bag_quantity = (TextView) findViewById(R.id.bag_quantity);
        recentlyAdded = (TextView) findViewById(R.id.recently_added);
        recentlyDonated = (TextView) findViewById(R.id.recently_donated);
        vBag_serial = (EditText) findViewById(R.id.bag_serial);
        vBag_serial.setRawInputType(Configuration.KEYBOARD_12KEY);

        dateView = (TextView) findViewById(R.id.edittext_date_donated);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        time = tools.getCurrentTime();
        datetime = tools.getDateTime();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        bloodtype.setText(blood_type);
        query = mRootRef.child("Supply").child(turf).child(blood_type);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Integer> map = dataSnapshot.getValue(Map.class);
                Map<String, String> map2 = dataSnapshot.getValue(Map.class);

                count = map.get("count");
                bag_quantity.setText("Available: " + count);
                recentlyAdded.setText(map2.get("added"));
                recentlyDonated.setText(map2.get("removed"));
                progressDialog.dismiss();
                query.removeEventListener(this);
                
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        vBag_serial.addTextChangedListener(new TextWatcher() {

            boolean hyphenExists;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() >= 4 && s.length() < 10 ) {
                    hyphenExists = true;
                    Log.d("TAG", "true" );
                }else if(s.length() >= 11 ){
                    hyphenExists = true;
                } else {
                    hyphenExists = false;
                    Log.d("TAG", "false" );
                }

                Log.d("TAG", "beforeTextChanged " + s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.d("TAG", "onTextChanged " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4 || s.length() == 11) {
                    if (!hyphenExists)
                        s.append('-');
                }
                Log.d("TAG", "afterTextChanged " + s.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

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

    public void submit_bag(View view) {
        sBag_serial = vBag_serial.getText().toString();
        duplicate = 0;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        if(sBag_serial.trim().equals("") ) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(blood_supply_info.this, "Please input the serial number of the bag.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 88);
                    toast.show();
                }
            });
            progressDialog.dismiss();
        }else{

            new Thread(new Runnable() {

                @Override
                public void run() {
                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                    if(isInternetAvailable()){
                        queryBlood = mRootRef.child("Blood").orderByChild("serial").equalTo(sBag_serial.toUpperCase());
                        queryBlood.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                duplicate = dataSnapshot.getChildrenCount();
                                queryBlood.removeEventListener(this);
                                if(duplicate == 0) {
                                    progressDialog.dismiss();

                                    Firebase blood = mRootRef.child("Blood").child(turf).push();

                                    if (sBag_serial.trim().equals("")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast toast = Toast.makeText(blood_supply_info.this, "Please enter a serial number.", Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.TOP, 0, 88);
                                                toast.show();
                                            }
                                        });
                                    } else if (tools.isSerialValid(sBag_serial) == 0) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast toast = Toast.makeText(blood_supply_info.this, "Invalid serial number.", Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.TOP, 0, 88);
                                                toast.show();
                                            }
                                        });
                                    } else {
                                        if (mYear == 0) {
                                            mYear = year;
                                            mMonth = month;
                                            mDay = day;
                                        }

                                        date = (mYear * 10000) + ((mMonth + 1) * 100) + (mDay);

                                        blood.child("date").setValue(date);
                                        blood.child("bloodtype").setValue(blood_type);
                                        blood.child("serial").setValue(sBag_serial.toUpperCase());
                                        blood.child("userID").setValue("-K_2nAZ1ynR9ZF15HvVw");

                                        mRootRef.child("Supply").child(turf).child(blood_type).child("count").setValue(count + 1);
                                        mRootRef.child("Supply").child(turf).child(blood_type).child("added").setValue(sBag_serial.toUpperCase());

                                        qnotify = notifyRef.child(turf).child(blood_type).orderByChild("datetime").limitToFirst(1);
                                        notifyListenerCE = new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                Map<String, String> map = dataSnapshot.getValue(Map.class);

                                                contact = dataSnapshot.getKey();
                                                messageDB = "Someone donated " + blood_type + " blood bag.\nNote: This is first come first serve.";
                                                message = "Someone donated " + blood_type + " blood bag.\nNote: This is first come first serve.\n\nDon't reply.\n\n";

                                                mRootRef.child("Notify").child(turf).child(blood_type).child(contact).removeValue();

                                                new SendRequest(contact, message).execute();

                                                notifRef = mRootRef.child("Notification").child(map.get("userID")).push();
                                                notifRef.child("content").setValue(messageDB);
                                                notifRef.child("date").setValue(date);
                                                notifRef.child("time").setValue(time);
                                                notifRef.child("datetime").setValue(datetime);
                                                mRootRef.child("Unread").child(map.get("userID")).setValue("on");

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
                                                finish();
                                                Intent intent = new Intent(blood_supply_info.this, blood_supply_info.class);
                                                intent.putExtra("blood_type", blood_type);
                                                intent.putExtra("turf", turf);
                                                qnotify.removeEventListener(notifyListenerCE);
                                                startActivity(intent);
                                                blood_supply_info.this.finish();
                                            }

                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {

                                            }
                                        });
                                        qnotify.addChildEventListener(notifyListenerCE);
                                    }
                                }
                                else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            Toast toast = Toast.makeText(blood_supply_info.this, "Serial number already exists.", Toast.LENGTH_SHORT);
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
                    Log.d("Network Checker", "Successfully connected to com.example.capstone.redflow.internet");
                    progressDialog.dismiss();
                    return true;
                }
            } catch (IOException e) {
                Log.e("Network Checker", "Error checking com.example.capstone.redflow.internet connection", e);
                progressDialog.dismiss();
            }
        }
        final Snackbar snackBar = Snackbar.make(findViewById(R.id.activity_blood_supply_info), "Poor internet connection. To continue using RedFlow, please check your internet connection or turn on your wifi/data..", Snackbar.LENGTH_INDEFINITE);
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
                    progressDialog.dismiss();
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
