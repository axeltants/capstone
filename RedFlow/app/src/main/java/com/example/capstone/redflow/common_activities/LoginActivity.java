package com.example.capstone.redflow.common_activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Process;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import com.example.capstone.redflow.Firebasenotification.SharedPrefManager;
import com.example.capstone.redflow.R;
import com.example.capstone.redflow.ToolBox;
import com.example.capstone.redflow.admin.admin_home;
import com.example.capstone.redflow.user_activities.home;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static LoginActivity loginActivity;

    private ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    Firebase mRootRef;
    Firebase userRef;

    Query query;

    String userID;
    String password;

    EditText vEmail;
    EditText vPassword;

    String sEmail;
    String sPassword;

    String bloodType;
    String location;

    ToolBox tools = new ToolBox();

    SharedPreferences sharedpreferences;
    Intent in;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Uid = "uidKey";
    public static final String Email = "emailKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(com.example.capstone.redflow.R.layout.login);

        getWindow().setBackgroundDrawableResource(R.drawable.bg);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        loginActivity = this;

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");
        userRef = mRootRef.child("User");

        vEmail = (EditText) findViewById(R.id.edittext_email);
        vPassword = (EditText) findViewById(R.id.edittext_pass);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if(sharedpreferences.getString(Email, null) != null){
            in = new Intent(LoginActivity.this,home.class);
            startActivity(in);
        }

    }

    public static LoginActivity getInstance() {
        return loginActivity;
    }

    public void signin(View view) {

        if(isInternetAvailable()){

            sEmail = vEmail.getText().toString();
            sPassword = tools.SHA1(vPassword.getText().toString());

            if(sEmail.trim().equals("") || sPassword.trim().equals("")) {
                Toast toast = Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 88);
                toast.show();
            }
            else {
                progressDialog.setMessage("Signing in...");
                progressDialog.show();

                mAuth.signInWithEmailAndPassword(sEmail, sPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            progressDialog.dismiss();
                            query = userRef.orderByChild("email").equalTo(sEmail.toLowerCase());
                            query.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    Map<String, String> map = dataSnapshot.getValue(Map.class);
                                    userID = dataSnapshot.getKey().toString();

                                    if(map.get("status").equals("admin")) {
                                        Intent i = new Intent(LoginActivity.this, admin_home.class);
                                        i.putExtra("userID", userID);
                                        query.removeEventListener(this);
                                        startActivity(i);
                                    }
                                    else {
                                        SharedPreferences.Editor editor = sharedpreferences.edit();

                                        bloodType = map.get("bloodtype");
                                        location = map.get("province");

                                        new Thread(new Runnable() {

                                            @Override
                                            public void run() {
                                                android.os.Process.setThreadPriority(Process. THREAD_PRIORITY_BACKGROUND);
                                                sendTokenToServer();
                                            }

                                        }).start();

                                        Intent i = new Intent(LoginActivity.this, home.class);
                                        editor.putString(Uid, userID);
                                        editor.putString(Email, sEmail);
                                        editor.apply();
                                        i.putExtra("mail", sEmail);
                                        i.putExtra("userID", userID);
                                        query.removeEventListener(this);
                                        startActivity(i);
                                        //Toast.makeText(LoginActivity.this, "Bloodtype: " + bloodType + "\n Location: " + location, Toast.LENGTH_LONG).show();
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
                            });
                        }
                        else {
                            progressDialog.dismiss();
                            Toast toast = Toast.makeText(LoginActivity.this, "Wrong Email/Password.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 88);
                            toast.show();
                        }
                    }
                });
            }
        }
    }

    public void register(View view) {
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
    }

    private void sendTokenToServer() {

        final String token = SharedPrefManager.getInstance(this).getDeviceToken();
        final String email = sEmail;

        if (token == null) {
            //Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_REGISTER_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            //Toast.makeText(LoginActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        sendTokenToServer();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("bloodType", bloodType);
                params.put("location", location);
                params.put("token", token);
                return params;
            }
        };
        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void about(View view) {
        Intent intent = new Intent(this, about.class);
        startActivity(intent);
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
        final Snackbar snackBar = Snackbar.make(findViewById(R.id.activity_login), "Poor internet connection. To continue using RedFlow, please check your internet connection or turn on your wifi/data..", Snackbar.LENGTH_INDEFINITE);
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
            final Context ctx = context;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                    ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo ni = manager.getActiveNetworkInfo();
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

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
