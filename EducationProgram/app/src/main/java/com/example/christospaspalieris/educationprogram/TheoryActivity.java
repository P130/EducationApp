package com.example.christospaspalieris.educationprogram;

import android.os.Build;
import android.support.annotation.IntegerRes;
//import android.support.design.ViewPagerAdapter;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class TheoryActivity extends AppCompatActivity {

    private static final String TAG = "TheoryActivity";

    private DatabaseReference mDatabaseNotes;
    private DatabaseReference mDatabaseTheory;

    ConstraintLayout cl;
    LinearLayout ll;
    TextView theoryTitle;
    TextView theory;
    int counter;
    LayoutInflater inflator;

    String subject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            subject = extras.getString("Subject");
            //The key argument here must match that used in the other activity
        }

        mDatabaseTheory = FirebaseDatabase.getInstance().getReference("Theory").child(subject);

        theoryTitle = (TextView) findViewById(R.id.theoryTitle);
        theoryTitle.setText(subject);
        theory = (TextView) findViewById(R.id.theory);

        Toast.makeText(getApplicationContext(), subject,
                Toast.LENGTH_LONG).show();

        mDatabaseTheory.child("Text").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String text = snapshot.getValue().toString();
                if (Build.VERSION.SDK_INT >= 24) {
                    theory.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)); // for 24 api and more
                } else {
                    theory.setText(Html.fromHtml(text)); // or for older api
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}


