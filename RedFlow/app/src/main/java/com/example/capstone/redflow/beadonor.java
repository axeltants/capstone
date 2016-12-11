package com.example.capstone.redflow;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class beadonor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.capstone.redflow.R.layout.beadonor);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.capstone.redflow.R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void redcross(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://www.redcross.org.ph/get-involved/give-blood/how-to-donate"));
        startActivity(intent);
    }

    public void health_test(View view) {
        Toast.makeText(getApplicationContext(), "Health test", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, beadonor.class);
        startActivity(intent);
    }
}
