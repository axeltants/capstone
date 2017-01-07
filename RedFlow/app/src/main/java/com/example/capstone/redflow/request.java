package com.example.capstone.redflow;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class request extends AppCompatActivity {

    private Firebase mRootRef;
    private Firebase notify;
    private Query query;
    private Query sQuery;
    private Query notifyquery;

    private ProgressDialog progressDialog;

    private Spinner vBloodtype;
    private Spinner vLocation;
    private EditText vBagqty;

    private String bloodtype;
    private String location;
    private String sBagqty;

    private int bagqty;
    private int bloodcount;
    private long priority;
    private long matchcount;

    private String contact;
    private String message;
    private String notif;
    private String province;
    private String userID;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.capstone.redflow.R.layout.request);

        userID = getIntent().getStringExtra("userID");

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");

        notifyquery = mRootRef.child("Notify");
        notifyquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                priority = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        progressDialog = new ProgressDialog(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onSubmitButton(View view) {
        Query userquery;

        vBloodtype = (Spinner) findViewById(R.id.spinnr_bloodtype);
        vLocation = (Spinner) findViewById(R.id.spinnr_location);
        vBagqty = (EditText) findViewById(R.id.edittext_bagqntty);

        bloodtype = vBloodtype.getSelectedItem().toString();
        location = vLocation.getSelectedItem().toString();
        sBagqty = vBagqty.getText().toString();

        sQuery = mRootRef.child("Supply").child(bloodtype).child("count");
        sQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bloodcount = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        if(sBagqty.trim().equals("")) {
            Toast.makeText(this, "Please enter quantity of blood bag needed.", Toast.LENGTH_SHORT).show();
        }
        else {
            bagqty = Integer.parseInt(sBagqty);

            message = "Someone is in need of " + bagqty + " bag(s) of blood type " + bloodtype + ". Please help us save this person's life.";

            progressDialog.setMessage("Sending request...");
            progressDialog.show();

            if(bloodcount > bagqty) {
                Toast.makeText(request.this, "There are available supply. Please visit any RedCross blood facility to get blood.", Toast.LENGTH_SHORT).show();
            }
            else {
                userquery = mRootRef.child("User").child(userID).child("contact");
                userquery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        notify = mRootRef.child("Notify").child(dataSnapshot.getValue(String.class));
                        notify.child("priority").setValue(priority);
                        notify.child("qty").setValue(bagqty);
                        notify.child("bloodtype").setValue(bloodtype);
                        sendSMSRequest();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        }
    }


    public void sendSMSRequest() {

        query = mRootRef.child("User").orderByChild("bloodtype").equalTo(bloodtype);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                matchcount = dataSnapshot.getChildrenCount();
                progressDialog.dismiss();
                Toast.makeText(request.this, "Request sent. The system will notify you if there any available blood supply.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(request.this, home.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
                query.removeEventListener(this);
                request.this.finish();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                Firebase onsms;

                onsms = mRootRef.child("ONSMS").push();

                contact = map.get("contact");
                notif = map.get("sms");
                province = map.get("province");
                user = dataSnapshot.getKey();


                if (notif.equals("on") && province.equals(location)) {
                    int myDays = 1;

                    final Calendar c = Calendar.getInstance();
                    c.add(Calendar.DATE, myDays);  // number of days to add
                    int newDate =   (c.get(Calendar.YEAR) * 10000) +
                                    ((c.get(Calendar.MONTH) + 1) * 100) +
                                    (c.get(Calendar.DAY_OF_MONTH));

                    onsms.child("userID").setValue(user);
                    onsms.child("duedate").setValue(newDate);

                    //new SendRequest().execute();

                    //Toast.makeText(request.this, "Request sent.", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(request.this, "Hey, " + user, Toast.LENGTH_LONG).show();
                    //Toast.makeText(request.this, "Date is, " + newDate, Toast.LENGTH_LONG).show();

                    mRootRef.child("User").child(user).child("sms").setValue("off");
                }


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
        });
    }

    public class SendRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://axeltants.000webhostapp.com/sms.php");

                JSONObject postDataParams = new JSONObject();


                postDataParams.put("contact", contact); //ANHI IBUTANG ANG CONTACT NUMBER SA RCPIENT
                postDataParams.put("message", message); //ANG CONTENT SA MESSAGE DIRI.

                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();

        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
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
