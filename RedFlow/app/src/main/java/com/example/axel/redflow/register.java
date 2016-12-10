package com.example.axel.redflow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

import static com.example.axel.redflow.R.layout.login;
import static java.lang.Boolean.FALSE;

public class register extends AppCompatActivity {

    private static register reg;

    private ProgressDialog progressDialog;

    private Firebase mRootRef;
    private Firebase newUser;
    private FirebaseAuth mAuth;
    private Query query;

    private EditText vFname;
    private EditText vLname;
    private EditText vMname;
    private EditText vContact;
    private EditText vNationality;
    private EditText vEmail;
    private EditText vPassword;
    private EditText vHome;
    private EditText vProvince;
    private EditText vZip;
    private EditText vBday;
    private Spinner vGender;

    private String sFname;
    private String sLname;
    private String sMname;
    private String sContact;
    private String sNationality;
    private String sEmail;
    private String sPassword;
    private String sHome;
    private String sProvince;
    private String sZip;
    private String sBday;
    private String sGender;

    private ToolBox tools = new ToolBox();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        reg = this;

        vFname = (EditText) findViewById(R.id.edittext_fname);
        vLname = (EditText) findViewById(R.id.edittext_lname);
        vMname = (EditText) findViewById(R.id.edittext_mname);
        vContact = (EditText) findViewById(R.id.edittext_contact);
        vNationality = (EditText) findViewById(R.id.edittext_nationality);
        vEmail = (EditText) findViewById(R.id.edittext_email);
        vPassword = (EditText) findViewById(R.id.edittext_password);
        vHome = (EditText) findViewById(R.id.edittext_home);
        vProvince = (EditText) findViewById(R.id.edittext_province);
        vZip = (EditText) findViewById(R.id.edittext_zip);
        vBday = (EditText) findViewById(R.id.edittext_bday);
        vGender = (Spinner) findViewById(R.id.spinnr_gender);

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

    }

    public static register getInstance(){
        return   reg;
    }

    public void recorduser(View view) {
        Firebase userRef = mRootRef.child("User");
        newUser = userRef.push();

        sFname = vFname.getText().toString();
        sLname = vLname.getText().toString();
        sMname = vMname.getText().toString();
        sContact = vContact.getText().toString();
        sNationality = vNationality.getText().toString();
        sEmail = vEmail.getText().toString();
        sPassword = vPassword.getText().toString();
        sHome = vHome.getText().toString();
        sProvince = vProvince.getText().toString();
        sZip = vZip.getText().toString();
        sBday = vBday.getText().toString();
        sGender = vGender.getSelectedItem().toString();

        if(sFname.trim().equals("") || sLname.trim().equals("") || sMname.trim().equals("") || sContact.trim().equals("") || sNationality.trim().equals("") || sEmail.trim().equals("") || sPassword.trim().equals("") || sHome.trim().equals("") || sProvince.trim().equals("") || sZip.trim().equals("") || sBday.trim().equals("") || sGender.trim().equals("")) {
            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
        }
        else {
            query = mRootRef.child("User").orderByChild("email").equalTo(sEmail);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null) {
                        Toast.makeText(register.this, "Email already exists.", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        progressDialog.setMessage("Registering user...");
                        progressDialog.show();
                        query.removeEventListener(this);
                        mAuth.createUserWithEmailAndPassword(sEmail, tools.SHA1(sPassword)).addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {

                                    progressDialog.dismiss();

                                    newUser.child("fname").setValue(sFname);
                                    newUser.child("lname").setValue(sLname);
                                    newUser.child("mname").setValue(sMname);
                                    newUser.child("contact").setValue(sContact);
                                    newUser.child("nationality").setValue(sNationality);
                                    newUser.child("email").setValue(sEmail);
                                    newUser.child("home").setValue(sHome);
                                    newUser.child("province").setValue(sProvince);
                                    newUser.child("zip").setValue(sZip);
                                    newUser.child("bday").setValue(sBday);
                                    newUser.child("gender").setValue(sGender);

                                    LoginActivity.getInstance().finish();
                                    Intent intent = new Intent(register.this, LoginActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(register.this, "Successfully registered.", Toast.LENGTH_SHORT).show();
                                    register.this.finish();

                                }
                                else {
                                    Toast.makeText(register.this, "Failed to register.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }

    }

    public void login(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}
