package com.example.capstone.redflow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private static LoginActivity loginActivity;

    private ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    Firebase mRootRef;
    Query query;

    String userID;
    String password;

    EditText vEmail;
    EditText vPassword;

    String sEmail;
    String sPassword;

    ToolBox tools = new ToolBox();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(com.example.capstone.redflow.R.layout.login);

        loginActivity = this;

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");

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
                        query = mRootRef.child("User").orderByChild("email").equalTo(sEmail);

                        query.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                userID = dataSnapshot.getKey().toString();
                                Intent i = new Intent(LoginActivity.this, home.class);
                                i.putExtra("userID", userID);
                                startActivity(i);
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

}
