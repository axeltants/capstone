package com.example.capstone.redflow.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.capstone.redflow.R;

public class search_menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_menu);
    }

    public void searchuser(View view) {
        Intent intent = new Intent(this, search_user.class);
        startActivity(intent);
    }

    public void searchbloodsupply(View view) {
        Intent intent = new Intent(this, search_blood.class);
        startActivity(intent);
    }
}
