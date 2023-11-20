package com.techcools.gostudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;



public class AddPostActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private Button buttonSavePost, buttonCancel, buttonAddPost;
    private Bitmap capturedImageBitmap;
    private FirebaseFirestore db;
    ImageView userPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        // Initialize FireStore
        db = FirebaseFirestore.getInstance();

        // Initialize buttons and image view
        buttonSavePost = findViewById(R.id.buttonSavePost);
        buttonCancel = findViewById(R.id.buttonCancelPost);
        buttonAddPost = findViewById(R.id.addPost_Button);
        userPost = findViewById(R.id.addPost_imageView);

        // Set click listener for adding a post
        buttonAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if camera permission is granted
                if (ContextCompat.checkSelfPermission(AddPostActivity.this, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    // Open the camera
                    openCamera();
                } else {
                    // Request camera permission
                    ActivityCompat.requestPermissions(AddPostActivity.this,
                            new String[]{android.Manifest.permission.CAMERA},
                            REQUEST_CAMERA_PERMISSION);
                }
            }
        });

        // Set click listener for saving the post
        buttonSavePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Upload the photo to FireStore and finish the activity
                uploadPhotoToFireStore();
                finish();
            }
        });


    }

    /** Method to open the camera for capturing an image **/
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Start the camera activity
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /** Method called when the camera activity returns a result **/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Get the captured image bitmap and display it in the image view
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            capturedImageBitmap = (Bitmap) extras.get("data");
            userPost.setImageBitmap(capturedImageBitmap);
        }
    }

    /** Method called when a permission is requested **/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If camera permission is granted, open the camera
                openCamera();
            } else {
                // Camera permission denied, show a toast message
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /** Method to upload the photo to FireStore **/
    private void uploadPhotoToFireStore() {
        if (capturedImageBitmap != null) {
            // Convert the Bitmap to a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            capturedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageData = baos.toByteArray();

            // Create a reference to the Firebase Storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            // Generate a unique ID for the photo
            String photoId = UUID.randomUUID().toString();
            String photoPath = "Community/" + photoId + ".jpg";
            StorageReference photoRef = storageRef.child(photoPath);

            // Upload the photo to Firebase Storage
            UploadTask uploadTask = photoRef.putBytes(imageData);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get the download URL of the uploaded photo
                    photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            // Store the download URL in FireStore
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            String photoUrl = downloadUri.toString();
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();


                            Map<String, Object> postData = new HashMap<>();
                            postData.put("userID", userId);
                            postData.put("userEmail", userEmail);
                            postData.put("postUrl", photoUrl);

                            db.collection("Community").document(photoId)
                                    .set(postData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Photo upload and FireStore update successful
                                            Toast.makeText(AddPostActivity.this, "Photo uploaded successfully", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // FireStore update failed
                                            Toast.makeText(AddPostActivity.this, "Failed to upload photo to FireStore", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed to get the download URL
                            Toast.makeText(AddPostActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Photo upload failed
                    Toast.makeText(AddPostActivity.this, "Failed to upload photo to Firebase Storage", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}