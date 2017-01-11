package com.example.capstone.redflow;

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

import java.util.HashMap;
import java.util.Map;

public class zero_supply_request extends AppCompatActivity {
    private String bloodtype;
    private String qtty;
    private TextView vBloodtype;
    String message2;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zero_supply_request);

        bloodtype = getIntent().getStringExtra("bloodtype");
        qtty = getIntent().getStringExtra("qtty");
        vBloodtype = (TextView) findViewById(R.id.btype);
        vBloodtype.setText(bloodtype);
        message2 = "Someone is in need of " + qtty + " bag(s) of blood type " + bloodtype + ". Please help us save this person's life.";
        sendMultiplePush();
    }

    private void sendMultiplePush() {
        final String title = "RedFlow";
        final String message =  message2;
        final String image = null;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SEND_MULTIPLE_PUSH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(zero_supply_request.this, response, Toast.LENGTH_LONG).show();
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


}
