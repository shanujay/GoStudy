package com.techcools.gostudy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class ProgressFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        // Find the CardViews by their IDs
        CardView completeTasksCard = view.findViewById(R.id.cardComTasks);
        CardView pendingTasksCard = view.findViewById(R.id.cardPenTasks);
        CardView progressStatisticsCard = view.findViewById(R.id.reports);

        // Set click listeners for each CardView
        completeTasksCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click for Complete Tasks
                showCompletedTasks();
            }
        });

        pendingTasksCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click for Pending Tasks
                showPendingTasks();
            }
        });

        progressStatisticsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click for Progress Statistics
                showProgressStatistics();
            }
        });

        return view;
    }

    private void showCompletedTasks() {
        // Create and show the CompletedTasksFragment
        CompletedTasksFragment completedTasksFragment = new CompletedTasksFragment();
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, completedTasksFragment)
                .addToBackStack(null)  // Optional: Add to back stack for navigation
                .commit();
    }

    private void showPendingTasks() {
        // Create and show the PendingTasksFragment
        PendingTasksFragment pendingTasksFragment = new PendingTasksFragment();
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, pendingTasksFragment)
                .addToBackStack(null)  // Optional: Add to back stack for navigation
                .commit();
    }


    private void showProgressStatistics() {
        // Create and show the ProgressStatisticsFragment
        ProgressStatisticsFragment progressStatisticsFragment = new ProgressStatisticsFragment();
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, progressStatisticsFragment)
                .addToBackStack(null)  // Optional: Add to back stack for navigation
                .commit();
    }
}
