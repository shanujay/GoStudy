package com.techcools.gostudy;

import static com.techcools.gostudy.Register.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunityFragment extends Fragment {

    CircleImageView profileCommunity;
    FloatingActionButton fabAddPost;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    ArrayList<UserPosts> userPostsArrayList;
    postAdapter postAdapter;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_community, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        profileCommunity = v.findViewById(R.id.profileIconCommunity);
        if (user != null) {
            // Load profile image
            loadProfileImage();
        }

        fabAddPost = v.findViewById(R.id.fabAddPost);
        recyclerView = v.findViewById(R.id.recycleView_community);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        userPostsArrayList = new ArrayList<UserPosts>();
        postAdapter = new postAdapter(getContext(), userPostsArrayList);

        recyclerView.setAdapter(postAdapter);

        eventChangeListener();

        fabAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Camera
                showAddPostDialog();
            }
        });

        return v;
    }

    private void showAddPostDialog() {
        Intent intent = new Intent(getActivity(), AddPostActivity.class);
        startActivity(intent);
    }

    private void eventChangeListener() {

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("Community")
                .whereEqualTo("userID", currentUserId)// Filter documents by userId
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null){

                            Log.e("Firestore Error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc: value.getDocumentChanges()) {

                            if (dc.getType() == DocumentChange.Type.ADDED) {

                                userPostsArrayList.add(dc.getDocument().toObject(UserPosts.class));
                            }

                            postAdapter.notifyDataSetChanged();

                        }

                    }
                });

    }

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
                        Picasso.get().load(profileImageUrl).into(profileCommunity);
                    }
                }
            }
        });
    }

}