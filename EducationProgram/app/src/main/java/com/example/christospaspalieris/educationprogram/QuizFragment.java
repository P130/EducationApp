package com.example.christospaspalieris.educationprogram;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizFragment extends Fragment implements View.OnClickListener {

    Button answer1, answer2, answer3, answer4, start_test,btnrate;
    TextView score, question, plus2;
    RatingBar ratequiz;
    private DatabaseReference db_questions,db_scores,current_user;
    private Questions questions_obj;
    private String mAnswer;
    private int mScore = 0;
    private int key = 1;
    private int maxKey = 10000;
    private MediaPlayer correct_sound;
    private CountDownTimer countDownTimer;
    private String user;
    private boolean isPaused = false;
    private long user_time = 0;
    private final static String TAG = "QuizFragment";
    private static final int TIMER_LENGTH = 20;
    final float startSize = 14; // Size in pixels
    final float endSize = 38;
    final int animationDuration = 400; // Animation duration in ms
    ValueAnimator plus2_animator;

    private TimerView mTimerView;

    String subject;
    String distinct_key;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quiz_fragment,container,false);
        ratequiz = (RatingBar) view.findViewById(R.id.ratingBar);

        current_user = FirebaseDatabase.getInstance().getReference("USERS").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        current_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                user = userInformation.getUsername();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Bundle bundle = this.getArguments();
        subject = bundle.getString("subject", "Default");
        db_questions = FirebaseDatabase.getInstance().getReference("Questions").child(subject);
        db_scores = FirebaseDatabase.getInstance().getReference("Scores").child(subject);


        answer1 = (Button) view.findViewById(R.id.answer1);
        answer1.setOnClickListener(this);
        answer2 = (Button) view.findViewById(R.id.answer2);
        answer2.setOnClickListener(this);
        answer3 = (Button) view.findViewById(R.id.answer3);
        answer3.setOnClickListener(this);
        answer4 = (Button) view.findViewById(R.id.answer4);
        answer4.setOnClickListener(this);
        btnrate = (Button) view.findViewById(R.id.rate_button);
        btnrate.setOnClickListener(this);

        score = (TextView) view.findViewById(R.id.score);
        question = (TextView) view.findViewById(R.id.question);

        countDownTimer = new CountDownTimer(10*TIMER_LENGTH*100,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(isPaused)
                    cancel();
                else
                    user_time = ((11*1000) - millisUntilFinished)/1000;

            }

            @Override
            public void onFinish() {
                GameOver();
            }
        };



        plus2 = (TextView)view.findViewById(R.id.plus2textView);
        plus2_animator = ValueAnimator.ofFloat(startSize, endSize);
        plus2_animator.setDuration(animationDuration);

        plus2_animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                plus2.setVisibility(View.VISIBLE);
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                plus2.setTextSize(animatedValue);


            }



        });

        plus2_animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                plus2.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });



        mTimerView = (TimerView) view.findViewById(R.id.timer);

        start_test = (Button) view.findViewById(R.id.start);
        start_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.start();
                mTimerView.start(TIMER_LENGTH);
                start_test.setVisibility(View.INVISIBLE);
                question.setVisibility(View.VISIBLE);
                answer1.setVisibility(View.VISIBLE);
                answer2.setVisibility(View.VISIBLE);
                answer3.setVisibility(View.VISIBLE);
                answer4.setVisibility(View.VISIBLE);
                score.setText("Score " + mScore);
                LoadNextQuestion();

            }
        });

        //correct_sound = MediaPlayer.create(this,R.raw.correct);





        //timer = (TextView) findViewById(R.id.time);







        return view;
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.answer1:
                if(answer1.getText().equals(mAnswer))
                {
                    mScore++;
                    score.setText("Score " + mScore);
                    plus2_animator.start();
                    mTimerView.right_answer = true;
                    key++;
                    //correct_sound.start();
                    LoadNextQuestion();
                }
                else
                    GameOver();
                break;
            case R.id.answer2:
                if(answer2.getText().equals(mAnswer))
                {
                    mScore++;
                    score.setText("Score " + mScore);
                    plus2_animator.start();
                    key++;
                    //correct_sound.start();
                    LoadNextQuestion();
                }
                else
                    GameOver();
                break;
            case R.id.answer3:
                if(answer3.getText().equals(mAnswer))
                {
                    mScore++;
                    score.setText("Score " + mScore);
                    plus2_animator.start();
                    key++;
                    //correct_sound.start();
                    LoadNextQuestion();
                }
                else
                    GameOver();
                break;
            case R.id.answer4:
                if(answer4.getText().equals(mAnswer))
                {
                    mScore++;
                    score.setText("Score " + mScore);
                    plus2_animator.start();
                    key++;
                    //correct_sound.start();
                    LoadNextQuestion();
                }
                else
                    GameOver();
                break;
            case R.id.rate_button:
                db_scores.child(user).child("User_Rate").setValue(ratequiz.getRating());
                startActivity(new Intent(getActivity(),EducationalProgramActivity.class));
                break;
        }


    }


    private void LoadNextQuestion() {

        if(key == maxKey + 1)
            Success();
        else
        {

            Log.d(TAG,String.valueOf(key));
            db_questions.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    maxKey = (int)dataSnapshot.getChildrenCount();
                    questions_obj = dataSnapshot.child(String.valueOf(key)).getValue(Questions.class);
                    Log.d(TAG,questions_obj.getQuestionText());
                    question.setText(questions_obj.getQuestionText());
                    answer1.setText(questions_obj.getChoiceA());
                    answer2.setText(questions_obj.getChoiceB());
                    answer3.setText(questions_obj.getChoiceC());
                    answer4.setText(questions_obj.getChoiceD());
                    mAnswer = questions_obj.getCorrectAnswer();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



    }



    private void Success() {
        isPaused = true;
        mTimerView.stop();
        countDownTimer.cancel();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setMessage("Congratulations!!! "+ user + " You've made it with score " + mScore + " points")
                .setCancelable(false)
                .setPositiveButton("Save my score", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db_scores.child(user).child("Score").setValue(mScore);
                        db_scores.child(user).child("Time").setValue(user_time);
                        db_scores.child(user).child("User_ID").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        startActivity(new Intent(getActivity(),EducationalProgramActivity.class));
                    }
                })
                .setNegativeButton("Go back to solve more", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(),EducationalProgramActivity.class));
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void ClearUI()
    {
        key = 1;
        mScore = 0;
        start_test.setVisibility(View.VISIBLE);
        question.setVisibility(View.INVISIBLE);
        answer1.setVisibility(View.INVISIBLE);
        answer2.setVisibility(View.INVISIBLE);
        answer3.setVisibility(View.INVISIBLE);
        answer4.setVisibility(View.INVISIBLE);
        LoadNextQuestion();
        isPaused = false;

        score.setText("Score: " + mScore);
    }

    public void GameOver(){
        isPaused = true;

        mTimerView.stop();
        countDownTimer.cancel();

        db_scores.child(user).child("Score").setValue(mScore);
        db_scores.child(user).child("Time").setValue(user_time);
        db_scores.child(user).child("User_ID").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setMessage("Game Over! Your score is " + mScore + " points. Would you like to try again?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ClearUI();
                        start_test.performClick();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClearUI();
                    }
                })
        .setNeutralButton("Rate Quiz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                answer1.setVisibility(View.GONE);
                answer2.setVisibility(View.GONE);
                answer3.setVisibility(View.GONE);
                answer4.setVisibility(View.GONE);
                start_test.setVisibility(View.GONE);
                score.setVisibility(View.GONE);
                question.setVisibility(View.GONE);
                plus2.setVisibility(View.GONE);
                ratequiz.setVisibility(View.VISIBLE);
                btnrate.setVisibility(View.VISIBLE);

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onPause() {
        mTimerView.stop();
        countDownTimer.cancel();
        super.onPause();
    }
}
