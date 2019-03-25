package com.example.christospaspalieris.educationprogram;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuizListActivity extends AppCompatActivity {


    private DatabaseReference student_tests_keys;
    private ListView testlistView;
    public ArrayList<String> tests_keys;
    private Fragment QuizFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        QuizFragment = new QuizFragment();

        testlistView = (ListView) findViewById(R.id.tests_list_view);
        tests_keys = new ArrayList<>();
        student_tests_keys = FirebaseDatabase.getInstance().getReference("Tests");
        student_tests_keys.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Log.d("KEY",ds.getKey());
                    tests_keys.add(ds.getKey());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        testlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(TestActivityStudent.this, tests_keys.get(position), Toast.LENGTH_SHORT).show();
                Intent test_intent = new Intent(QuizListActivity.this,QuizActivity.class);
                test_intent.putExtra("key",tests_keys.get(position));
                startActivity(test_intent);
            }
        });



        FirebaseListAdapter<QuizHelper> firebaseListAdapter = new FirebaseListAdapter<QuizHelper>(
                QuizListActivity.this,
                QuizHelper.class,
                R.layout.quiz_layout,
                student_tests_keys) {
            @Override
            protected void populateView(final View v, final QuizHelper model, int position) {

                student_tests_keys.child(tests_keys.get(position)).child("Test_Name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        TextView test_title = (TextView) v.findViewById(R.id.display_test_title);
                        test_title.setText(String.valueOf(dataSnapshot.getValue()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        testlistView.setAdapter(firebaseListAdapter);
    }
}
