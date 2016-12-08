package com.example.axel.redflow;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by jp on 12/3/16.
 */

public class RedFlow extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
