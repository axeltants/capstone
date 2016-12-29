package com.example.capstone.redflow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;


import com.example.capstone.redflow.notification_list.notificationGetSet;
import com.example.capstone.redflow.notification_list.notiflistAdapter;
import com.example.capstone.redflow.notification_list.notifprovider;

import java.util.List;

public class notification extends AppCompatActivity {

    private List<notificationGetSet> notifications = notifprovider.notiflist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.capstone.redflow.R.layout.notification);

        notiflistAdapter adapter = new notiflistAdapter(
                this, com.example.capstone.redflow.R.layout.list_notif, notifications);
        ListView lv = (ListView) findViewById(com.example.capstone.redflow.R.id.notificationlist);
        lv.setAdapter(adapter);
        lv.setEmptyView(findViewById(R.id.empty));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
