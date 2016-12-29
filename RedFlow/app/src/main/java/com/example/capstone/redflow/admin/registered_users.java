package com.example.capstone.redflow.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

import com.example.capstone.redflow.R;
import com.example.capstone.redflow.Result_lists.resultGetSet;
import com.example.capstone.redflow.Result_lists.resultprovider;
import com.example.capstone.redflow.users_list.usersGetSet;
import com.example.capstone.redflow.users_list.userslistAdapter;
import com.example.capstone.redflow.users_list.usersprovider;

import java.util.List;

public class registered_users extends AppCompatActivity {

    private List<usersGetSet> user = usersprovider.userslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered_users);

        userslistAdapter adapter = new userslistAdapter(
                this, R.layout.registered_userslist, user);
        ListView lv = (ListView) findViewById(R.id.userslist);
        lv.setAdapter(adapter);
        lv.setEmptyView(findViewById(R.id.empty));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
