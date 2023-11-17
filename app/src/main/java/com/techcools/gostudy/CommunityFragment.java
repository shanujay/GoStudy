package com.techcools.gostudy;

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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CommunityFragment extends Fragment {

    FloatingActionButton fabAddPost;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    ArrayList<UserPosts> userPostsArrayList;
    postAdapter postAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_community, container, false);

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

}