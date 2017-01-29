package com.example.capstone.redflow.user_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

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

public class proceed_to_RedCross extends AppCompatActivity {

    private String bloodtype;
    private int bloodcount;

    private TextView vBloodcount;
    private TextView vBloodtype;
    String message2;
    private int qtty;
    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proceed_to__red_cross);

        bloodtype = getIntent().getStringExtra("bloodtype");
        bloodcount = getIntent().getIntExtra("bloodcount", 0);
        qtty = getIntent().getIntExtra("qtty",0);
        mail = getIntent().getStringExtra("mail");

        vBloodcount = (TextView) findViewById(R.id.edittext_bloodcount);
        vBloodtype = (TextView) findViewById(R.id.edittext_bloodtype);

        vBloodcount.setText(" (" + bloodcount + ") ");
        vBloodtype.setText(" " + bloodtype + " ");

        message2 = "Someone is in need of " + qtty + " bag(s) of blood type " + bloodtype + ". Your blood type is compatible with his/her blood, please help us save this person's life.";
        if(qtty > bloodcount){

            new Thread(new Runnable() {

                @Override
                public void run() {
                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                    sendFilteredPush();
                }

            }).start();

        }

    }

    public void location(View view) {
        Intent intent = new Intent(this, redcross_location.class);
        startActivity(intent);
        this.finish();
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
