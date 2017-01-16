package com.example.capstone.redflow.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.capstone.redflow.Firebasenotification.Send_Push_Notification;
import com.example.capstone.redflow.Firebasenotification.EndPoints;
import com.example.capstone.redflow.Firebasenotification.MyVolley;
import com.example.capstone.redflow.LoginActivity;
import com.example.capstone.redflow.R;
import com.example.capstone.redflow.SendRequest;
import com.example.capstone.redflow.ToolBox;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class announcement extends AppCompatActivity {

    private EditText editText;
    private TextView textView;
    private List<String> devices;
    private ProgressDialog progressDialog;

    private Firebase mRootRef;
    private Firebase notifRef;
    private Query query;
    private ChildEventListener listener;

    private String message;
    private String contact;

    private int date;
    private int time;

    private  double datetime;

    private Calendar c;

    private ToolBox tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.announcement);

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");

        editText = (EditText) findViewById(R.id.edittext_message);
        textView = (TextView) findViewById(R.id.textView_count);

        tools = new ToolBox();

        c = Calendar.getInstance();

        date = tools.getCurrentDate();
        time = tools.getCurrentTime();
        datetime = tools.getDateTime();


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {


                textView.setText(String.valueOf(s.length())+"/300");

                if (String.valueOf(s.length()).equals("300")){
                    textView.setTextColor(Color.RED);
                }
            }
        });

    }

    private void sendMultiplePush() {
        final String title = "RedFlow";
        final String message = editText.getText().toString();
        final String image = null;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SEND_MULTIPLE_PUSH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        editText.setText("");
                        progressDialog.dismiss();
                        Toast.makeText(announcement.this, "Message sent.", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(announcement.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("message", message);

                if (!TextUtils.isEmpty(image))
                    params.put("image", image);
                return params;
            }
        };

        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void announce(View view) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending...");
        progressDialog.show();

        message = editText.getText().toString();

        query = mRootRef.child("User").orderByChild("sms").equalTo("on");
        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);

                notifRef = mRootRef.child("Notification").child(dataSnapshot.getKey()).push();
                notifRef.child("content").setValue(message);
                notifRef.child("date").setValue(date);
                notifRef.child("time").setValue(time);
                notifRef.child("datetime").setValue(datetime);

                new SendRequest(map.get("contact"), message).execute();
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
                query.removeEventListener(listener);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        query.addChildEventListener(listener);

        sendMultiplePush();
    }
}
