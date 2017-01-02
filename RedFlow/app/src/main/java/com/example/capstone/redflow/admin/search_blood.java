package com.example.capstone.redflow.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.capstone.redflow.R;
import com.example.capstone.redflow.ToolBox;
import com.firebase.client.Firebase;

public class search_blood extends AppCompatActivity {

    EditText vSearch;

    String sSearch;

    ToolBox tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.search_blood);

        vSearch = (EditText) findViewById(R.id.edittext_srchblood);

        tools = new ToolBox();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void search_blood(View view) {

        sSearch = vSearch.getText().toString();

        if(sSearch.trim().equals("")) {
            Toast.makeText(this, "Please enter a serial number.", Toast.LENGTH_SHORT).show();
        }
        else if(sSearch.length() != 8) {
            Toast.makeText(this, "Invalid serial number.", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(this, search_blood_result.class);
            intent.putExtra("serial_number", sSearch.toUpperCase());
            startActivity(intent);
        }
    }

}
