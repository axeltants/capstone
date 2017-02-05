package com.example.capstone.redflow.common_activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.example.capstone.redflow.user_activities.home;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.Calendar;

public class register extends AppCompatActivity {

    private static register reg;

    private ProgressDialog progressDialog;

    private Firebase mRootRef;
    private Firebase userRef;
    private Firebase newUser;
    private FirebaseAuth mAuth;
    private Query query;

    private ValueEventListener userListenerVE;

    private EditText vFname;
    private EditText vLname;
    private EditText vMname;
    private EditText vContact;
    private EditText vNationality;
    private EditText vEmail;
    private EditText vPassword;
    private EditText vHome;
    private Spinner vProvince;
    private EditText vZip;
    private EditText vBday;
    private Spinner vGender;
    private Spinner vBloodtype;

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
    private String sBloodtype;

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

    private int mDay, mMonth, mYear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.register);

        getWindow().setBackgroundDrawableResource(R.drawable.bg);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        reg = this;

        vFname = (EditText) findViewById(R.id.edittext_fname);
        vLname = (EditText) findViewById(R.id.edittext_lname);
        vMname = (EditText) findViewById(R.id.edittext_mname);
        vContact = (EditText) findViewById(R.id.edittext_contact);
        vNationality = (EditText) findViewById(R.id.edittext_nationality);
        vEmail = (EditText) findViewById(R.id.edittext_email);
        vPassword = (EditText) findViewById(R.id.edittext_password);
        vHome = (EditText) findViewById(R.id.edittext_home);
        vProvince = (Spinner) findViewById(R.id.spinnr_province);
        vZip = (EditText) findViewById(R.id.edittext_zip);
        vBday = (EditText) findViewById(R.id.edittext_bday);
        vGender = (Spinner) findViewById(R.id.spinnr_gender);
        vBloodtype = (Spinner) findViewById(R.id.spinnr_bloodtype);

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");
        userRef = mRootRef.child("User");

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        /*PROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOMMMPTTTTTTTTTTTTTTTEEEEEEEEEEEEEEEEERRRRRRRRRR*/

        result = (EditText) findViewById(R.id.edittext_email);
        text = (EditText) findViewById(R.id.edittext_email);
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

        dateView = (TextView) findViewById(R.id.edittext_bday);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                LayoutInflater li = LayoutInflater.from(context);

                View promptsView = li.inflate(R.layout.text_prompt, null);
                TextView messageView = (TextView)promptsView.findViewById(R.id.textView1);
                messageView.setText("E-mail adress");
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                sEmail = vEmail.getText().toString();
                userInput.setText(sEmail);
                userInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        result.setText(userInput.getText());
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

                sPassword = vPassword.getText().toString();
                userInput.setText(sPassword);
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
    }

    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day

                    mYear = arg1;
                    mMonth = arg2;
                    mDay = arg3;

                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    public static register getInstance(){
        return   reg;
    }

    public void recorduser(View view) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

                if(isInternetAvailable()){

                    newUser = userRef.push();

                    sFname = vFname.getText().toString();
                    sLname = vLname.getText().toString();
                    sMname = vMname.getText().toString();
                    sContact = vContact.getText().toString();
                    sNationality = vNationality.getText().toString();
                    sEmail = vEmail.getText().toString();
                    sPassword = vPassword.getText().toString();
                    sHome = vHome.getText().toString();
                    sProvince = vProvince.getSelectedItem().toString();
                    sZip = vZip.getText().toString();
                    sBday = vBday.getText().toString();
                    sGender = vGender.getSelectedItem().toString();
                    sBloodtype = vBloodtype.getSelectedItem().toString();


                    if(sFname.trim().equals("") || sLname.trim().equals("") || sMname.trim().equals("") || sContact.trim().equals("") || sNationality.trim().equals("") || sEmail.trim().equals("") || sPassword.trim().equals("") || sHome.trim().equals("") || sProvince.trim().equals("") || sZip.trim().equals("") || sBday.trim().equals("") || sGender.trim().equals("") || sBloodtype.trim().equals("")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(register.this, "Please fill out all fields.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 88);
                                toast.show();
                            }
                        });
                    }
                    else if(sContact.length() != 11) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(register.this, "Mobile number should be 11 digits long.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 88);
                                toast.show();
                            }
                        });

                    }
                    else if(sZip.length() != 4) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(register.this, "Zip number should be 4 digits long.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 88);
                                toast.show();
                            }
                        });
                    }
                    else {
                        query = userRef.orderByChild("email").equalTo(sEmail);

                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue() != null) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast toast = Toast.makeText(register.this, "Email already exists.", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.TOP, 0, 88);
                                            toast.show();
                                        }
                                    });
                                    query.removeEventListener(this);
                                }
                                else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.setMessage("Registering user...");
                                            progressDialog.show();
                                        }
                                    });
                                    query.removeEventListener(this);
                                    mAuth.createUserWithEmailAndPassword(sEmail, tools.SHA1(sPassword)).addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()) {

                                                newUser.child("birthday").setValue(mDay);
                                                newUser.child("birthmonth").setValue(mMonth+1);
                                                newUser.child("birthyear").setValue(mYear);
                                                newUser.child("fname").setValue(tools.nameFormatter(sFname));
                                                newUser.child("lname").setValue(tools.nameFormatter(sLname));
                                                newUser.child("mname").setValue(tools.nameFormatter(sMname));
                                                newUser.child("contact").setValue(sContact);
                                                newUser.child("nationality").setValue(tools.nameFormatter(sNationality));
                                                newUser.child("email").setValue(sEmail.toLowerCase());
                                                newUser.child("home").setValue(sHome);
                                                newUser.child("province").setValue(sProvince);
                                                newUser.child("zip").setValue(sZip);
                                                newUser.child("gender").setValue(sGender);
                                                newUser.child("bloodtype").setValue(sBloodtype);
                                                newUser.child("status").setValue("Unverified");
                                                newUser.child("fullname").setValue(sFname.toLowerCase() + " " + sLname.toLowerCase());
                                                newUser.child("sms").setValue("on");
                                                newUser.child("request").setValue("on");

                                                mRootRef.child("Unread").child(newUser.getKey()).setValue("off");

                                                progressDialog.dismiss();


                                                Intent intent = new Intent(register.this, LoginActivity.class);
                                                intent.putExtra("userID", newUser.getKey());
                                                intent.putExtra("mail", sEmail.toLowerCase());
                                                startActivity(intent);
                                                Toast toast = Toast.makeText(register.this, "Successfully registered.", Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.TOP, 0, 88);
                                                toast.show();
                                                register.this.finish();

                                            }
                                            else {
                                                progressDialog.dismiss();
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast toast = Toast.makeText(register.this, "Failed to register.", Toast.LENGTH_SHORT);
                                                        toast.setGravity(Gravity.TOP, 0, 88);
                                                        toast.show();
                                                    }
                                                });
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
