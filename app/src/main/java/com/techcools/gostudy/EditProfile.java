package com.techcools.gostudy;

import static com.techcools.gostudy.Register.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    EditText name, username, email;
    TextView enableEdit;
    String nameStr, usernameStr;
    ImageButton saveProfile, cancelEdit;
    CircleImageView profilePhoto;

    private Uri ImageUri;
    private Bitmap bitmap;
    FirebaseStorage storage;
    StorageReference mStorageRef;
    String photoUrl;
    FirebaseFirestore fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Instance for Firebase Storage
        fireStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();

        name = findViewById(R.id.editName);
        username = findViewById(R.id.editUserName);
        email = findViewById(R.id.editUserEmail);
        saveProfile = findViewById(R.id.saveProfile);
        cancelEdit = findViewById(R.id.cancelEdit);
        enableEdit = findViewById(R.id.enableEditProfile);

        String nameEditProfile = DbQuery.myProfile.getName();
        name.setText(nameEditProfile);

        String usernameEditProfile = DbQuery.myProfile.getUsername();
        username.setText(usernameEditProfile);

        String emailEditProfile = DbQuery.myProfile.getEmail();
        email.setText(emailEditProfile);

        profilePhoto = findViewById(R.id.profilePhoto_editProfile);
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePhotoDialog();
            }
        });

        disableEditing();
        loadProfileImage();

        enableEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableEditing();
            }
        });

        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validate();
                saveData();
                uploadImage();
            }
        });

        cancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableEditing();
                navigateUserProfile();
            }
        });
    }

    /** Disable Editing Method **/
    private void disableEditing() {
        name.setEnabled(false);
        username.setEnabled(false);
        email.setEnabled(false);
        saveProfile.setEnabled(false);
        profilePhoto.setEnabled(false);
    }

    /** Enable Editing Method **/
    private void enableEditing() {
        name.setEnabled(true);
        email.setEnabled(false);
        username.setEnabled(true);
        saveProfile.setEnabled(true);
        profilePhoto.setEnabled(true);

    }

    /** Save Data **/
    public void saveData() {

        DbQuery.saveProfileData(nameStr, usernameStr, new AppCompleteListener() {
            @Override
            public void onSuccess() {

                Toast.makeText(EditProfile.this, "Updated Successfully",
                        Toast.LENGTH_SHORT).show();

                disableEditing();
            }

            @Override
            public void onFailure() {

                Toast.makeText(EditProfile.this, "Something Went Wrong ! Please Try Again Later.",
                        Toast.LENGTH_SHORT).show();

            }
        });

    }

    /** Validate Data **/
    public void validate() {
        nameStr = name.getText().toString();
        usernameStr = username.getText().toString();

        if (nameStr.isEmpty()) {
            name.setError("Name Cannot Be Empty !");
        }

        if (usernameStr.isEmpty()) {
            username.setError("Username Cannot Be Empty ! ");
        }
    }

    /** Profile Photo Dialog **/
    public void profilePhotoDialog(){

        ConstraintLayout changeProfileConstraintLayout = findViewById(R.id.changeProfileConstraintLayout);
        View view = LayoutInflater.from(EditProfile.this).inflate(R.layout.change_profile_dialog, changeProfileConstraintLayout);
        Button change = view.findViewById(R.id.change);
        Button cancelChange = view.findViewById(R.id.cancel_change);

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        // Profile Photo Change Button
        change.findViewById(R.id.change).setOnClickListener(v -> {
            alertDialog.dismiss();
            checkStoragePermission();


        });

        // Cancel Button
        cancelChange.findViewById(R.id.cancel_change).setOnClickListener(v -> {
            alertDialog.dismiss();
            Toast.makeText(EditProfile.this, "Cancel", Toast.LENGTH_SHORT).show();
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

    /** Check Permission For External Storage **/
    public void checkStoragePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                chooseProfilePhoto();
            }
        } else {
            chooseProfilePhoto();
        }
    }

    /** Choose Profile Photo Method **/
    private void chooseProfilePhoto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        //Launcher
        launcher.launch(intent);
    }

    ActivityResultLauncher<Intent> launcher
            =registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    if (data != null && data.getData() != null){

                        ImageUri = data.getData();

                        //Converting image into bitmap
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(),
                                    ImageUri
                            );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //Set Image into Image View
                    if (ImageUri != null) {
                        profilePhoto.setImageBitmap(bitmap);
                    }
                }
            }
    );

    /**Upload Image into Firebase Storage in Store Image Uri into Firebase FireStore
     *  and Method for Upload Image into Firebase Storage **/
    private void uploadImage() {

        // Check ImageUri
        if (ImageUri != null) {

            FirebaseStorage storage = FirebaseStorage.getInstance();

            // Create Storage Instances
            final StorageReference myRef = mStorageRef.child("UserProfile/"+ ImageUri.getLastPathSegment());
            myRef.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get Download Url to Store in String
                    myRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (uri != null){
                                photoUrl = uri.toString();
                                saveProfileImageToFirestore(photoUrl); // Save the profile image URL to Firestore
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /** Save Profile Image to FireStore **/
    private void saveProfileImageToFirestore(String imageUrl) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference userRef = fireStore.collection("ProfilePhoto").document(userId);

        Map<String, Object> profileData = new HashMap<>();
        profileData.put("profileImageUrl", imageUrl);
        profileData.put("userEmail", userEmail);

        userRef.set(profileData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Photo upload and Firestore update successful
                        Toast.makeText(EditProfile.this, "Photo uploaded successfully", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Firestore update failed
                        Toast.makeText(EditProfile.this, "Failed to upload photo !", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    /** Load Profile Image **/
    public void loadProfileImage() {

        // Get the current user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get a reference to the user document in FireStore
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference userRef = firebaseFirestore.collection("ProfilePhoto").document(userId);

        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    // Get the profileImageUrl field value from the document
                    String profileImageUrl = snapshot.getString("profileImageUrl");

                    if (profileImageUrl != null) {
                        // Load the image into the profilePhoto CircleImageView using Picasso
                        Picasso.get().load(profileImageUrl).into(profilePhoto);
                    }
                }
            }
        });
    }

    /** Navigate to User Profile **/
    public void navigateUserProfile() {
        Intent intent = new Intent(EditProfile.this, UserProfile.class);
        startActivity(intent);
        EditProfile.this.finish();
    }
}