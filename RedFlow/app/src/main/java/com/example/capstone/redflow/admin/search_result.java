package com.example.capstone.redflow.admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.capstone.redflow.LoginActivity;
import com.example.capstone.redflow.R;
import com.example.capstone.redflow.about;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Map;

public class search_result extends AppCompatActivity {

    private static search_result searchResult;

    private String searchname;

    private Firebase mRootRef;

    private ChildEventListener userListenerCE;
    private ChildEventListener userListenerCE2;
    private ChildEventListener userListenerCE3;

    private Query query;
    private Query query2;
    private Query query3;

    private ArrayList<String> result_list;
    private ArrayList<String> userIDs;
    private ArrayList<String> status;

    private ArrayAdapter<String> adapter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        searchResult = this;

        searchname = getIntent().getStringExtra("searchname");

        result_list = new ArrayList<String>();
        userIDs = new ArrayList<String>();
        status = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, R.layout.result_item, result_list);
        final ListView vResultList = (ListView) findViewById(R.id.resultlist);
        vResultList.setAdapter(adapter);

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Searching...");
        progressDialog.show();

        /******************************************************************************************/

        query = mRootRef.child("User").orderByChild("fname").equalTo(searchname);
        userListenerCE = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                result_list.add(map.get("fname") + " " + map.get("lname"));
                userIDs.add(dataSnapshot.getKey());
                status.add(map.get("status"));
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if(dataSnapshot.getChildrenCount() == 0) {
                    vResultList.setEmptyView(findViewById(R.id.empty));
                }
                query.removeEventListener(userListenerCE);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        query.addChildEventListener(userListenerCE);

        /******************************************************************************************/

        query2 = mRootRef.child("User").orderByChild("fullname").equalTo(searchname.toLowerCase());
        userListenerCE2 = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                result_list.add(map.get("fname") + " " + map.get("lname"));
                userIDs.add(dataSnapshot.getKey());
                status.add(map.get("status"));
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if(dataSnapshot.getChildrenCount() == 0) {
                    vResultList.setEmptyView(findViewById(R.id.empty));
                }
                query2.removeEventListener(userListenerCE2);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        query2.addChildEventListener(userListenerCE2);

        /******************************************************************************************/

        query3 = mRootRef.child("User").orderByChild("lname").equalTo(searchname);
        userListenerCE3 = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                result_list.add(map.get("fname") + " " + map.get("lname"));
                userIDs.add(dataSnapshot.getKey());
                status.add(map.get("status"));
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        query3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if(dataSnapshot.getChildrenCount() == 0) {
                    vResultList.setEmptyView(findViewById(R.id.empty));
                }
                query3.removeEventListener(userListenerCE3);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        query3.addChildEventListener(userListenerCE3);

        onClickListener();
    }

    private void onClickListener() {
        ListView list = (ListView) findViewById(R.id.resultlist);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View clickedView, int position, long id) {
                if(status.get(position).equals("Unverified")) {
                    Intent i = new Intent(search_result.this, user_profile_admin_verifier.class);
                    i.putExtra("userID", userIDs.get(position));
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(search_result.this, user_profile_admin.class);
                    i.putExtra("userID", userIDs.get(position));
                    startActivity(i);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static search_result getInstance() {
        return searchResult;
    }


    /*FOR ACTION BAR EVENTS*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionlogout:
                Logout();
                return true;
            case R.id.actionabout:
                about();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void Logout(){
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Do you really want to logout?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "successfully logged out", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        backtologin();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public void backtologin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void about(){
        Intent intent = new Intent(this, about.class);
        startActivity(intent);
    }
/////////////////////////////////////////////////////
}
