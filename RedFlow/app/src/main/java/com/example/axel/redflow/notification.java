package com.example.axel.redflow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;


import com.example.axel.redflow.notification_list.notificationGetSet;
import com.example.axel.redflow.notification_list.notiflistAdapter;
import com.example.axel.redflow.notification_list.notifprovider;

import java.util.List;

public class notification extends AppCompatActivity {

    private List<notificationGetSet> notifications = notifprovider.notiflist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        notiflistAdapter adapter = new notiflistAdapter(
                this, R.layout.list_notif, notifications);
        ListView lv = (ListView) findViewById(R.id.notificationlist);
        lv.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
