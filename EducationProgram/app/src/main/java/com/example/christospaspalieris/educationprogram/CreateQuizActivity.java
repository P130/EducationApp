package com.example.christospaspalieris.educationprogram;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CreateQuizActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference db_test;
    EditText edit_title, edit_question, edit_choiceA, edit_choiceB, edit_choiceC, edit_choiceD, edit_answer;
    Button btn_next, btn_end;
    private int question_key = 1;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        typeface =Typeface.createFromAsset(getAssets(),"fonts/Chalk.ttf");
        db_test = FirebaseDatabase.getInstance().getReference("Tests").push();


        edit_title = (EditText) findViewById(R.id.edit_title_test);
        edit_question = (EditText) findViewById(R.id.edit_question);
        edit_choiceA = (EditText) findViewById(R.id.edit_ChoiceA);
        edit_choiceB = (EditText) findViewById(R.id.edit_ChoiveB);
        edit_choiceC = (EditText) findViewById(R.id.edit_ChoiceC);
        edit_choiceD = (EditText) findViewById(R.id.edit_ChoiceD);
        edit_answer = (EditText) findViewById(R.id.edit_Answer);

        btn_next = (Button) findViewById(R.id.button_next);
        btn_end = (Button) findViewById(R.id.button_confirm);

        edit_title.setTypeface(typeface);
        edit_question.setTypeface(typeface);
        edit_choiceA.setTypeface(typeface);
        edit_choiceB.setTypeface(typeface);
        edit_choiceC.setTypeface(typeface);
        edit_choiceD.setTypeface(typeface);
        edit_answer.setTypeface(typeface);
        btn_next.setTypeface(typeface);
        btn_end.setTypeface(typeface);



    }

    @Override
    public void onClick(View view) {
        if (view == btn_next) {

            if(TextUtils.isEmpty(edit_title.getText()) && TextUtils.isEmpty(edit_question.getText()) && TextUtils.isEmpty(edit_choiceA.getText()) && TextUtils.isEmpty(edit_choiceB.getText()) && TextUtils.isEmpty(edit_choiceC.getText()) && TextUtils.isEmpty(edit_choiceD.getText()) && TextUtils.isEmpty(edit_answer.getText()))
            {
                Toast.makeText(CreateQuizActivity.this, "Input Question please", Toast.LENGTH_SHORT).show();
            }
            else
            {
                LoadToFirebase();
                edit_title.setEnabled(false);
                edit_question.getText().clear();
                edit_choiceA.getText().clear();
                edit_choiceB.getText().clear();
                edit_choiceC.getText().clear();
                edit_choiceD.getText().clear();
                edit_answer.getText().clear();
                btn_end.setVisibility(View.VISIBLE);
            }


        }
        else if(view == btn_end)
        {
            /*String getTitle = edit_title.getText().toString();
            testsHelper.setTitle(getTitle);*/
            Toast.makeText(CreateQuizActivity.this,"Test is completed", Toast.LENGTH_SHORT).show();
            finish();

            Intent educationIntent = new Intent(CreateQuizActivity.this,EducationalProgramActivity.class);
            educationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(educationIntent);

        }
    }

    private void LoadToFirebase() {

        String Title = edit_title.getText().toString().trim();
        String Question = edit_question.getText().toString().trim();
        String ChoiceA = edit_choiceA.getText().toString().trim();
        String ChoiceB = edit_choiceB.getText().toString().trim();
        String ChoiceC = edit_choiceC.getText().toString().trim();
        String ChoiceD = edit_choiceD.getText().toString().trim();
        String Answer = edit_answer.getText().toString().trim();

        if(TextUtils.isEmpty(Title)) {
            Toast.makeText(CreateQuizActivity.this, "Please enter the title of the test", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(Question)) {
            Toast.makeText(CreateQuizActivity.this, "Please enter question", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(ChoiceA)) {
            Toast.makeText(CreateQuizActivity.this, "Please enter first choice", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(ChoiceB)) {
            Toast.makeText(CreateQuizActivity.this, "Please enter second choice", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(ChoiceC)) {
            Toast.makeText(CreateQuizActivity.this, "Please enter third choice", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(ChoiceD)) {
            Toast.makeText(CreateQuizActivity.this, "Please enter fourth choice", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(Answer)) {
            Toast.makeText(CreateQuizActivity.this, "Please enter the correct answer", Toast.LENGTH_SHORT).show();
            return;
        }


        if(TextUtils.equals(ChoiceA,Answer) || TextUtils.equals(ChoiceB,Answer) || TextUtils.equals(ChoiceC,Answer) || TextUtils.equals(ChoiceD,Answer))
        {
            Questions question = new Questions(edit_question.getText().toString(), edit_choiceA.getText().toString(), edit_choiceB.getText().toString(), edit_choiceC.getText().toString(), edit_choiceD.getText().toString(), edit_answer.getText().toString());
            db_test.child("Test_Questions").child(String.valueOf(question_key)).setValue(question);
            //TestsHelper testsHelper = new TestsHelper(Title);
            db_test.child("Test_Name").setValue(Title);
            // titles_tests.add(Title);
            question_key++;
        }
        else
        {
            Toast.makeText(CreateQuizActivity.this,"The correct answer needs to be the same with at least one choice", Toast.LENGTH_SHORT).show();
        }

    }
}
