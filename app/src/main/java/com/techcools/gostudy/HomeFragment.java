package com.techcools.gostudy;



import static com.techcools.gostudy.Register.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {

    CircleImageView profileHome;
    ImageButton notificationHome;
    CardView pomodoroTimerHome, studyQuestionnaireHome, upcomingTaskHome, taskManagerHome, studyMusicHome;
    TextView textWelcome;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        profileHome = v.findViewById(R.id.profileIconHome);

        if (user != null) {
            // Load profile image
            loadProfileImage();
        }

        profileHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserProfile.class);
                startActivity(intent);

            }
        });


        // Notification Button
        notificationHome = v.findViewById(R.id.notificationHome);
        notificationHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NotificationSettings.class);
                startActivity(intent);
            }
        });

        //welcome text
        textWelcome = v.findViewById(R.id.textWelcome);

        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        // Update the welcome text based on the time of day
        if (hourOfDay >= 0 && hourOfDay < 12) {
            textWelcome.setText("Good Morning");
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            textWelcome.setText("Good Afternoon");
        } else if (hourOfDay >= 18 && hourOfDay < 21) {
            textWelcome.setText("Good Evening");
        } else {
            textWelcome.setText("Good Night");
        }

        //Pomodoro Timer
        pomodoroTimerHome = v.findViewById(R.id.pomodoroBtnHome);
        pomodoroTimerHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // timer intent
            }
        });

        // Upcoming Task
        upcomingTaskHome = v.findViewById(R.id.upcomingTaskBtnHome);
        upcomingTaskHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // upcoming task intent
            }
        });

        // Study Questionnaire
        studyQuestionnaireHome = v.findViewById(R.id.questionnaireBtnHome);
        studyQuestionnaireHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StudyMethodActivity.class);
                startActivity(intent);
            }
        });

        // Task Manager
        taskManagerHome = v.findViewById(R.id.taskBtnHome);
        taskManagerHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TaskManagerActivity.class);
                startActivity(intent);
            }
        });

        // Study Music
        studyMusicHome = v.findViewById(R.id.musicBtnHome);
        studyMusicHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // study music intent
            }
        });
        return v;

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
                        Picasso.get().load(profileImageUrl).into(profileHome);
                    }
                }
            }
        });
    }
}