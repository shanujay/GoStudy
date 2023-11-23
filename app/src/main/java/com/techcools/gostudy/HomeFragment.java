package com.techcools.gostudy;



import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.Calendar;


public class HomeFragment extends Fragment {

    Button profile;
    Button notification;

    TextView welcome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);

        profile = v.findViewById(R.id.profileHome);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserProfile.class);
                startActivity(intent);

            }
        });


        // Notification Button
        notification = v.findViewById(R.id.Notifications);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NotificationSettings.class);
                startActivity(intent);
            }
        });

        //welcome text
        welcome = v.findViewById(R.id.welcome);

        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        // Update the welcome text based on the time of day
        if (hourOfDay >= 0 && hourOfDay < 12) {
            welcome.setText("Good Morning...");
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            welcome.setText("Good Afternoon...");
        } else if (hourOfDay >= 18 && hourOfDay < 21) {
            welcome.setText("Good Evening...");
        } else {
            welcome.setText("Good Night...");
        }

        return v;

    }
}