package com.example.capstone.redflow.notimportant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ListView;

import com.example.capstone.redflow.Blood_history.claim_lists.claimAdapter;
import com.example.capstone.redflow.Blood_history.claim_lists.claimGetSet;
import com.example.capstone.redflow.Blood_history.claim_lists.claimProvider;
import com.example.capstone.redflow.R;

import java.util.List;

public class Claim_history extends AppCompatActivity {

    private List<claimGetSet> claims = claimProvider.claimlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.claim_history);

        claimAdapter adapter = new claimAdapter(
                this, R.layout.claim_history_list, claims);
        ListView lv = (ListView) findViewById(R.id.bloodclaimlist);
        lv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
