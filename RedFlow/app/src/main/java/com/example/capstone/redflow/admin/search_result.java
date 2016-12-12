package com.example.capstone.redflow.admin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.capstone.redflow.R;
import com.example.capstone.redflow.admin_lists.resultGetSet;
import com.example.capstone.redflow.admin_lists.resultlistAdapter;
import com.example.capstone.redflow.admin_lists.resultprovider;

import java.util.List;

public class search_result extends AppCompatActivity {

    private List<resultGetSet> result = resultprovider.searchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        resultlistAdapter adapter = new resultlistAdapter(
                this, R.layout.search_resultlist, result);
        ListView lv = (ListView) findViewById(R.id.resultlist);
        lv.setAdapter(adapter);
    }
}
