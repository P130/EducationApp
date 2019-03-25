package com.example.christospaspalieris.educationprogram;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CommunityActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseUsers;
    private RecyclerView mFriendsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("USERS");
        mFriendsList = (RecyclerView)findViewById(R.id.recyclerView);
        mFriendsList.setHasFixedSize(true);
        mFriendsList.setLayoutManager(new LinearLayoutManager(this));
    }




    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Friends,FriendsViewHolder> firebaserecycleradapter = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(
                Friends.class,
                R.layout.friends_row,
                FriendsViewHolder.class,
                mDatabaseUsers
        ) {
            @Override
            protected void populateViewHolder(FriendsViewHolder viewHolder, Friends model, int position) {
                final String friend_key = getRef(position).getKey();
                viewHolder.setUsername(model.getUsername());
                viewHolder.setOccupation(model.getRole());
                viewHolder.setImageUri(model.getImage(),getApplicationContext());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profileIntent = new Intent(getApplicationContext(),ProfileDisplay.class);
                        profileIntent.putExtra("user_friend",friend_key);
                        startActivity(profileIntent);
                    }
                });
            }
        };

        mFriendsList.setAdapter(firebaserecycleradapter);


    }


    public static class FriendsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        ImageView userImage;

        public FriendsViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
            userImage = (ImageView)mView.findViewById(R.id.profile_friend);
        }

        public void setImageUri(String imageUri, Context cxt)
        {
            ImageView profile_icon = (ImageView) mView.findViewById(R.id.profile_friend);
            Picasso.with(cxt).load(imageUri).into(profile_icon);
        }

        public void setUsername(String username)
        {
            TextView username_tv = (TextView) mView.findViewById(R.id.username_friend);
            username_tv.setText(username);
        }

        public void setOccupation(String occupation)
        {
            TextView occupation_tv = (TextView) mView.findViewById(R.id.occupation);
            occupation_tv.setText(occupation);
        }

    }

}
