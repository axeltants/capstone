package com.example.capstone.redflow.admin;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.example.capstone.redflow.R;

public class announcement extends AppCompatActivity {

    private EditText editText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement);

        editText = (EditText) findViewById(R.id.edittext_message);
        textView = (TextView) findViewById(R.id.textView_count);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {


                textView.setText(String.valueOf(s.length())+"/300");

                    if (String.valueOf(s.length()).equals("300")){
                    textView.setTextColor(Color.RED);
                }
            }
        });

    }

}
