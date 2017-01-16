package com.example.capstone.redflow.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.capstone.redflow.common_activities.LoginActivity;
import com.example.capstone.redflow.R;
import com.example.capstone.redflow.common_activities.about;
import com.google.firebase.auth.FirebaseAuth;

public class blood_supply_record extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blood_supply_record);
    }

    public void oplus(View view) {
        Intent intent = new Intent(this, blood_supply_info.class);
        intent.putExtra("blood_type", "O+");
        startActivity(intent);
    }

    public void ominus(View view) {
        Intent intent = new Intent(this, blood_supply_info.class);
        intent.putExtra("blood_type", "O-");
        startActivity(intent);
    }

    public void aplus(View view) {
        Intent intent = new Intent(this, blood_supply_info.class);
        intent.putExtra("blood_type", "A+");
        startActivity(intent);
    }

    public void aminus(View view) {
        Intent intent = new Intent(this, blood_supply_info.class);
        intent.putExtra("blood_type", "A-");
        startActivity(intent);
    }

    public void bplus(View view) {
        Intent intent = new Intent(this, blood_supply_info.class);
        intent.putExtra("blood_type", "B+");
        startActivity(intent);
    }

    public void bminus(View view) {
        Intent intent = new Intent(this, blood_supply_info.class);
        intent.putExtra("blood_type", "B-");
        startActivity(intent);
    }

    public void abplus(View view) {
        Intent intent = new Intent(this, blood_supply_info.class);
        intent.putExtra("blood_type", "AB+");
        startActivity(intent);
    }

    public void abminus(View view) {
        Intent intent = new Intent(this, blood_supply_info.class);
        intent.putExtra("blood_type", "AB-");
        startActivity(intent);
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
