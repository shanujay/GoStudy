package com.techcools.gostudy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class CompletedTaskFragment extends Fragment {

    RecyclerView recyclerViewCompletedTask;
    private TaskAdapter taskAdapter;
    private ArrayList<TaskModel> taskArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_completed_task, container, false);

        recyclerViewCompletedTask = v.findViewById(R.id.recycleView_completedTask);

        recyclerViewCompletedTask.setHasFixedSize(true);
        recyclerViewCompletedTask.setLayoutManager(new LinearLayoutManager(getContext()));

        taskArrayList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskArrayList);
        recyclerViewCompletedTask.setAdapter(taskAdapter);

        loadCompletedTasksFromFireStore();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Method to Load tasks every time the fragment is resumed
        loadCompletedTasksFromFireStore();
    }

    // Load Task From FireStore
    private void loadCompletedTasksFromFireStore() {
        DbQuery.getCompletedTaskDataList(new TaskDataListCompleteListener() {
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