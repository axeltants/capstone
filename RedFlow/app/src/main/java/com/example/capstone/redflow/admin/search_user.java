package com.example.capstone.redflow.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.capstone.redflow.R;
import com.example.capstone.redflow.request;

public class search_user extends AppCompatActivity {

    EditText vSearch;

    String sSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_user);

        vSearch = (EditText) findViewById(R.id.edittext_srchusr);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void search(View view) {

        sSearch = vSearch.getText().toString();

        if(sSearch.trim().equals("")) {
            Toast.makeText(this, "Please enter a name.", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(this, search_result.class);
            intent.putExtra("searchname", sSearch);
            startActivity(intent);
        }
    }
}
