package com.example.capstone.redflow;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class request extends AppCompatActivity {

    private Firebase mRootRef;

    private Spinner vBloodtype;
    private Spinner vLocation;
    private EditText vBagqty;

    private String bloodtype;
    private String location;
    private String sBagqty;
    private int bagqty;

    private String contact;
    private String message;
    private String notif;
    private String province;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.capstone.redflow.R.layout.request);

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onSubmitButton(View view) {
        vBloodtype = (Spinner) findViewById(R.id.spinnr_bloodtype);
        vLocation = (Spinner) findViewById(R.id.spinnr_location);
        vBagqty = (EditText) findViewById(R.id.edittext_bagqntty);

        bloodtype = vBloodtype.getSelectedItem().toString();
        location = vLocation.getSelectedItem().toString();
        sBagqty = vBagqty.getText().toString();

        if(sBagqty.trim().equals("")) {
            Toast.makeText(this, "Please enter quantity of blood bag needed.", Toast.LENGTH_SHORT).show();
        }
        else {
            Query query;
            bagqty = Integer.parseInt(sBagqty);

            message = "Someone is in need of " + bagqty + " bag(s) of blood type " + bloodtype + ". Please help us save this person's life.";
            //Toast.makeText(request.this, message, Toast.LENGTH_LONG).show();

            query = mRootRef.child("User").orderByChild("bloodtype").equalTo(bloodtype);
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Map<String, String> map = dataSnapshot.getValue(Map.class);

                    contact = map.get("contact");
                    notif = map.get("sms");
                    province = map.get("province");
                    if(notif.equals("on") && province.equals(location)) {
                        new SendRequest().execute();
                        //Toast.makeText(request.this, "Hey, " + contact, Toast.LENGTH_LONG).show();
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
}
