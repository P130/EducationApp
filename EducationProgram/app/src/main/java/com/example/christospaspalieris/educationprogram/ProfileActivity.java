package com.example.christospaspalieris.educationprogram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity  implements View.OnClickListener {


    private static final String TAG = "ProfileActivity";

    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST_CODE = 2;


    private FirebaseAuth mAuth;
    private DatabaseReference mdbReference;
    private StorageReference mStorageReference;
    private FirebaseUser user;

    private TextView profile_label;
    private TextView firstName;
    private TextView lastName;
    private TextView Age;

    private EditText firstname;
    private EditText lastname;
    private EditText oldpass;
    private EditText newpass;
    private EditText age;

    private ImageButton profilephoto;

    private Button saveprofile;
    private Button editprofile;
    private Button canceledit;

    private boolean changedpass;
    private boolean editmode;
    UserInformation userInformation;

    private ToggleButton changepass;

    private ProgressDialog progressSave;
    private Typeface typeFace;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mStorageReference = FirebaseStorage.getInstance().getReference().child("Profile_images");
        mdbReference = FirebaseDatabase.getInstance().getReference("USERS").child(user.getUid());
       // LoadUserInfo();


        progressSave =  new ProgressDialog(ProfileActivity.this);

        typeFace= Typeface.createFromAsset(getAssets(),"fonts/ComicBook.otf");

        profile_label = (TextView)findViewById(R.id.TVprofile);
        firstName = (TextView)findViewById((R.id.TVfirstName));
        lastName = (TextView)findViewById((R.id.TVlastName));
        Age = (TextView)findViewById((R.id.TVage));

        firstname = (EditText) findViewById(R.id.ETfirstName);
        lastname = (EditText) findViewById(R.id.ETlastName);
        oldpass = (EditText) findViewById(R.id.EToldPass);
        newpass = (EditText) findViewById(R.id.ETnewPass);
        age = (EditText) findViewById(R.id.ETage);

        changepass = (ToggleButton) findViewById(R.id.changepassButton);

        profilephoto = (ImageButton) findViewById(R.id.imageDisplay);

        saveprofile = (Button)findViewById(R.id.saveProfile);
        editprofile = (Button)findViewById(R.id.editProfile);
        canceledit = (Button)findViewById(R.id.cancelEdit);

        profile_label.setTypeface(typeFace);
        firstName.setTypeface(typeFace);
        lastName.setTypeface(typeFace);
        Age.setTypeface(typeFace);
        firstname.setTypeface(typeFace);
        lastname.setTypeface(typeFace);
        age.setTypeface(typeFace);



        changepass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    oldpass.setVisibility(View.VISIBLE);
                    newpass.setVisibility(View.VISIBLE);
                }
                else
                {
                    oldpass.setVisibility(View.GONE);
                    newpass.setVisibility(View.GONE);
                    oldpass.setText("");
                    newpass.setText("");
                }
            }
        });

        editprofile.setOnClickListener(this);
        canceledit.setOnClickListener(this);
        saveprofile.setOnClickListener(this);
        profilephoto.setOnClickListener(this);
        LoadUserInfo();


    }

    @Override
    public void onClick(View v) {

        if(v==saveprofile)
        {
            progressSave.setMessage("Saving");
            progressSave.show();
            if(imageUri!=null) {
                mStorageReference.child(user.getUid()).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests")
                        String imageUri = taskSnapshot.getDownloadUrl().toString();
                        mdbReference.child("image").setValue(imageUri);
                    }
                });

            }
            else
                Toast.makeText(ProfileActivity.this, "Empty imageUri", Toast.LENGTH_SHORT).show();

            if(!TextUtils.isEmpty(firstname.getText().toString()))
            {
                mdbReference.child("firstName").setValue(firstname.getText().toString());
                Log.d(TAG,"Changed first name");
            }
            if(!TextUtils.isEmpty(lastname.getText().toString()))
            {
                mdbReference.child("lastName").setValue(lastname.getText().toString());
                Log.d(TAG,"Changed last name");
            }
            if(!TextUtils.isEmpty(age.getText().toString()))
            {
                mdbReference.child("age").setValue(age.getText().toString());
                Log.d(TAG,"Changed age");
            }

            if(TextUtils.isEmpty(firstname.getText().toString()) && TextUtils.isEmpty(lastname.getText().toString()) && TextUtils.isEmpty(age.getText().toString()))
                progressSave.dismiss();

            if(changepass.isChecked() && !TextUtils.isEmpty(oldpass.getText()) && !TextUtils.isEmpty(newpass.getText()))
            {
                if(newpass.getText().length()<6) {
                    Toast.makeText(ProfileActivity.this, "Password needs to have at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d(TAG + "old",String.valueOf(oldpass.getText()));
                Log.d(TAG,String.valueOf(userInformation.getPassword()));

                if(String.valueOf(oldpass.getText()).equals(userInformation.getPassword()))
                {
                    final String NewPassword = String.valueOf(newpass.getText());
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(userInformation.getEmail(), userInformation.getPassword());

                    Log.d(TAG,NewPassword);
                    // Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(NewPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    mdbReference.child("password").setValue(NewPassword);
                                                    Toast.makeText(ProfileActivity.this,"Your password was successfully changed", Toast.LENGTH_SHORT).show();
                                                    Log.d(TAG,"Changed password");
                                                } else {
                                                    Toast.makeText(ProfileActivity.this,"An error occurred while updating your password", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Log.d(TAG, "Error auth failed");
                                    }
                                }
                            });
                }

                editmode = false;
                firstName.setVisibility(View.VISIBLE);
                lastName.setVisibility(View.VISIBLE);
                Age.setVisibility(View.VISIBLE);
                editprofile.setVisibility(View.VISIBLE);


                canceledit.setVisibility(View.GONE);
                canceledit.setVisibility(View.GONE);
                firstname.setVisibility(View.GONE);
                lastname.setVisibility(View.GONE);
                age.setVisibility(View.GONE);
                changepass.setVisibility(View.GONE);
                oldpass.setVisibility(View.GONE);
                newpass.setVisibility(View.GONE);
                saveprofile.setVisibility(View.GONE);

                changepass.setChecked(false);
            }


        }

        if(v==profilephoto)
        {
            if(editmode)
            {
                PopupMenu popup = new PopupMenu(ProfileActivity.this, profilephoto);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.menu_list, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Browse")) {
                            Intent galleryIntent = new Intent();
                            galleryIntent.setAction(Intent.ACTION_PICK);
                            galleryIntent.setType("image/*");
                            startActivityForResult(galleryIntent, GALLERY_REQUEST);
                        } else if (item.getTitle().equals("Take a Selfie")) {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(ProfileActivity.this.getPackageManager()) != null) {
                                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                            }
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        }

        if(v==editprofile)
        {
            editmode = true;
            firstName.setVisibility(View.GONE);
            lastName.setVisibility(View.GONE);
            Age.setVisibility(View.GONE);
            editprofile.setVisibility(View.GONE);

            canceledit.setVisibility(View.VISIBLE);
            firstname.setVisibility(View.VISIBLE);
            lastname.setVisibility(View.VISIBLE);
            age.setVisibility(View.VISIBLE);
            changepass.setVisibility(View.VISIBLE);
            saveprofile.setVisibility(View.VISIBLE);
        }

        if(v==canceledit)
        {
            editmode = false;
            firstName.setVisibility(View.VISIBLE);
            lastName.setVisibility(View.VISIBLE);
            Age.setVisibility(View.VISIBLE);
            editprofile.setVisibility(View.VISIBLE);


            canceledit.setVisibility(View.GONE);
            canceledit.setVisibility(View.GONE);
            firstname.setVisibility(View.GONE);
            lastname.setVisibility(View.GONE);
            age.setVisibility(View.GONE);
            changepass.setVisibility(View.GONE);
            oldpass.setVisibility(View.GONE);
            newpass.setVisibility(View.GONE);
            saveprofile.setVisibility(View.GONE);

            changepass.setChecked(false);
        }

    }

    void LoadUserInfo()
    {

        mdbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    userInformation= dataSnapshot.getValue(UserInformation.class);
                    Picasso.with(ProfileActivity.this).load(userInformation.getImage()).fit().into(profilephoto);
                    firstName.setText(userInformation.getFirstName());
                    lastName.setText(userInformation.getLastName());
                    Age.setText(userInformation.getAge());
                    firstname.setHint(userInformation.getFirstName());
                    lastname.setHint(userInformation.getLastName());
                    age.setHint(userInformation.getAge());

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"Is paused");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressSave.dismiss();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),EducationalProgramActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK)
        {
            imageUri = data.getData();
            Picasso.with(ProfileActivity.this).load(imageUri).noPlaceholder().centerCrop().fit().into(profilephoto);

        }
        else if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK)
        {
            imageUri = data.getData();
            Picasso.with(ProfileActivity.this).load(imageUri).noPlaceholder().centerCrop().fit().into(profilephoto);
        }

    }


}
