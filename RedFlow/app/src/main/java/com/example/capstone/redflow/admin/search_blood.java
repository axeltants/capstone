package com.example.capstone.redflow.admin;

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
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.redflow.common_activities.LoginActivity;
import com.example.capstone.redflow.R;
import com.example.capstone.redflow.ToolBox;
import com.example.capstone.redflow.common_activities.about;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class search_blood extends AppCompatActivity {

    private EditText vSearch;

    private String sSearch;
    private String turf;

    private ToolBox tools;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_blood);

        turf = getIntent().getStringExtra("turf");

        vSearch = (EditText) findViewById(R.id.edittext_srchblood);

        tools = new ToolBox();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        vSearch.setRawInputType(Configuration.KEYBOARD_12KEY);
        vSearch.addTextChangedListener(new TextWatcher() {

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

    public void search_blood(View view) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                if(isInternetAvailable()){
                    sSearch = vSearch.getText().toString();

                    if(sSearch.trim().equals("")) {
                        progressDialog.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(search_blood.this, "Please enter a serial number.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 88);
                                toast.show();
                            }
                        });
                    }
                    else if(tools.isSerialValid(sSearch) == 0) {
                        progressDialog.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(search_blood.this, "Invalid serial number.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 88);
                                toast.show();
                            }
                        });
                    }
                    else {
                        progressDialog.dismiss();
                        Intent intent = new Intent(search_blood.this, search_blood_profile.class);
                        intent.putExtra("serial_number", sSearch.toUpperCase());
                        intent.putExtra("turf", turf);
                        startActivity(intent);
                    }
                }
            }

        }).start();
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
        final Snackbar snackBar = Snackbar.make(findViewById(R.id.search_blood), "Poor internet connection. To continue using RedFlow, please check your internet connection or turn on your wifi/data..", Snackbar.LENGTH_INDEFINITE);
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
