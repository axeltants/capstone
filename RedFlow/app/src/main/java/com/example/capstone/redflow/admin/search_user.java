package com.example.capstone.redflow.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.capstone.redflow.R;
import com.example.capstone.redflow.request;

public class search_user extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_user);
    }

    public void search(View view) {
        Intent intent = new Intent(this, search_result.class);
        startActivity(intent);
    }
}
