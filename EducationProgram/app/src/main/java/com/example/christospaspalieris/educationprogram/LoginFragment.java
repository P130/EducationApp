package com.example.christospaspalieris.educationprogram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by User on 2/28/2017.
 */

public class LoginFragment extends Fragment  {
    private static final String TAG = "LoginFragment";

    private FirebaseAuth mAuth;
    private DatabaseReference dbReference;

    EditText email,password;
    Button LoginBtn;
    private ProgressDialog progressLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment,container,false);

        mAuth = FirebaseAuth.getInstance();

        dbReference = FirebaseDatabase.getInstance().getReference("USERS");

        email = (EditText) view.findViewById(R.id.EmailAddress);
        password = (EditText) view.findViewById(R.id.TextPassword);

        LoginBtn=(Button)view.findViewById(R.id.buttonSiginIn);

        progressLogin = new ProgressDialog(getActivity());

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressLogin.setMessage("Signing In");
                progressLogin.show();
                checkLogin();
            }
        });





        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }




    private void checkLogin() {
        String getemail = email.getText().toString().trim();
        String getpassword = password.getText().toString().trim();



        if(!TextUtils.isEmpty(getemail)&&!TextUtils.isEmpty(getpassword)){
            mAuth.signInWithEmailAndPassword(getemail,getpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        checkUserExist();
                        progressLogin.dismiss();
                    }else{
                        Toast.makeText(getActivity(),"Error while Login",Toast.LENGTH_LONG).show();
                        progressLogin.dismiss();
                    }
                }
            });
        }
    }

    private void checkUserExist() {
        final String user_id = mAuth.getCurrentUser().getUid();
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){
                    Intent educationIntent = new Intent(getContext(),EducationalProgramActivity.class);
                    educationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(educationIntent);
                }else {
                    Toast.makeText(getActivity(),"You have to setup your account",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
