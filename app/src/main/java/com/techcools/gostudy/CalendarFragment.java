package com.techcools.gostudy;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;


public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    private FloatingActionButton fabAddTask;
    RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private ArrayList<TaskModel> taskArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = v.findViewById(R.id.calendar_view);
        fabAddTask = v.findViewById(R.id.fabAddTask);
        recyclerView = v.findViewById(R.id.recycleView_task);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        taskArrayList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskArrayList);
        recyclerView.setAdapter(taskAdapter);

        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a dialog or another activity to add a goal
                showAddGoalDialog();
            }
        });

        loadTasksFromFireStore();

        // Add this block to enable swipe-to-delete
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Swipe-to-delete logic
                int position = viewHolder.getAdapterPosition();
                showDeleteTaskDialog(position);
            }


        };


        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


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

    // Show Add Goal Dialog
    private void showAddGoalDialog() {
        Intent intent = new Intent(getActivity(), AddTask.class);
        startActivity(intent);
    }

    // Show Delete Task Dialog
    private void showDeleteTaskDialog(final int position) {
        // Implement a dialog to confirm task deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete Task");
        builder.setMessage("Are you sure you want to delete this task?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTask(position);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User canceled the deletion
                taskAdapter.notifyItemChanged(position); // Notify to rebind the view
            }
        });
        builder.show();
    }

    // Delete Task
    private void deleteTask(int position) {
        // Delete task from Firestore and update the RecyclerView
        String taskId = taskArrayList.get(position).getTaskId();
        DbQuery.deleteTask(taskId, new AppCompleteListener() {
            @Override
            public void onSuccess() {
                // Task deleted successfully
                taskAdapter.removeTask(position); // Update the RecyclerView
            }

            @Override
            public void onFailure() {
                // Handle failure
            }
        });
    }

}