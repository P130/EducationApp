package com.example.christospaspalieris.educationprogram;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class TheoryFragment extends Fragment implements View.OnClickListener {


    String subject;
    String role;
    String key;
    float coordX,coordY;
    int imageViewID;
    TextView theoryTitle,theory,noteTV,noteAuthorTV;
    EditText editTheoryText,noteEditText;
    FloatingActionButton editheoryFAB;
    private ScrollView scrollview;
    private Button addNoteBtn,cancelBtn,hideNoteBtn;
    private LinearLayout linearLayout,noteLayout;
    private RelativeLayout theoryLayout;
    private DatabaseReference mDatabaseTheory,noteRef,userRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private boolean editmode = false;
    private boolean istoadd = true;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.theory_fragment,container,false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            subject = bundle.getString("subject", "nothing");
            role = bundle.getString("role", "nothing");
        }

        imageViewID = 555;
        noteAuthorTV = (TextView) view.findViewById(R.id.note_author);
        noteTV = (TextView) view.findViewById(R.id.noteTV) ;
        noteLayout = (LinearLayout) view.findViewById(R.id.note_display_layout);
        linearLayout = (LinearLayout) view.findViewById(R.id.myLinearLayout);
        theoryLayout = (RelativeLayout) view.findViewById(R.id.theoryLayout);
        noteEditText = (EditText) view.findViewById(R.id.noteET);
        scrollview = (ScrollView) view.findViewById(R.id.scrollv);
        addNoteBtn = (Button) view.findViewById(R.id.addnoteBtn);
        addNoteBtn.setOnClickListener(this);
        hideNoteBtn = (Button) view.findViewById(R.id.hideNoteBtn);
        hideNoteBtn.setOnClickListener(this);
        cancelBtn = (Button)view.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(this);
        editheoryFAB = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        editTheoryText = (EditText) view.findViewById(R.id.editTextTheory);


        mDatabaseTheory = FirebaseDatabase.getInstance().getReference("Theory").child(subject);
        noteRef = FirebaseDatabase.getInstance().getReference("Theory").child(subject).child("Notes");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        theoryTitle = (TextView) view.findViewById(R.id.theoryTitle);
        theoryTitle.setText(subject);
        theory = (TextView) view.findViewById(R.id.theory);

        mDatabaseTheory.child("Text").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String text = snapshot.getValue().toString();
                if (Build.VERSION.SDK_INT >= 24) {
                    theory.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)); // for 24 api and more
                    editTheoryText.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    theory.setText(Html.fromHtml(text)); // or for older api
                    editTheoryText.setText(Html.fromHtml(text));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        if(role.equals("student"))
            editheoryFAB.setVisibility(View.GONE);

        editheoryFAB.setOnClickListener(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            editheoryFAB.setImageDrawable(getResources().getDrawable(R.drawable.ic_mode_edit_white_24dp,getActivity().getTheme()));
        else
            editheoryFAB.setImageDrawable(getResources().getDrawable(R.drawable.ic_mode_edit_white_24dp));


        noteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) //για κάθε παιδί της αναφοράς
                {

                    key = postSnapshot.getKey();
                    Log.d("ELELE",key);
                    Note note = postSnapshot.getValue(Note.class);

                    if(getActivity()!=null)
                    {
                        ImageView iv = new ImageView(getActivity());
                        iv.setClickable(true);
                        iv.setBackgroundResource(R.drawable.note);
                        iv.setTag(key);

                        iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {


                                noteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String note = String.valueOf(dataSnapshot.child(v.getTag().toString()).child("note").getValue());
                                        String author = String.valueOf(dataSnapshot.child(v.getTag().toString()).child("author").getValue());
                                        noteLayout.setVisibility(View.VISIBLE);
                                        noteTV.setText(note);
                                        noteAuthorTV.setText(author);



                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        });


                        //iv.setId(imageViewID);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
                        params.leftMargin = Math.round(Float.parseFloat(note.getCoordinatesX()) - 10);
                        params.topMargin = Math.round(Float.parseFloat(note.getCoordinatesY()) - 20);
                        theoryLayout.addView(iv, params);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        return view;
    }



    @Override
    public void onClick(View v) {
        if(v== editheoryFAB)
        {
            if(!editmode) //we disable the adding new note function
            {
                addNoteBtn.setVisibility(View.GONE); //hide addnote and the displayed note
                noteLayout.setVisibility(View.GONE);

                for(int i = 2; i < theoryLayout.getChildCount();i++)  //hide the notes
                {
                    theoryLayout.getChildAt(i).setVisibility(View.INVISIBLE);
                }

                theory.setVisibility(View.GONE);
                editTheoryText.setVisibility(View.VISIBLE);



                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    editheoryFAB.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_white_24dp, getActivity().getTheme()));
                } else {
                    editheoryFAB.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_white_24dp));
                }
                editmode = true;
            }
            else    //we enable the adding new note function
            {
                addNoteBtn.setVisibility(View.VISIBLE); //show addnote and the displayed note

                for(int i = 2; i < theoryLayout.getChildCount();i++) //show the notes
                {
                    theoryLayout.getChildAt(i).setVisibility(View.VISIBLE);
                }

                theory.setVisibility(View.VISIBLE);
                editTheoryText.setVisibility(View.GONE);


                if (Build.VERSION.SDK_INT >= 24)
                {
                    mDatabaseTheory.child("Text").setValue(Html.toHtml(editTheoryText.getText(), Html.FROM_HTML_MODE_LEGACY));
                }
                else
                {
                    mDatabaseTheory.child("Text").setValue(Html.toHtml(editTheoryText.getText()));
                }





                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    editheoryFAB.setImageDrawable(getResources().getDrawable(R.drawable.ic_mode_edit_white_24dp,getActivity().getTheme()));
                else
                    editheoryFAB.setImageDrawable(getResources().getDrawable(R.drawable.ic_mode_edit_white_24dp));
                editmode = false;
            }

        }
        else if(v==hideNoteBtn)
        {
            noteLayout.setVisibility(View.INVISIBLE);
        }
        else if(v==addNoteBtn)
        {
            if(istoadd)
            {
                Toast.makeText(getActivity(), "Select where you'd like to add your note!", Toast.LENGTH_SHORT).show();
                editheoryFAB.setVisibility(View.INVISIBLE); //disable the function of editing theory
                noteLayout.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.VISIBLE);
                noteEditText.setText(null);
                noteEditText.setVisibility(View.VISIBLE);
                addNoteBtn.setText("Save");
                addNoteBtn.setVisibility(View.GONE);
                theoryLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        addNoteBtn.setVisibility(View.VISIBLE);
                        coordX = event.getX();
                        coordY = event.getY();

                        ImageView iv = new ImageView(getActivity());
                        iv.setBackgroundResource(R.drawable.mark);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(25, 30);
                        params.leftMargin = Math.round(coordX-10);
                        params.topMargin = Math.round(coordY-20);
                        theoryLayout.addView(iv, params);

                        theoryLayout.setOnTouchListener(null);

                        /*ImageView mark = new ImageView(getActivity());
                        mark.setBackgroundResource(R.drawable.mark);

                        linearLayout.addView(mark);
                        mark.setX(coordX);
                        mark.setY(coordY);
                        mark.getLayoutParams().width = 20;
                        mark.getLayoutParams().height = 20;*/

//                        cont.setVisibility(View.VISIBLE);
//                        cancel.setVisibility(View.VISIBLE);
//                        cont.setText(null);
                        return true;
                    }
                });
                istoadd = false;
            }
            else //if is to save
            {
                if(!TextUtils.isEmpty(noteEditText.getText().toString())) {
                    if (theoryLayout.getChildCount() > 2) {

                        theoryLayout.removeViewAt(theoryLayout.getChildCount() - 1);
                    }

                    final String coordinatesX = String.valueOf(coordX);
                    final String coordinatesY = String.valueOf(coordY);
                    final String note = String.valueOf(noteEditText.getText());
                    String authorID = user.getUid();

                    userRef = FirebaseDatabase.getInstance().getReference("USERS").child(authorID);


                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String authorUsername = String.valueOf(dataSnapshot.child("username").getValue());

                            Note noteObj = new Note(coordinatesX, coordinatesY, note, authorUsername);
                            noteRef.push().setValue(noteObj);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                    if(role.equals("teacher"))
                        editheoryFAB.setVisibility(View.VISIBLE); //enable the function of editing theory
                    theoryLayout.setOnTouchListener(null);
                    cancelBtn.setVisibility(View.GONE);
                    noteEditText.setVisibility(View.GONE);
                    addNoteBtn.setText("Add Note");


                    istoadd = true;
                }
                else
                {
                    Toast toast = Toast.makeText(getActivity(), " You can't save an empty note ", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.setBackgroundResource(R.drawable.round_button);
                    TextView text = (TextView) view.findViewById(android.R.id.message);

/*Here you can do anything with above textview like text.setTextColor(Color.parseColor("#000000"));*/
                    toast.show();
                }

            }
        }
        else if(v==cancelBtn)
        {
            if(theoryLayout.getChildCount() > 2) {
                if(addNoteBtn.getVisibility() == View.VISIBLE) {   //if we have set a point for the note <=> saveBtn is visible
                    theoryLayout.removeViewAt(theoryLayout.getChildCount() - 1); //remove the point
                }
            }

            theoryLayout.setOnTouchListener(null);

            if(addNoteBtn.getVisibility() == View.GONE)
                addNoteBtn.setVisibility(View.VISIBLE);

            noteLayout.setVisibility(View.GONE);
            if(role.equals("teacher"))
                editheoryFAB.setVisibility(View.VISIBLE); //enable the function of editing theory
            cancelBtn.setVisibility(View.GONE);
            noteEditText.setVisibility(View.GONE);
            addNoteBtn.setText("Add Note");

            istoadd = true;
        }


    }
}
