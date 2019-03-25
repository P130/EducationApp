package com.example.christospaspalieris.educationprogram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileDisplay extends AppCompatActivity {

    String key_friend;
    Intent intent;
    DatabaseReference getFriend;
    UserInformation userInformation;
    TextView display_username,diplay_firstName,display_lastName,display_age,display_role;
    ImageView userImage_display;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_display);

        display_username = (TextView) findViewById(R.id.Profile_username);
        diplay_firstName = (TextView) findViewById(R.id.firstName_display);
        display_lastName = (TextView) findViewById(R.id.lastName_display);
        display_age = (TextView) findViewById(R.id.age_display);
        display_role = (TextView) findViewById(R.id.role_display);

        userImage_display = (ImageView) findViewById(R.id.userimage_display);

        intent = getIntent();
        key_friend = intent.getStringExtra("user_friend");

        getFriend = FirebaseDatabase.getInstance().getReference().child("USERS").child(key_friend);
        getFriend.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInformation = dataSnapshot.getValue(UserInformation.class);
                Picasso.with(ProfileDisplay.this).load(userInformation.getImage()).fit().into(userImage_display);
                display_username.setText(userInformation.getUsername());
                diplay_firstName.setText(userInformation.getFirstName());
                display_lastName.setText(userInformation.getLastName());
                display_age.setText(userInformation.getAge());
                display_role.setText(userInformation.getRole());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
