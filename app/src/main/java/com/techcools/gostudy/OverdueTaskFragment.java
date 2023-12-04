package com.techcools.gostudy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class OverdueTaskFragment extends Fragment {

    RecyclerView recyclerViewOverdueTask;
    private TaskAdapter taskAdapter;
    private ArrayList<TaskModel> taskArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_overdue_task, container, false);

        recyclerViewOverdueTask = v.findViewById(R.id.recycleView_overdueTask);

        recyclerViewOverdueTask.setHasFixedSize(true);
        recyclerViewOverdueTask.setLayoutManager(new LinearLayoutManager(getContext()));

        taskArrayList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskArrayList);
        recyclerViewOverdueTask.setAdapter(taskAdapter);

        loadOverdueTasksFromFireStore();

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        // Method to Load tasks every time the fragment is resumed
        loadOverdueTasksFromFireStore();
    }

    // Load Task From FireStore
    private void loadOverdueTasksFromFireStore() {
        DbQuery.getOverdueTaskDataList(new TaskDataListCompleteListener() {
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

        // Include only overdue tasks
        for (TaskModel task : tasks) {
            if (task.isOverdue()) {
                taskArrayList.add(task);
            }
        }

        taskAdapter.notifyDataSetChanged();
    }

}