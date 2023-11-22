package com.techcools.gostudy;

import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;

public class DbQuery {


    public static FirebaseFirestore g_firestore;
    public static ProfileModel myProfile = new ProfileModel("NA", null, "NA");

    public static void createUserData(String name, String email, String userName, AppCompleteListener completeListener) {

        // Store User Details in Firebase FireStore When User Register
        Map<String,Object> user = new HashMap<>();
        user.put("fullName", name);
        user.put("userEmail", email);
        user.put("userName", userName);

        DocumentReference userDoc = g_firestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        WriteBatch batch = g_firestore.batch();

        batch.set(userDoc, user);

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {

                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();

                    }
                });
    }

    public static void getUserData(AppCompleteListener completeListener) {

        g_firestore.collection("Users").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        myProfile.setName(documentSnapshot.getString("fullName"));
                        myProfile.setEmail(documentSnapshot.getString("userEmail"));
                        myProfile.setUsername(documentSnapshot.getString("userName"));

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }



    public static void loadData(AppCompleteListener completeListener) {

        getUserData(completeListener);

    }

    public static void saveProfileData(String name, String username, AppCompleteListener completeListener) {

        Map<String, Object> profileData = new ArrayMap<>();

        profileData.put("fullName", name);
        profileData.put("userName", username);

        g_firestore.collection("Users").document(FirebaseAuth.getInstance().getUid())
                .update(profileData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        myProfile.setName(name);
                        myProfile.setUsername(username);

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });

    }


}
