package com.example.christospaspalieris.educationprogram;

import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ReactionsFragment extends DialogFragment {

    RecyclerView rv;
    DatabaseReference reactions_Ref;
    String post_key;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_reactions,container);
        this.getDialog().setTitle("Reactions...");


        post_key = getArguments().getString("postkey");

        reactions_Ref = FirebaseDatabase.getInstance().getReference("Forum").child(post_key).child("Reactions");

        FirebaseRecyclerAdapter<String,ReactionsViewHolder> reactionsAdapter = new FirebaseRecyclerAdapter<String, ReactionsViewHolder>(
                String.class,
                R.layout.load_reactions_row,
                ReactionsViewHolder.class,
                reactions_Ref
        ) {
            @Override
            protected void populateViewHolder(final ReactionsViewHolder viewHolder, String model, int position) {

                String get_userid = getRef(position).getKey();
                DatabaseReference username_Ref = FirebaseDatabase.getInstance().getReference("USERS");
                username_Ref.child(get_userid).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.setUsername_text(String.valueOf(dataSnapshot.getValue()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                reactions_Ref.child(get_userid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.setImg_reaction(String.valueOf(dataSnapshot.getValue()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        rv = (RecyclerView) rootView.findViewById(R.id.mRecyerID);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        rv.setAdapter(reactionsAdapter);



        return rootView;
    }


    public static class ReactionsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView username_text;
        ImageView img_reaction;

        public ReactionsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            username_text = (TextView) mView.findViewById(R.id.username_load_reactions_row);
            img_reaction = (ImageView) mView.findViewById(R.id.img_load_reactions_row);
        }

        public void setUsername_text(String text)
        {
            username_text.setText(text);
        }

        public void setImg_reaction(String img_text)
        {
            if(img_text.equals("Goal"))
                img_reaction.setImageResource(R.drawable.goal);
            if(img_text.equals("Like"))
                img_reaction.setImageResource(R.drawable.praise);
            if(img_text.equals("Awesome"))
                img_reaction.setImageResource(R.drawable.surprise);
            if(img_text.equals("Happy"))
                img_reaction.setImageResource(R.drawable.happy);
            if(img_text.equals("Sad"))
                img_reaction.setImageResource(R.drawable.disappoint);
            if(img_text.equals("Angry"))
                img_reaction.setImageResource(R.drawable.mad);
        }
    }

}


