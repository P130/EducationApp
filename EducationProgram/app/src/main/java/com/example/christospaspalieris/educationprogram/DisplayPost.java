package com.example.christospaspalieris.educationprogram;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DisplayPost extends AppCompatActivity {

    private final static String TAG = "DisplayPost";

    DatabaseReference dbpost,dbuser;
    TextView username,title,description;
    String user_key;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_post);

        typeface = Typeface.createFromAsset(getAssets(),"fonts/Chalk.ttf");

        username = (TextView) findViewById(R.id.display_username);
        title = (TextView) findViewById(R.id.display_title);
        description = (TextView) findViewById(R.id.display_desc);

        username.setTypeface(typeface);
        title.setTypeface(typeface);
        description.setTypeface(typeface);

        user_key = new String();

        Intent getkeys = getIntent();

        String post_key = getkeys.getExtras().getString("postkey");



        dbpost = FirebaseDatabase.getInstance().getReference("Forum").child(post_key);
        dbpost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Post userpost = dataSnapshot.getValue(Post.class);
                title.setText(userpost.getTitle());
                description.setText(userpost.getDesc());
                user_key = userpost.getUid();

                dbuser = FirebaseDatabase.getInstance().getReference("USERS").child(user_key);

                dbuser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserInformation user = dataSnapshot.getValue(UserInformation.class);
                        username.setText(user.getUsername());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Log.d(TAG, user_key + "ουφ");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
