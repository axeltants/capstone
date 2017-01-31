package com.example.capstone.redflow.user_activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.capstone.redflow.Firebasenotification.EndPoints;
import com.example.capstone.redflow.Firebasenotification.MyVolley;
import com.example.capstone.redflow.R;

import java.util.HashMap;
import java.util.Map;

public class zero_supply_request extends AppCompatActivity {
    private String bloodtype;
    private String qtty;
    private TextView vBloodtype;
    String message2;
    private ProgressDialog progressDialog;

    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zero_supply_request);

        bloodtype = getIntent().getStringExtra("bloodtype");
        qtty = getIntent().getStringExtra("qtty");
        mail = getIntent().getStringExtra("mail");


        vBloodtype = (TextView) findViewById(R.id.btype);
        vBloodtype.setText(bloodtype);
        message2 = "Someone is in need of " + qtty + " bag(s) of blood type " + bloodtype + ". Please help us save this person's life.";

        new Thread(new Runnable() {

            @Override
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                sendFilteredPush();
            }

        }).start();
    }

    private void sendFilteredPush() {
        final String title = "RedFlow: Good Day!";
        final String message =  message2;
        final String image = null;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SEND_FILTERED_PUSH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(zero_supply_request.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sendFilteredPush();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("message", message);
                params.put("bloodType", bloodtype);
                params.put("email", mail);

                if (!TextUtils.isEmpty(image))
                    params.put("image", image);
                return params;
            }
        };

        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }
}
