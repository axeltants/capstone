package com.example.capstone.redflow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class profile extends AppCompatActivity {
    String userID;

    private static profile prof;

    private Firebase mRootRef;
    private FirebaseAuth auth;
    private FirebaseUser user;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.capstone.redflow.R.layout.profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prof = this;

        userID = getIntent().getStringExtra("userID");

        //Toast.makeText(this, "Welcome " + userID, Toast.LENGTH_SHORT).show();

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");
        auth = FirebaseAuth.getInstance();

        user = auth.getCurrentUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static profile getinstance(){return prof;}
}
