package com.example.capstone.redflow.user_activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Process;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.redflow.R;
import com.example.capstone.redflow.ToolBox;
import com.example.capstone.redflow.admin.admin_home;
import com.example.capstone.redflow.common_activities.LoginActivity;
import com.example.capstone.redflow.user_activities.home;
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
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.Calendar;
import java.util.Map;

import static java.lang.String.valueOf;

public class EditProfile extends AppCompatActivity {

    private static EditProfile editprof;

    private ProgressDialog progressDialog;

    private Firebase mRootRef;
    private Firebase userRef;
    private Firebase newUser;
    private FirebaseAuth mAuth;
    private Query query;
    private Query userquery;
    private ValueEventListener listener;
    //private ChildEventListener listener;
    private ValueEventListener userListenerVE;

    private EditText vFname;
    private EditText vLname;
    private EditText vMname;
    private EditText vContact;
    private EditText vNationality;
    private EditText vPasswordNew;
    private EditText vPasswordOld;
    private EditText vHome;
    private Spinner vProvince;
    private EditText vZip;
    private EditText vBday;
    private Spinner vGender;

    private String sFname;
    private String sLname;
    private String sMname;
    private String sContact;
    private String sNationality;
    private String sHome;
    private String sProvince;
    private String sZip;
    private String sBday;
    private String sGender;
    private String sEmailOld;
    private String sPasswordOld;
    private String sPasswordNew;

    private String mail;
    private String userID;

    private ToolBox tools = new ToolBox();

    final Context context = this;
    private EditText result;
    private EditText text;
    private EditText result2;
    private EditText text2;
    private EditText result3;
    private EditText text3;
    private EditText result4;
    private EditText text4;
    private EditText result5;
    private EditText text5;
    private EditText result6;
    private EditText text6;
    private EditText result7;
    private EditText text7;
    private EditText result8;
    private EditText text8;
    private EditText result9;
    private EditText text9;
    private EditText resulta;
    private EditText texta;
    private EditText resultb;
    private EditText textb;

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    private int agelimit;

    private int mDay, mMonth, mYear;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.edit_profile);

        getWindow().setBackgroundDrawableResource(R.drawable.bg);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        editprof = this;

        userID = getIntent().getStringExtra("userID");
        mail = getIntent().getStringExtra("mail");

        user = FirebaseAuth.getInstance().getCurrentUser();

        vFname = (EditText) findViewById(R.id.edittext_fname);
        vLname = (EditText) findViewById(R.id.edittext_lname);
        vMname = (EditText) findViewById(R.id.edittext_mname);
        vContact = (EditText) findViewById(R.id.edittext_contact);
        vNationality = (EditText) findViewById(R.id.edittext_nationality);
        vPasswordOld = (EditText) findViewById(R.id.edittext_password);
        vHome = (EditText) findViewById(R.id.edittext_home);
        vProvince = (Spinner) findViewById(R.id.spinnr_province);
        vZip = (EditText) findViewById(R.id.edittext_zip);
        vGender = (Spinner) findViewById(R.id.spinnr_gender);
        vPasswordNew = (EditText) findViewById(R.id.edittext_passwordNew);

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");
        userRef = mRootRef.child("User");

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);


        /*PROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOMMMPTTTTTTTTTTTTTTTEEEEEEEEEEEEEEEEERRRRRRRRRR*/

        result2 = (EditText) findViewById(R.id.edittext_password);
        text2 = (EditText) findViewById(R.id.edittext_password);
        result3 = (EditText) findViewById(R.id.edittext_fname);
        text3 = (EditText) findViewById(R.id.edittext_fname);
        result4 = (EditText) findViewById(R.id.edittext_lname);
        text4 = (EditText) findViewById(R.id.edittext_lname);
        result5 = (EditText) findViewById(R.id.edittext_mname);
        text5 = (EditText) findViewById(R.id.edittext_mname);
        result6 = (EditText) findViewById(R.id.edittext_contact);
        text6 = (EditText) findViewById(R.id.edittext_contact);
        result7 = (EditText) findViewById(R.id.edittext_nationality);
        text7 = (EditText) findViewById(R.id.edittext_nationality);
        result8 = (EditText) findViewById(R.id.edittext_home);
        text8 = (EditText) findViewById(R.id.edittext_home);
        resulta= (EditText) findViewById(R.id.edittext_zip);
        texta = (EditText) findViewById(R.id.edittext_zip);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);


        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


        text2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.text_prompt, null);
                TextView messageView = (TextView)promptsView.findViewById(R.id.textView1);
                messageView.setText("Password");
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                sPasswordOld = vPasswordOld.getText().toString();
                userInput.setText(sPasswordOld);
                userInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        result2.setText(userInput.getText());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        });

        text3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.text_prompt, null);
                TextView messageView = (TextView)promptsView.findViewById(R.id.textView1);
                messageView.setText("First name");

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                sFname = vFname.getText().toString();
                userInput.setText(sFname);
                userInput.setInputType(InputType.TYPE_CLASS_TEXT);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        result3.setText(userInput.getText());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        });

        text4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.text_prompt, null);
                TextView messageView = (TextView)promptsView.findViewById(R.id.textView1);
                messageView.setText("Last name");

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                sLname = vLname.getText().toString();
                userInput.setText(sLname);
                userInput.setInputType(InputType.TYPE_CLASS_TEXT);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        result4.setText(userInput.getText());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        });

        text5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.text_prompt, null);
                TextView messageView = (TextView)promptsView.findViewById(R.id.textView1);
                messageView.setText("Middle name");

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                sMname = vMname.getText().toString();
                userInput.setText(sMname);
                userInput.setInputType(InputType.TYPE_CLASS_TEXT);


                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        result5.setText(userInput.getText());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        });

        text6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.text_prompt, null);
                TextView messageView = (TextView)promptsView.findViewById(R.id.textView1);
                messageView.setText("Contact number");

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                sContact = vContact.getText().toString();
                userInput.setText(sContact);
                userInput.setInputType(InputType.TYPE_CLASS_NUMBER);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        result6.setText(userInput.getText());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        });

        text7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.text_prompt, null);
                TextView messageView = (TextView)promptsView.findViewById(R.id.textView1);
                messageView.setText("Nationality");

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                sNationality = vNationality.getText().toString();
                userInput.setText(sNationality);
                userInput.setInputType(InputType.TYPE_CLASS_TEXT);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        result7.setText(userInput.getText());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        });

        text8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.text_prompt, null);
                TextView messageView = (TextView)promptsView.findViewById(R.id.textView1);
                messageView.setText("Street Address");

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                sNationality = vNationality.getText().toString();
                userInput.setText(sNationality);
                userInput.setInputType(InputType.TYPE_CLASS_TEXT);

                sHome = vHome.getText().toString();
                userInput.setText(sHome);
                userInput.setInputType(InputType.TYPE_CLASS_TEXT);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        result8.setText(userInput.getText());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        });

        texta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.text_prompt, null);
                TextView messageView = (TextView)promptsView.findViewById(R.id.textView1);
                messageView.setText("Zipcode");
                messageView.setInputType(InputType.TYPE_CLASS_NUMBER);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                sZip = vZip.getText().toString();
                userInput.setText(sZip);
                userInput.setInputType(InputType.TYPE_CLASS_NUMBER);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        resulta.setText(userInput.getText());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        });

        userquery = mRootRef.child("User").child(userID);
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                Map<String, Integer> map2 = dataSnapshot.getValue(Map.class);

                vFname.setText(map.get("fname"));
                vLname.setText(map.get("lname"));
                vMname.setText(map.get("mname"));
                vContact.setText(map.get("contact"));
                vNationality.setText(map.get("nationality"));
                vHome.setText(map.get("home"));
                vProvince.setSelection(tools.getIndex(vProvince, map.get("province")));
                vZip.setText(map.get("zip"));
                vGender.setSelection(tools.getIndex(vGender, map.get("gender")));

                sEmailOld = map.get("email");
                mYear = map2.get("birthyear");
                mMonth = map2.get("birthmonth");
                mDay = map2.get("birthday");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        userquery.addValueEventListener(listener);
    }


    public static EditProfile getInstance(){
        return   editprof;
    }

    public void recorduser(View view) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

                if(isInternetAvailable()){

                    newUser = mRootRef.child("User").child(userID);

                    sFname = vFname.getText().toString();
                    sLname = vLname.getText().toString();
                    sMname = vMname.getText().toString();
                    sContact = vContact.getText().toString();
                    sNationality = vNationality.getText().toString();
                    sPasswordOld = vPasswordOld.getText().toString();
                    sPasswordNew = vPasswordNew.getText().toString();
                    sHome = vHome.getText().toString();
                    sProvince = vProvince.getSelectedItem().toString();
                    sZip = vZip.getText().toString();
                    sGender = vGender.getSelectedItem().toString();


                    if(sFname.trim().equals("") || sLname.trim().equals("") || sMname.trim().equals("") || sContact.trim().equals("") || sNationality.trim().equals("") || sHome.trim().equals("") || sProvince.trim().equals("") || sZip.trim().equals("") || sGender.trim().equals("")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(EditProfile.this, "Please fill out all fields.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 88);
                                toast.show();
                            }
                        });
                    }
                    else if(sContact.length() != 11) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(EditProfile.this, "Mobile number should be 11 digits long.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 88);
                                toast.show();
                            }
                        });

                    }
                    else if(sZip.length() != 4) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(EditProfile.this, "Zip number should be 4 digits long.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 88);
                                toast.show();
                            }
                        });
                    }
                    else if(sPasswordOld.isEmpty()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(EditProfile.this, "Please enter current password.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 88);
                                toast.show();
                            }
                        });
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.setMessage("Updating Profile...");
                                progressDialog.show();
                            }
                        });

                        mAuth.signInWithEmailAndPassword(sEmailOld.toLowerCase(), tools.SHA1(sPasswordOld)).addOnCompleteListener(EditProfile.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                final Task task2 = task;
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                                        if(isInternetAvailable()){
                                            if(task2.isSuccessful()) {
                                                newUser.child("fname").setValue(tools.nameFormatter(sFname));
                                                newUser.child("lname").setValue(tools.nameFormatter(sLname));
                                                newUser.child("mname").setValue(tools.nameFormatter(sMname));
                                                newUser.child("contact").setValue(sContact);
                                                newUser.child("nationality").setValue(tools.nameFormatter(sNationality));
                                                newUser.child("home").setValue(sHome);
                                                newUser.child("province").setValue(sProvince);
                                                newUser.child("zip").setValue(sZip);
                                                newUser.child("gender").setValue(sGender);
                                                newUser.child("fullname").setValue(sFname.toLowerCase() + " " + sLname.toLowerCase());
                                                if(!sPasswordNew.isEmpty()) {
                                                    user.updatePassword(tools.SHA1(sPasswordNew))
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        //Toast.makeText(MainActivity.this, "Password is updated!", Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        Toast.makeText(EditProfile.this, "Failed to update password!", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                }
                                                            });
                                                }

                                                progressDialog.dismiss();
                                                Intent intent = new Intent(EditProfile.this, profile.class);
                                                intent.putExtra("userID", userID);
                                                intent.putExtra("mail", mail);
                                                profile.getinstance().finish();
                                                EditProfile.this.finish();
                                                startActivity(intent);
                                            }
                                            else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        progressDialog.dismiss();
                                                        Toast toast = Toast.makeText(EditProfile.this, "Wrong Email/Password.", Toast.LENGTH_SHORT);
                                                        toast.setGravity(Gravity.TOP, 0, 88);
                                                        toast.show();
                                                    }
                                                });
                                            }
                                        }
                                    }

                                }).start();
                            }
                        });


                    }

                }

            }

        }).start();
    }

    public void login(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }


    public  boolean isInternetAvailable(){
        if(test()){
            try {
                HttpURLConnection urlConnection = (HttpURLConnection)
                        (new URL("https://clients3.google.com/generate_204")
                                .openConnection());
                urlConnection.setRequestProperty("User-Agent", "Android");
                urlConnection.setRequestProperty("Connection", "close");
                urlConnection.setConnectTimeout(1500);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 204 &&
                        urlConnection.getContentLength() == 0) {
                    Log.d("Network Checker", "Successfully connected to com.example.capstone.redflow.internet");
                    return true;
                }
            } catch (IOException e) {
                Log.e("Network Checker", "Error checking com.example.capstone.redflow.internet connection", e);
            }
        }
        final Snackbar snackBar = Snackbar.make(findViewById(R.id.register), "Poor internet connection. To continue using RedFlow, please check your internet connection or turn on your wifi/data..", Snackbar.LENGTH_INDEFINITE);
        View v = snackBar.getView();
        TextView textView = (TextView) v.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)v.getLayoutParams();
        params.gravity = Gravity.CENTER;
        v.setLayoutParams(params);
        snackBar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
            }
        });
        snackBar.show();
        return false;
    }

    public boolean test(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    private BroadcastReceiver networkStateReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isInternetAvailable();
                }
            }).start();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        unregisterReceiver(networkStateReceiver);
        super.onPause();
    }
}
