package com.example.capstone.redflow.preliminary_bloodtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.capstone.redflow.R;

import java.util.List;

public class TestActivity extends Activity {
    List<QuestionGetSet> quesList;
    int score = 0;
    int qid = 0;
    QuestionGetSet currentQ;
    TextView txtQuestion;
    RadioButton rda;
    RadioButton rdb;
    TextView rdc;
    Button butNext;

    public TestActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.prelimtest);
        DbHelper db = new DbHelper(this);
        this.quesList = db.getAllQuestions();
        this.currentQ = (QuestionGetSet)this.quesList.get(this.qid);
        this.txtQuestion = (TextView)this.findViewById(R.id.textView1);
        this.rda = (RadioButton)this.findViewById(R.id.radio0);
        this.rdb = (RadioButton)this.findViewById(R.id.radio1);
        this.rdc = (TextView)this.findViewById(R.id.radio2);
        this.butNext = (Button)this.findViewById(R.id.button1);
        this.setQuestionView();
        this.butNext.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RadioGroup grp = (RadioGroup)TestActivity.this.findViewById(R.id.radioGroup1);
                RadioButton answer = (RadioButton)TestActivity.this.findViewById(grp.getCheckedRadioButtonId());
                Log.d("yourans", TestActivity.this.currentQ.getANSWER() + " " + answer.getText());
                if(TestActivity.this.currentQ.getANSWER().equals(answer.getText())) {
                    ++TestActivity.this.score;
                    Log.d("score", "Your score" + TestActivity.this.score);
                }

                if(TestActivity.this.qid < 20) {
                    TestActivity.this.currentQ = (QuestionGetSet)TestActivity.this.quesList.get(TestActivity.this.qid);
                    TestActivity.this.setQuestionView();
                } else {
                    Intent intent = new Intent(TestActivity.this, ResultActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("score", TestActivity.this.score);
                    intent.putExtras(b);
                    TestActivity.this.startActivity(intent);
                    TestActivity.this.finish();
                }

            }
        });
    }

    private void setQuestionView() {
        this.txtQuestion.setText(this.currentQ.getQUESTION());
        this.rda.setText(this.currentQ.getOPTA());
        this.rdb.setText(this.currentQ.getOPTB());
        this.rdc.setText(this.currentQ.getOPTC());
        ++this.qid;
    }
}

