package com.example.capstone.redflow;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.capstone.redflow.Firebasenotification.EndPoints;
import com.example.capstone.redflow.Firebasenotification.MyVolley;
import com.example.capstone.redflow.Firebasenotification.SharedPrefManager;
import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class home extends AppCompatActivity {

    private Firebase mRootRef;
    private Firebase userRef;
    private Firebase supplyRef;
    private Firebase notifyRef;
    private Firebase bloodRef;

    private String userID;
    private String mail;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(com.example.capstone.redflow.R.layout.home);

        userID = getIntent().getStringExtra("userID");
        mail = getIntent().getStringExtra("mail");

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");
        userRef = mRootRef.child("User");
        supplyRef = mRootRef.child("Supply");
        notifyRef = mRootRef.child("Notify");
        bloodRef = mRootRef.child("Blood");

        //Toast.makeText(this, "Welcome " + userID, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void request(View view) {
        Intent intent = new Intent(this, request.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void history(View view) {
        Toast.makeText(getApplicationContext(), "HISTORY", Toast.LENGTH_SHORT).show();
       /* Intent intent = new Intent(this, history.class);
        startActivity(intent);*/
        Intent intent = new Intent(this, Donation_history.class);
        startActivity(intent);
    }

    public void profile(View view) {
        Intent intent = new Intent(this, profile.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void notification(View view) {
        Toast.makeText(getApplicationContext(), "NOTIFICATION", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, notification.class);
        startActivity(intent);
    }

    public void donor(View view) {
        Toast.makeText(getApplicationContext(), "DONOR", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, beadonor.class);
        startActivity(intent);
    }

    public void location(View view) {
        Toast.makeText(getApplicationContext(), "Red Cross locations", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, redcross_location.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
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

    /*//////////////////////////////////////////FOR ACTION BAR EVENTS*/
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
        DeleteToken();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void about(){
        Intent intent = new Intent(this, about.class);
        startActivity(intent);
    }
/////////////////////////////////////////////////////
}
