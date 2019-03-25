package com.example.christospaspalieris.educationprogram;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.squareup.picasso.Picasso;

public class GradesFragment extends Fragment implements View.OnClickListener {

    private String subject;
    DatabaseReference db_student_score;
    RecyclerView students;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grades,container,false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            subject = bundle.getString("subject", "nothing");
        }

        db_student_score = FirebaseDatabase.getInstance().getReference("Scores").child(subject);

        students = (RecyclerView) view.findViewById(R.id.students_recycler_view);
        students.setHasFixedSize(true);
        students.setLayoutManager(new LinearLayoutManager(getActivity()));



        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onStart() {
        super.onStart();


       FirebaseRecyclerAdapter<Students_Score,GradesViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Students_Score, GradesViewHolder>(
               Students_Score.class,
               R.layout.scores_student_row,
               GradesViewHolder.class,
               db_student_score
       ) {
           @Override
           protected void populateViewHolder(final GradesViewHolder viewHolder, Students_Score model, int position) {
               String username = getRef(position).getKey();




               viewHolder.setUsername(username);
               viewHolder.setScore(model.getScore());
               viewHolder.setTime(model.getTime());
               viewHolder.setRate(model.getUser_Rate());


               DatabaseReference db_student_image;
               db_student_image = FirebaseDatabase.getInstance().getReference("USERS").child(model.getUser_ID());
               db_student_image.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       String image_uri = String.valueOf(dataSnapshot.child("image").getValue());

                       viewHolder.setImage(getActivity(),image_uri);
                       Log.d("H POUTAH EIKONA", image_uri);

                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });


           }
       };

       students.setAdapter(firebaseRecyclerAdapter);

    }




    public static class GradesViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public GradesViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setScore (int score)
        {
            TextView score_text = (TextView)mView.findViewById(R.id.user_score);
            score_text.setText(String.valueOf(score));
        }

        public void setTime(int time)
        {
            TextView time_text = (TextView)mView.findViewById(R.id.user_time);
            time_text.setText(String.valueOf(time));
        }

        public void setImage(Context ctx, String image)
        {
            ImageView student_image = (ImageView)mView.findViewById(R.id.user_image);
            Picasso.with(ctx).load(image).into(student_image);
        }

        public void setUsername(String username)
        {
            TextView student_username = (TextView)mView.findViewById(R.id.user_username);
            student_username.setText(username);

        }

        public void setRate(float rating)
        {
            TextView student_rate = (TextView)mView.findViewById(R.id.user_rate);
            student_rate.setText(String.valueOf(rating));
        }


    }
}
