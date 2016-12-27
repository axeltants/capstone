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

public class search_blood extends AppCompatActivity {

    EditText vSearch;

    String sSearch;

    ToolBox tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_blood);

        vSearch = (EditText) findViewById(R.id.edittext_srchblood);

        tools = new ToolBox();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void search(View view) {

        sSearch = vSearch.getText().toString();

        if(sSearch.trim().equals("")) {
            Toast.makeText(this, "Please enter a serial for blood bag", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(this, search_result.class);
            intent.putExtra("blood", sSearch);
            startActivity(intent);
        }
    }

    public void search_blood(View view) {
        Intent intent = new Intent(this, search_blood_result.class);
        startActivity(intent);
    }
}
