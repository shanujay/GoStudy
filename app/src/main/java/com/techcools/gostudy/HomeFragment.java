package com.techcools.gostudy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class HomeFragment extends Fragment {

    ImageButton pomodoroTimerBtn;
    ImageButton studyMethodBtn;
    ImageButton UpcomingTaskBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        pomodoroTimerBtn = (ImageButton) view.findViewById(R.id.Pomodoro_timerBtn);
        pomodoroTimerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }
}

