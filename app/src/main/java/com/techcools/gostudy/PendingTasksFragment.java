package com.techcools.gostudy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class PendingTasksFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_tasks, container, false);

        // Replace the following TextView with your RecyclerView or any other UI component
        TextView pendingTasksTextView = view.findViewById(R.id.pendingTasksTextView);
        // Get your pending tasks data and set it to the TextView (replace this with your actual data)
        pendingTasksTextView.setText("List of pending tasks:\n1. Task 1\n2. Task 2\n3. Task 3");

        return view;
    }
}