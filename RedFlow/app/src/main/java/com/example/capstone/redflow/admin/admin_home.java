package com.example.capstone.redflow.admin;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.example.capstone.redflow.Firebasenotification.Send_Push_Notification;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class admin_home extends AppCompatActivity {
    private ToolBox tools;

    private Firebase mRootRef;
    private Firebase offsmsRef;
    private Query query;
    private Query supplyquery;
    private ValueEventListener supplylistener;
    private ChildEventListener listener;

    private int date;
    private int day;
    int i;

    private String btype;

    private String user;
    private String turf;

    private Calendar calendar;

    private String email;

    private ArrayList<String> bloodlist;
    private int ctr = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.admin_home);

        turf = getIntent().getStringExtra("turf");
        email = getIntent().getStringExtra("mail");

        tools = new ToolBox();

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");
        offsmsRef = mRootRef.child("OffSMS");

        date = tools.getCurrentDate();

        query = offsmsRef.orderByChild("duedate").startAt(0).endAt(date);

        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                String id;

                id = dataSnapshot.getKey();
                user = map.get("userID");

                mRootRef.child("User").child(user).child("request").setValue("on");

                mRootRef.child("OffSMS").child(id).removeValue();

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

        query.addChildEventListener(listener);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_WEEK);

        //Toast.makeText(this, "Day is " + day, Toast.LENGTH_SHORT).show();

        bloodlist = new ArrayList<>();

        bloodlist.add("A+");
        bloodlist.add("B+");
        bloodlist.add("O+");
        bloodlist.add("AB+");
        bloodlist.add("A-");
        bloodlist.add("B-");
        bloodlist.add("O-");
        bloodlist.add("AB-");

        //Toast.makeText(admin_home.this, String.valueOf(day), Toast.LENGTH_SHORT).show();
        if(day == 1) {
            for(i = 0; i < bloodlist.size(); i++) {
                //Toast.makeText(admin_home.this, btype, Toast.LENGTH_SHORT).show();
                supplyChecker(bloodlist.get(i));
            }

        }

    }

    public void supplyChecker(String blood) {

        final String bloodtype = blood;

        supplyquery = mRootRef.child("Supply").child(turf).child(bloodtype);
        supplylistener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Integer> map = dataSnapshot.getValue(Map.class);

                //Toast.makeText(admin_home.this, bloodtype + ": " + map.get("count"), Toast.LENGTH_SHORT).show();

                if(map.get("count") < 5) {
                    //Toast.makeText(admin_home.this, bloodtype + " is out of stock.", Toast.LENGTH_SHORT).show();
                    //DRI IBUTANG ANG PUSH NOTIF NGA FUNCTION CALL.
                    //sendFilteredPush();

                    final String title = "RedFlow: Good Day!";
                    final String message = "Red Cross is in need of blood type "+bloodtype+". We are hoping for your donation. Thank you.";
                    final String image = null;
                    final String loc = turf;
                    final String type = bloodtype;
                    //Toast.makeText(admin_home.this, String.valueOf(i), Toast.LENGTH_SHORT).show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SEND_CHECK_PUSH,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //Toast.makeText(admin_home.this, response, Toast.LENGTH_LONG).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    sendFilteredPush();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("title", title);
                            params.put("message", message);
                            params.put("bloodType", type);
                            params.put("email", "admin@redcross.ph");
                            params.put("loc", loc);

                            if (!TextUtils.isEmpty(image))
                                params.put("image", image);
                            return params;
                        }
                    };

                    MyVolley.getInstance(admin_home.this).addToRequestQueue(stringRequest);


                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        supplyquery.addValueEventListener(supplylistener);
        supplyquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                supplyquery.removeEventListener(supplylistener);
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

    public void statistics(View view) {
        Intent intent = new Intent(this,  statistics_home.class);
        intent.putExtra("turf", turf);
        startActivity(intent);
    }

    public void donors(View view) {
        Intent intent = new Intent(this, registered_users.class);
        startActivity(intent);
    }

    public void blood_supply_records(View view) {
        Intent intent = new Intent(this,  blood_supply_record.class);
        intent.putExtra("turf", turf);
        startActivity(intent);
    }


    public void search(View view) {
        Intent intent = new Intent(this, search_menu.class);
        intent.putExtra("turf", turf);
        startActivity(intent);
    }



    private void sendFilteredPush() {

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
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
            }
        }
        final Snackbar snackBar = Snackbar.make(findViewById(R.id.activity_admin_home), "Poor internet connection. To continue using RedFlow, please check your internet connection or turn on your wifi/data..", Snackbar.LENGTH_INDEFINITE);
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
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void about(){
        Intent intent = new Intent(this, about.class);
        startActivity(intent);
    }

    public void textall(View view) {
        Intent intent = new Intent(this, announcement.class);
        startActivity(intent);
    }
/////////////////////////////////////////////////////
}
