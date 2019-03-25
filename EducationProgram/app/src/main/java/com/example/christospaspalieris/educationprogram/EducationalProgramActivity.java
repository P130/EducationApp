package com.example.christospaspalieris.educationprogram;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import org.jetbrains.annotations.NotNull;

import br.com.bloder.magic.view.MagicButton;

public class EducationalProgramActivity extends AppCompatActivity  {

    private static final String TAG = "EducationalProgram";
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mdbReference;
    private FirebaseUser user;
    private UserInformation userInformation;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private RelativeLayout myLayout;
    private PopupMenu popupMenu;
    private String role;
    private boolean display_tests;

    private BubblePicker picker;
    private MagicButton btnQuizTeacher;
    private MagicButton btnQuizStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educational_program);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null)
                {
                    Intent logregIntentt = new Intent(getApplicationContext(),LoginRegisterActivity.class);
                    logregIntentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logregIntentt);
                }
            }
        };
        user = mAuth.getCurrentUser();

        mdbReference = FirebaseDatabase.getInstance().getReference("USERS").child(user.getUid());

        btnQuizTeacher = (MagicButton) findViewById(R.id.btnQuizTeacher);
        btnQuizStudent = (MagicButton) findViewById(R.id.btnQuizStudent);

        mdbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInformation = dataSnapshot.getValue(UserInformation.class);
                role = userInformation.getRole();
                if(role.equals("student"))
                {
                    btnQuizStudent.setVisibility(View.VISIBLE);
                    display_tests = false;
                }
                else if(role.equals("teacher"))
                {
                    btnQuizTeacher.setVisibility(View.VISIBLE);
                    display_tests = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });








        btnQuizTeacher.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent createquizIntent = new Intent(EducationalProgramActivity.this, CreateQuizActivity.class);
                startActivity(createquizIntent);


            }
        });

        btnQuizStudent.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent quizIntent = new Intent(EducationalProgramActivity.this, QuizListActivity.class);
                startActivity(quizIntent);


            }
        });

        final String[] titles = getResources().getStringArray(R.array.subjects);
        final TypedArray colors = getResources().obtainTypedArray(R.array.colors);
       // final TypedArray images = getResources().obtainTypedArray(R.array.images);

        picker = (BubblePicker)findViewById(R.id.picker);

        picker.setCenterImmediately(true);
        picker.setBubbleSize(65);

        picker.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                return titles.length ;
            }

            @NotNull
            @Override
            public PickerItem getItem(int position) {
                PickerItem item = new PickerItem();
                item.setTitle(titles[position]);
                item.setColor(colors.getResourceId(position,0));

                item.setColor(colors.getInt(position,0));
                item.setTypeface(Typeface.SERIF);
                item.setTextColor(ContextCompat.getColor(getApplication(), android.R.color.white));
                return item;
            }
        });


        picker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem item) {

                Intent studypractiseIntent = new Intent(EducationalProgramActivity.this,StudyPracticeActivity.class);
                Bundle extras = new Bundle();
                extras.putString("subject",item.getTitle());
                extras.putString("role",role);
                studypractiseIntent.putExtras(extras);
                startActivity(studypractiseIntent);

            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem item) {

            }
        });

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //---

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close)
        {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                picker.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                picker.setVisibility(View.INVISIBLE);
            }
        };

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigationView);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem){
                switch (menuItem.getItemId()){
                    case(R.id.nav_profile):
                        finish();
                        Intent profileIntent = new Intent(getApplicationContext(),ProfileActivity.class);
                        startActivity(profileIntent);
                        break;
                    case(R.id.nav_forum):
                        Intent accountActivity = new Intent(getApplicationContext(), WallActivity.class);
                        startActivity(accountActivity);
                        break;
                    case(R.id.nav_community):
                        Intent communityIntent = new Intent(getApplicationContext(),CommunityActivity.class);
                        startActivity(communityIntent);
                        break;
                    case(R.id.nav_logout):
                        logout();
                        break;

                }
                return true;
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        picker.onResume();
    }



    @Override
    protected void onPause() {
        super.onPause();
        picker.onPause();
    }

    private void logout()
    {
        mAuth.signOut();
    }
}



