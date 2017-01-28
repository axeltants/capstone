package com.example.capstone.redflow.admin;

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
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.capstone.redflow.common_activities.LoginActivity;
import com.example.capstone.redflow.R;
import com.example.capstone.redflow.common_activities.about;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.user_profile_admin_verifier);

        userID = getIntent().getStringExtra("userID");

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

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

                progressDialog.dismiss();
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
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                                if(isInternetAvailable()){
                                    mRootRef.child("User").child(userID).child("bloodtype").setValue(vBloodtype.getSelectedItem().toString());
                                    mRootRef.child("User").child(userID).child("status").setValue("Verified");
                                    Intent i = new Intent(user_profile_admin_verifier.this, user_profile_admin.class);
                                    i.putExtra("userID", userID);
                                    search_result.getInstance().finish();
                                    startActivity(i);
                                    finish();
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
        final Snackbar snackBar = Snackbar.make(findViewById(R.id.activity_user_profile_admin), "Poor internet connection. To continue using RedFlow, please internet connection or turn on your wifi/data..", Snackbar.LENGTH_INDEFINITE);
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
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void about(){
        Intent intent = new Intent(this, about.class);
        startActivity(intent);
    }
/////////////////////////////////////////////////////
}
