package com.example.capstone.redflow.user_activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Process;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class request extends AppCompatActivity {

    private Firebase mRootRef;
    private Firebase notify;
    private Firebase userRef;
    private Firebase supplyRef;
    private Firebase notifyRef;
    private Firebase historyRef;
    private Firebase notifRef;

    private ValueEventListener notifyListenerVE;
    private ValueEventListener userListenerVE;
    private ValueEventListener supplyListenerVE;

    private ChildEventListener userListenerCE;

    private Query query;
    private Query sQuery;
    private Query notifyquery;
    private Query quser;
    private Query userquery;
    private Query demandQuery;

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
    private String messageDB;
    private String message;
    private String notif;
    private String province;
    private String userID;
    private String user;
    private String iBloodtype;
    private String iLocation;

    private ToolBox tools;

    private List<String> devices;

    private int date;

    private double time;
    private double datetime;

    private long demandctr;

    private String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(com.example.capstone.redflow.R.layout.request);

        getWindow().setBackgroundDrawableResource(R.drawable.bg);

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
        progressDialog.setMessage("Requesting...");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                if(isInternetAvailable()){


                    bloodtype = vBloodtype.getSelectedItem().toString();
                    location = vLocation.getSelectedItem().toString();
                    sBagqty = vBagqty.getText().toString();

                    sQuery = supplyRef.child(bloodtype).child("count");
                    //First use of supplyListenerVE.
                    supplyListenerVE = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            bloodcount = dataSnapshot.getValue(Integer.class);
                            if(sBagqty.trim().equals("")) {
                                progressDialog.dismiss();
                                Toast toast = Toast.makeText(request.this, "Please enter quantity of blood bag needed.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 88);
                                toast.show();
                            }
                            else {
                                bagqty = Integer.parseInt(sBagqty);

                                historyRef = mRootRef.child("History").child(userID).push();
                                historyRef.child("content").setValue("Requested " + bagqty + " " + bloodtype + " blood bag.");
                                historyRef.child("date").setValue(date);
                                historyRef.child("time").setValue(time);
                                historyRef.child("datetime").setValue(datetime);

                                demandQuery = mRootRef.child("Demand").child(bloodtype);
                                demandQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        mRootRef.child("Demand").child(bloodtype).setValue(dataSnapshot.getValue(Long.class) + bagqty);

                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });


                                messageDB = "Someone is in need of " + bagqty + " bag(s) of blood type " + bloodtype + ".\nHelp us save this person's life.";
                                message = "Someone is in need of " + bagqty + " bag(s) of blood type " + bloodtype + ".\nHelp us save this person's life.\n\nDon't reply.\n\n";


                                if(bloodcount > bagqty) {

                                    sQuery.removeEventListener(supplyListenerVE);
                                    userquery.removeEventListener(userListenerVE);

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
                                            notify.child("userID").setValue(userID);
                                            notify.child("email").setValue(mail);
                                            mRootRef.child("Notify").child("count").setValue(priority+1);
                                            userquery.removeEventListener(userListenerVE);
                                            sQuery.removeEventListener(supplyListenerVE);
                                            sendSMSRequest();
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    };
                                    userquery.addValueEventListener(userListenerVE);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    };
                    sQuery.addValueEventListener(supplyListenerVE);
                }

            }
        }).start();
    }


    public void sendSMSRequest() {

        query = userRef.orderByChild("bloodtype").equalTo(bloodtype).limitToFirst(250);
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


                if (notif.equals("on") && map.get("sms").equals("on") && province.toUpperCase().equals(location) && !userID.equals(user)) {
                    int myDays = 1;

                    final Calendar c = Calendar.getInstance();
                    c.add(Calendar.DATE, myDays);  // number of days to add
                    int newDate =   (c.get(Calendar.YEAR) * 10000) +
                            ((c.get(Calendar.MONTH) + 1) * 100) +
                            (c.get(Calendar.DAY_OF_MONTH));

                    offsms.child("userID").setValue(user);
                    offsms.child("duedate").setValue(newDate);

                    new SendRequest(contact, message).execute();

                    notifRef = mRootRef.child("Notification").child(dataSnapshot.getKey()).push();
                    notifRef.child("content").setValue(messageDB);
                    notifRef.child("date").setValue(date);
                    notifRef.child("time").setValue(time);
                    notifRef.child("datetime").setValue(datetime);

                    //mRootRef.child("User").child(user).child("request").setValue("off");
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



    private void DeleteToken() {
        final String email = mail;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_DELETE_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            //Toast.makeText(home.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DeleteToken();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
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
                    return true;
                }
            } catch (IOException e) {
                Log.e("Network Checker", "Error checking com.example.capstone.redflow.internet connection", e);
            }
        }
        final Snackbar snackBar = Snackbar.make(findViewById(R.id.request), "Poor internet connection. To continue using RedFlow, please check your internet connection or turn on your wifi/data..", Snackbar.LENGTH_INDEFINITE);
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



    ///////////*FOR ACTION BAR EVENTS*/
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
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                if(isInternetAvailable()){
                                    SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.clear();
                                    editor.commit();
                                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                                    FirebaseAuth.getInstance().signOut();
                                    backtologin();
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
    public void backtologin(){

        new Thread(new Runnable() {

            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                DeleteToken();
            }

        }).start();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void about(){
        Intent intent = new Intent(this, about.class);
        startActivity(intent);
    }
/////////////////////////////////////////////////////
}
