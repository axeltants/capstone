package com.example.capstone.redflow.admin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ListView;

import com.example.capstone.redflow.R;
import com.example.capstone.redflow.Result_lists.resultGetSet;
import com.example.capstone.redflow.Result_lists.resultlistAdapter;
import com.example.capstone.redflow.Result_lists.resultprovider;

import java.util.List;

public class search_result extends AppCompatActivity {

    String searchname;

    private List<resultGetSet> result = resultprovider.searchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        searchname = getIntent().getStringExtra("searchname");

        resultlistAdapter adapter = new resultlistAdapter(
                this, R.layout.search_resultlist, result);
        ListView lv = (ListView) findViewById(R.id.resultlist);
        lv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
