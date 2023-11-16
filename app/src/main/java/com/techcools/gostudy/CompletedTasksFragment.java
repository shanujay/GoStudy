package com.techcools.gostudy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class CompletedTasksFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_tasks, container, false);

        // Replace the following TextView with your RecyclerView or any other UI component
        TextView completedTasksTextView = view.findViewById(R.id.completedTasksTextView);
        // Get your completed tasks data and set it to the TextView (replace this with your actual data)
        completedTasksTextView.setText("List of completed tasks:\n1. Task 1\n2. Task 2\n3. Task 3");

        return view;
    }
}
