package com.example.capstone.redflow;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

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
        vBloodtype = (Spinner) findViewById(R.id.spinnr_bloodtype);

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");

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
        result9 = (EditText) findViewById(R.id.edittext_province);
        text9 = (EditText) findViewById(R.id.edittext_province);
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
                messageView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

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
                messageView.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

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

        text9.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.text_prompt, null);
                TextView messageView = (TextView)promptsView.findViewById(R.id.textView1);
                messageView.setText("Municipality/City");

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        result9.setText(userInput.getText());
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
        Toast.makeText(getApplicationContext(), "birthdate",
                Toast.LENGTH_SHORT)
                .show();
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
        sBloodtype = vBloodtype.getSelectedItem().toString();


        if(sFname.trim().equals("") || sLname.trim().equals("") || sMname.trim().equals("") || sContact.trim().equals("") || sNationality.trim().equals("") || sEmail.trim().equals("") || sPassword.trim().equals("") || sHome.trim().equals("") || sProvince.trim().equals("") || sZip.trim().equals("") || sBday.trim().equals("") || sGender.trim().equals("") || sBloodtype.trim().equals("")) {
            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
        }
        else if(sContact.length() != 11) {
            Toast.makeText(this, "Mobile number should be 11 digits long.", Toast.LENGTH_SHORT).show();
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

                                    newUser.child("birthday").setValue(day);
                                    newUser.child("birthmonth").setValue(month+1);
                                    newUser.child("birthyear").setValue(year);
                                    newUser.child("fname").setValue(tools.nameFormatter(sFname));
                                    newUser.child("lname").setValue(tools.nameFormatter(sLname));
                                    newUser.child("mname").setValue(tools.nameFormatter(sMname));
                                    newUser.child("contact").setValue(sContact);
                                    newUser.child("nationality").setValue(tools.nameFormatter(sNationality));
                                    newUser.child("email").setValue(sEmail.toLowerCase());
                                    newUser.child("home").setValue(sHome);
                                    newUser.child("province").setValue(tools.nameFormatter(sProvince));
                                    newUser.child("zip").setValue(sZip);
                                    newUser.child("gender").setValue(sGender);
                                    newUser.child("bloodtype").setValue(sBloodtype);
                                    newUser.child("status").setValue("Unverified");
                                    newUser.child("fullname").setValue(sFname.toLowerCase() + " " + sLname.toLowerCase());

                                    LoginActivity.getInstance().finish();
                                    Intent intent = new Intent(register.this, LoginActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(register.this, "Successfully registered.", Toast.LENGTH_SHORT).show();
                                    register.this.finish();

                                }
                                else {
                                    progressDialog.dismiss();
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
