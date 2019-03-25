package com.example.christospaspalieris.educationprogram;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class StudyPracticeActivity extends AppCompatActivity {

    private static final String TAG = "StudyPractiseActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;
    private Fragment TheoryFragment ,QuizFragment, GradesFragment;

    String subject;
    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_practice);
        Log.d(TAG, "onCreate: Starting.");

        TheoryFragment = new TheoryFragment();
        QuizFragment =  new QuizFragment();
        GradesFragment = new GradesFragment();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            subject = extras.getString("subject");
            role = extras.getString("role");
        }

        Bundle bundle = new Bundle();
        bundle.putString("subject", subject);
        bundle.putString("role", role);
        TheoryFragment.setArguments(bundle);
        QuizFragment.setArguments(bundle);
        GradesFragment.setArguments(bundle);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container2);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs2);
        tabLayout.setupWithViewPager(mViewPager);
    }


    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(TheoryFragment, "Theory");
        if(role.equals("student"))
            adapter.addFragment(QuizFragment, "Quiz");
        else if(role.equals("teacher"))
            adapter.addFragment(GradesFragment,"Grades");
        viewPager.setAdapter(adapter);

    }
}
