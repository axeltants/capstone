package com.example.capstone.redflow;

import android.app.Application;

import com.firebase.client.Firebase;

public class RedFlow extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
