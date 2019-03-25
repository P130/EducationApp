package com.example.christospaspalieris.educationprogram;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by User on 2/28/2017.
 */

public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";

    private Button buttonRegister;
    private EditText editTextEmail,editTextPassword,editTextUserName,editTextFirstName,editTextLastName, editTextAge;
    private RadioButton male,female;
    private RadioGroup choice_sex;
    private CheckBox teacher;
    private String sex;
    private Uri profile_pic;

    private ProgressDialog mProgress;


    private FirebaseAuth mAuth;
    private DatabaseReference dbReference;
    private StorageReference mStorageImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment,container,false);

        buttonRegister=(Button)view.findViewById(R.id.buttonRegister);

        editTextUserName = (EditText)view.findViewById(R.id.editTextUserName);
        editTextFirstName = (EditText)view.findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText)view.findViewById(R.id.editTextLastName);
        editTextEmail = (EditText)view.findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)view.findViewById(R.id.editTextPassword);
        editTextAge = (EditText)view.findViewById(R.id.editAge);

        male = (RadioButton)view. findViewById(R.id.radiomale);
        female = (RadioButton) view.findViewById(R.id.radiofemale);
        choice_sex = (RadioGroup) view.findViewById(R.id.radiogroup);

        teacher = (CheckBox) view.findViewById(R.id.teacher_cb);


        mProgress = new ProgressDialog(getActivity());



        mAuth = FirebaseAuth.getInstance();


        dbReference = FirebaseDatabase.getInstance().getReference("USERS");
        mStorageImage = FirebaseStorage.getInstance().getReference("Profile_images");

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        return view;
    }

/*
    public Uri getImageUri(Context inContext, Bitmap photo) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), photo, "Title", null);
        return Uri.parse(path);
    }
    */
    private void registerUser() {

        String username = editTextUserName.getText().toString().trim();
        String FirstName = editTextFirstName.getText().toString().trim();
        String LastName = editTextLastName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(getActivity(),"Please enter username", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(FirstName)){
            Toast.makeText(getActivity(),"Please enter first name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(LastName)){
            Toast.makeText(getActivity(),"Please enter last name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(email)){
            Toast.makeText(getActivity(),"Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(getActivity(),"Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(age)){
            Toast.makeText(getActivity(),"Please enter your age", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!male.isChecked() && !female.isChecked())
        {
            Toast.makeText(getActivity(),"Please choose your gender", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgress.setMessage("Registering User and\nSigning In");
        mProgress.show();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    SaveUserInfo();

                    mProgress.dismiss();

                    Intent educationIntent = new Intent(getActivity(), EducationalProgramActivity.class);
                    educationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(educationIntent);



                }
                else {
                    Toast.makeText(getActivity()
                            ,"Error while login", Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();
                }

            }
        });



    }

    private void SaveUserInfo(){
        String username = editTextUserName.getText().toString().trim();
        String firstname = editTextFirstName.getText().toString().trim();
        String lastname = editTextLastName.getText().toString().trim();
        String email_address = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String default_image = "https://firebasestorage.googleapis.com/v0/b/my-projectsdb.appspot.com/o/Profile_images%2Fdefault_profile_icon.png?alt=media&token=58f16c42-f3c9-4f23-8e29-cbd462f4dc2d";
        String role = teacher.isChecked() ? "teacher" : "student";

        if(male.isChecked())
            sex = "male";
        if(female.isChecked())
            sex = "female";

        UserInformation userInformation = new UserInformation(username,firstname,lastname,email_address,password,age,sex,default_image,role);
        FirebaseUser user = mAuth.getCurrentUser();

        dbReference.child(user.getUid()).setValue(userInformation);

    }
}
