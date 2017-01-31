package com.example.capstone.redflow.admin;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.capstone.redflow.Firebasenotification.EndPoints;
import com.example.capstone.redflow.Firebasenotification.MyVolley;
import com.example.capstone.redflow.R;
import com.example.capstone.redflow.SendRequest;
import com.example.capstone.redflow.SplashScreen.Splashscreen;
import com.example.capstone.redflow.ToolBox;
import com.example.capstone.redflow.common_activities.LoginActivity;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class announcement extends AppCompatActivity {

    private EditText editText;
    private TextView textView;
    private Spinner vLocation;

    private List<String> devices;

    private ProgressDialog progressDialog;

    private Firebase mRootRef;
    private Firebase notifRef;
    private Query query;
    private ChildEventListener listener;

    private String message;
    private String messageDB;
    private String sLocation;

    private int date;

    private double time;
    private  double datetime;

    private Calendar c;

    private ToolBox tools;
    Thread push;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.announcement);

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");

        editText = (EditText) findViewById(R.id.edittext_message);
        textView = (TextView) findViewById(R.id.textView_count);
        vLocation = (Spinner) findViewById(R.id.spinnr_province);

        tools = new ToolBox();

        c = Calendar.getInstance();

        date = tools.getCurrentDate();
        time = tools.getCurrentTime();
        datetime = tools.getDateTime();


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {


                textView.setText(String.valueOf(s.length())+"/300");

                if (String.valueOf(s.length()).equals("300")){
                    textView.setTextColor(Color.RED);
                }
            }
        });

    }

    private void sendMultiplePush() {
        final String title = "RedFlow: Announcement!";
        final String message = editText.getText().toString();
        final String image = null;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SEND_MULTIPLE_PUSH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        editText.setText("");
                        progressDialog.dismiss();
                        Toast toast = Toast.makeText(announcement.this, "Message sent.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 110);
                        toast.show();

                        //Toast.makeText(announcement.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sendMultiplePush();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("message", message);

                if (!TextUtils.isEmpty(image))
                    params.put("image", image);
                return params;
            }
        };

        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void announce(View view) {

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Sending...");
            progressDialog.show();

            sLocation = vLocation.getSelectedItem().toString();
            messageDB = editText.getText().toString();
            message = messageDB + "\n\nDon't reply.\n\n";

            if(messageDB.trim().equals("")) {
                progressDialog.dismiss();
                Toast toast = Toast.makeText(announcement.this, "Message is empty.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 88);
                toast.show();
            }
            else {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                        if(isInternetAvailable()){

                            query = mRootRef.child("User").orderByChild("sms").equalTo("on");
                            listener = new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    Map<String, String> map = dataSnapshot.getValue(Map.class);

                                    if(sLocation.equals(map.get("province")) || sLocation.equals("ALL")) {
                                        notifRef = mRootRef.child("Notification").child(dataSnapshot.getKey()).push();
                                        notifRef.child("content").setValue(messageDB);
                                        notifRef.child("date").setValue(date);
                                        notifRef.child("time").setValue(time);
                                        notifRef.child("datetime").setValue(datetime);

                                        new SendRequest(map.get("contact"), message).execute();
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
                                    query.removeEventListener(listener);
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                            query.addChildEventListener(listener);
                            push = new Thread() {
                                @Override
                                public void run() {
                                    sendMultiplePush();
                                }
                            };
                            push.start();

                        }
                    }

                }).start();
            }
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
        final Snackbar snackBar = Snackbar.make(findViewById(R.id.announcement), "Poor internet connection. To continue using RedFlow, please check your internet connection or turn on your wifi/data..", Snackbar.LENGTH_INDEFINITE);
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

}
