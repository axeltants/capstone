package com.example.capstone.redflow.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.redflow.R;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

public class blood_supply_info extends AppCompatActivity {

    private String blood_type;

    private Firebase mRootRef;
    private Query query;

    private TextView bloodtype;
    private TextView bag_quantity;
    private EditText vBag_serial;

    private String sBag_serial;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blood_supply_info);

        blood_type = getIntent().getStringExtra("blood_type");

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");

        bloodtype = (TextView) findViewById(R.id.bloodtype);
        bag_quantity = (TextView) findViewById(R.id.bag_quantity);
        vBag_serial = (EditText) findViewById(R.id.bag_serial);

        bloodtype.setText(blood_type);
        query = mRootRef.child("Supply").child(blood_type).child("count");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count = Integer.parseInt(dataSnapshot.getValue().toString());
                bag_quantity.setText("Available: " + count);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void submit_bag(View view) {
        sBag_serial = vBag_serial.getText().toString();
        Firebase blood = mRootRef.child("Blood").push();

        if(sBag_serial.trim().equals("")) {
            Toast.makeText(this, "Please enter a serial number.", Toast.LENGTH_SHORT).show();
        }
        else if(sBag_serial.length() != 8) {
            Toast.makeText(this, "Invalid serial number.", Toast.LENGTH_SHORT).show();
        }
        else {
            blood.child("bloodtype").setValue(blood_type);
            blood.child("serial").setValue(sBag_serial.toUpperCase());
            blood.child("userID").setValue("-K_2nAZ1ynR9ZF15HvVw");
            mRootRef.child("Supply").child(blood_type).child("count").setValue(count+1);
            Intent intent = new Intent(blood_supply_info.this, blood_supply_info.class);
            intent.putExtra("blood_type", blood_type);
            this.finish();
            Toast.makeText(this, "Successfully added.", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    }
}
