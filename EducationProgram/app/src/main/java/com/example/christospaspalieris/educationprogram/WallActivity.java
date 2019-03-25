package com.example.christospaspalieris.educationprogram;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class WallActivity extends AppCompatActivity {

    private final static String TAG = "WallActivity";

    private RecyclerView mForumList;

    private DatabaseReference mDatabase, mReactions;

    private FloatingActionButton addpost;

    public static String userid , postid, user_id;

    public int reactions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("Forum");


        mForumList = (RecyclerView)findViewById(R.id.forum_list);
        mForumList.setHasFixedSize(true);
        mForumList.setLayoutManager(new LinearLayoutManager(this));


        addpost = (FloatingActionButton) findViewById(R.id.floatingActionButton2);

        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PostActivity.class));
            }
        });
        addpost.setImageResource(R.drawable.chalk);



    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseRecyclerAdapter<Post,PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.forum_row,
                PostViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(final PostViewHolder viewHolder, Post model, final int position) {

                final FragmentManager fragmentManager=getFragmentManager();
                final ReactionsFragment reactionsFragment=new ReactionsFragment();
                final Bundle args = new Bundle();

                final Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Chalk.ttf");
                viewHolder.reaction_count.setTypeface(typeface);
                userid = model.getUid();
                postid = getRef(position).getKey();
                mReactions = mDatabase.child(postid).child("Reactions");
                args.putString("postkey", postid);
                mDatabase.child(postid).child("Reactions").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        reactions = Math.round(dataSnapshot.getChildrenCount());
                        Log.d(TAG,String.valueOf(reactions));
                        viewHolder.setReactionCount(reactions);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });






                DatabaseReference db = FirebaseDatabase.getInstance().getReference("USERS").child(userid);
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String profile_image = String.valueOf(dataSnapshot.child("image").getValue());
                        String username = String.valueOf(dataSnapshot.child("username").getValue());

                        viewHolder.setImage(getApplicationContext(),profile_image);
                        viewHolder.setUsername(username,typeface);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mDatabase.child(postid).child("Reactions").child(user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String get_reaction = String.valueOf(dataSnapshot.getValue());
                        viewHolder.setReaction(get_reaction);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.setTitle(model.getTitle(),typeface);
                viewHolder.setDesc(model.getDesc(),typeface);
                viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent displaypost = new Intent(getApplicationContext(),DisplayPost.class);
                        Log.d(TAG,userid);
                        displaypost.putExtra("postkey",postid);
                        startActivity(displaypost);
                    }
                });

                viewHolder.reaction_button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        postid = getRef(position).getKey();

                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(WallActivity.this);
                        View alertView = getLayoutInflater().inflate(R.layout.reaction_layout,null);
                        final ImageView reaction_goal = (ImageView) alertView.findViewById(R.id.goal);
                        final ImageView reaction_like = (ImageView) alertView.findViewById(R.id.like);
                        final ImageView reaction_awesome = (ImageView) alertView.findViewById(R.id.awesome);
                        final ImageView reaction_happy = (ImageView) alertView.findViewById(R.id.happy);
                        final ImageView reaction_sad = (ImageView) alertView.findViewById(R.id.sad);
                        final ImageView reaction_angry = (ImageView) alertView.findViewById(R.id.angry);

                        mBuilder.setView(alertView);
                        final AlertDialog dialog = mBuilder.create();
                        dialog.show();

                        reaction_goal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                              SaveReaction(postid,"Goal");
                              dialog.dismiss();
                            }
                        });

                        reaction_like.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SaveReaction(postid,"Like");
                                dialog.dismiss();
                            }
                        });

                        reaction_awesome.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SaveReaction(postid,"Awesome");
                                dialog.dismiss();
                            }
                        });

                        reaction_happy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SaveReaction(postid,"Happy");
                                dialog.dismiss();
                            }
                        });

                        reaction_sad.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SaveReaction(postid,"Sad");
                                dialog.dismiss();
                            }
                        });

                        reaction_angry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SaveReaction(postid,"Angry");
                                dialog.dismiss();
                            }
                        });


                    }

                });

                viewHolder.reaction_count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reactionsFragment.setArguments(args);
                        reactionsFragment.show(fragmentManager,"TV_tag");
                    }
                });



            }
        };

        mForumList.setAdapter(firebaseRecyclerAdapter);



    }




    public static class PostViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        CardView cardView;
        ImageView reaction_button;
        TextView reaction_count;

        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            reaction_button = (ImageView) mView.findViewById(R.id.reactionbutton);
            cardView = (CardView) mView.findViewById(R.id.post_cardview);
            reaction_button.setImageResource(R.drawable.like);
            reaction_count = (TextView) mView.findViewById(R.id.count_reactions);

        }

        public void setTitle(String title, Typeface typefaceTitle)
        {
            TextView post_title = (TextView)mView.findViewById(R.id.post_title);
            post_title.setTypeface(typefaceTitle);
            post_title.setText(title);
        }

        public void setDesc(String desc, Typeface typefaceDesc)
        {
            TextView post_desc = (TextView)mView.findViewById(R.id.post_desc);
            post_desc.setTypeface(typefaceDesc);
            post_desc.setText(desc);
        }

        public void setImage(Context ctx,String image)
        {
            ImageView poster_image = (ImageView)mView.findViewById(R.id.poster_image);
            Picasso.with(ctx).load(image).into(poster_image);
        }

        public void setUsername(String username, Typeface typefaceUsername)
        {
            TextView username_tv = (TextView)mView.findViewById(R.id.username_tv);
            username_tv.setTypeface(typefaceUsername);
            username_tv.setText(username);

        }

        public void setReactionCount(int reactioncount)
        {
            reaction_count.setText(String.valueOf(reactioncount));
        }

        public void setReaction(String reaction)
        {
            if(reaction.equals("Goal"))
                reaction_button.setImageResource(R.drawable.goal);
            if(reaction.equals("Like"))
                reaction_button.setImageResource(R.drawable.praise);
            if(reaction.equals("Awesome"))
                reaction_button.setImageResource(R.drawable.surprise);
            if(reaction.equals("Happy"))
                reaction_button.setImageResource(R.drawable.happy);
            if(reaction.equals("Sad"))
                reaction_button.setImageResource(R.drawable.disappoint);
            if(reaction.equals("Angry"))
                reaction_button.setImageResource(R.drawable.mad);
        }


    }




    public void SaveReaction(String post_id, String reaction)
    {

        mDatabase.child(post_id).child("Reactions").child(user_id).setValue(reaction);
    }






}
