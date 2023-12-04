package com.techcools.gostudy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class UpcomingTaskFragment extends Fragment {

    RecyclerView recyclerViewUpcomingTask;
    private TaskAdapter taskAdapter;
    private ArrayList<TaskModel> taskArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_upcoming_task, container, false);

        recyclerViewUpcomingTask = v.findViewById(R.id.recycleView_upcomingTask);

        recyclerViewUpcomingTask.setHasFixedSize(true);
        recyclerViewUpcomingTask.setLayoutManager(new LinearLayoutManager(getContext()));

        taskArrayList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskArrayList);
        recyclerViewUpcomingTask.setAdapter(taskAdapter);

        loadTasksFromFireStore();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Method to Load tasks every time the fragment is resumed
        loadTasksFromFireStore();
    }

    // Load Task From FireStore
    private void loadTasksFromFireStore() {
        DbQuery.getTaskDataList(new TaskDataListCompleteListener() {
            @Override
            public void onSuccess(ArrayList<TaskModel> tasks) {
                updateRecyclerView(tasks);
            }

            @Override
            public void onFailure() {
                // failure
            }
        });
    }

    // Update Recycler View
    public void updateRecyclerView(ArrayList<TaskModel> tasks) {
        taskArrayList.clear();
        taskArrayList.addAll(tasks);
        taskAdapter.notifyDataSetChanged();
    }
}