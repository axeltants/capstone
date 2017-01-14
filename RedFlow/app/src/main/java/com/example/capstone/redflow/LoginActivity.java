package com.example.capstone.redflow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.capstone.redflow.Firebasenotification.EndPoints;
import com.example.capstone.redflow.Firebasenotification.MyVolley;
import com.example.capstone.redflow.Firebasenotification.SharedPrefManager;
import com.example.capstone.redflow.admin.admin_home;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(com.example.capstone.redflow.R.layout.login);

        loginActivity = this;

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");
        userRef = mRootRef.child("User");

        vEmail = (EditText) findViewById(R.id.edittext_email);
        vPassword = (EditText) findViewById(R.id.edittext_pass);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
    }

    public static LoginActivity getInstance() {
        return loginActivity;
    }

    public void signin(View view) {
        sEmail = vEmail.getText().toString();
        sPassword = tools.SHA1(vPassword.getText().toString());

        if(sEmail.trim().equals("") || sPassword.trim().equals("")) {
            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
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
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                userRef.removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                        query.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                Map<String, String> map = dataSnapshot.getValue(Map.class);
                                userID = dataSnapshot.getKey().toString();

                                if(map.get("status").equals("admin")) {
                                    Intent i = new Intent(LoginActivity.this, admin_home.class);
                                    i.putExtra("userID", userID);
                                    startActivity(i);
                                }
                                else {
                                    bloodType = map.get("bloodtype");
                                    location = map.get("province");
                                    sendTokenToServer();
                                    Intent i = new Intent(LoginActivity.this, home.class);
                                    i.putExtra("mail", sEmail);
                                    i.putExtra("userID", userID);
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
                        Toast.makeText(LoginActivity.this, "Wrong Email/Password.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
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
                        sendTokenToServer();
                        //Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
}
