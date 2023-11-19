package com.techcools.gostudy;

import static com.techcools.gostudy.Register.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    TextView usernameUserProfile, emailUserProfile;
    CardView editProfile, editNotifications, editRemainder, editTimer, editTheme, helpCenter, passwordReset, logOut;
    FirebaseFirestore firebaseFirestore;
    CircleImageView profilePhoto;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DocumentReference userRef;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        usernameUserProfile = findViewById(R.id.userNameProfile);
        emailUserProfile = findViewById(R.id.userEmailProfile);

        String nameProfile = DbQuery.myProfile.getUsername();
        usernameUserProfile.setText(nameProfile);

        String emailProfile = DbQuery.myProfile.getEmail();
        emailUserProfile.setText(emailProfile);

        profilePhoto = findViewById(R.id.profilePhoto_UserProfile);

        if (user != null) {
            // Load profile image
            loadProfileImage();
        }

        // Edit Profile
        editProfile = findViewById(R.id.editProfileButton);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateEditProfile();
            }
        });

        // Edit Notifications
        editNotifications = findViewById(R.id.editNotifications);

        // Edit Study Remainders
        editRemainder = findViewById(R.id.editStudyRemainders);
        editRemainder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateStudyRemainder();
            }
        });

        // Edit App Theme
        editTheme = findViewById(R.id.editAppTheme);
        editTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateAppThemeSettings();
            }
        });

        // Edit TImer Settings
        editTimer = findViewById(R.id.editTimerSettings);
        editTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateTimerSettings();
            }
        });

        // Help Center
        helpCenter = findViewById(R.id.helpCenter);
        helpCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateHelpCenter();
            }
        });

        // Edit Password
        passwordReset = findViewById(R.id.editResetPassword);
        passwordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigatePasswordReset();
            }
        });

        // Log Out
        logOut = findViewById(R.id.logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutDialog();
            }
        });
    }


    /** Navigate to Edit Profile **/
    private void navigateEditProfile() {
        Intent intent = new Intent(UserProfile.this, EditProfile.class);
        startActivity(intent);
        finish();
    }

    /** Log Out Dialog **/
    private void logOutDialog() {
        ConstraintLayout logOutConstraintLayout = findViewById(R.id.logOutConstraintLayout);
        View view = LayoutInflater.from(UserProfile.this).inflate(R.layout.log_out_dialog, logOutConstraintLayout);
        Button logOut = view.findViewById(R.id.logOut);
        Button cancelLogOut = view.findViewById(R.id.cancel_log_out);

        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        // Log Out Button
        logOut.findViewById(R.id.logOut).setOnClickListener(v -> {
            alertDialog.dismiss();

            //Log Out From Account
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(UserProfile.this, "Signed Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserProfile.this, LogIn.class);
            startActivity(intent);
            UserProfile.this.finish();

        });

        // Cancel Button
        cancelLogOut.findViewById(R.id.cancel_log_out).setOnClickListener(v -> {
            alertDialog.dismiss();
            Toast.makeText(UserProfile.this, "Cancel", Toast.LENGTH_SHORT).show();
        });

        alertDialog.show();
    }

    /** Navigate to Password Reset Activity **/
    public void navigatePasswordReset() {
        Intent intent = new Intent(UserProfile.this, ForgetPasswordEmail.class);
        startActivity(intent);
    }

    /** Navigate to Help Center Activity **/
    public void navigateHelpCenter() {
        Intent intent = new Intent(UserProfile.this, HelpCenterActivity.class);
        startActivity(intent);
    }

    /** navigate to Timer Settings Activity **/
    public void navigateTimerSettings() {
        Intent intent = new Intent(UserProfile.this, TimerSettings.class);
        startActivity(intent);
    }

    /** Navigate to Study Remainder Activity **/
    public void navigateStudyRemainder() {
        Intent intent = new Intent(UserProfile.this, StudyRemainder.class);
        startActivity(intent);
    }

    /** Navigate to App Theme Settings Activity **/
    public void navigateAppThemeSettings() {
        Intent intent = new Intent(UserProfile.this, AppThemeSettings.class);
        startActivity(intent);
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
}