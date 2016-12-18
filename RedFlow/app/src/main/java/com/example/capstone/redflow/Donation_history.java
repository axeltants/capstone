package com.example.capstone.redflow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ListView;

import com.example.capstone.redflow.Blood_history.donation_lists.donationAdapter;
import com.example.capstone.redflow.Blood_history.donation_lists.donationGetSet;
import com.example.capstone.redflow.Blood_history.donation_lists.donationprovider;

import java.util.List;

public class Donation_history extends AppCompatActivity {

    private List<donationGetSet> donations = donationprovider.donationlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donation_history);

        donationAdapter adapter = new donationAdapter(
                this, R.layout.donation_history_list, donations);
        ListView lv = (ListView) findViewById(R.id.blooddonationlist);
        lv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
