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
import android.widget.Toast;

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

        // Attach ItemTouchHelper for swipe-to-delete
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Handle swipe to delete
                if (direction == ItemTouchHelper.LEFT) {
                    // Display a confirmation dialog or perform deletion directly
                    showDeleteConfirmationDialog(viewHolder.getAdapterPosition());
                }else if (direction == ItemTouchHelper.RIGHT) {
                    // Handle swipe to complete
                    showCompleteConfirmationDialog(viewHolder.getAdapterPosition());
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a dialog or another activity to add a goal
                showAddGoalDialog();
            }
        });


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

    // Show Add Goal Dialog
    private void showAddGoalDialog() {
        Intent intent = new Intent(getActivity(), AddTask.class);
        startActivity(intent);
    }

    // Show Delete Confirmation Dialog
    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTask(position);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        taskAdapter.notifyItemChanged(position);
                    }
                })
                .show();
    }

    // Delete Task
    private void deleteTask(final int position) {
        String taskId = taskArrayList.get(position).getTaskId();

        if (taskId != null) {
            DbQuery.g_firestore.collection("Tasks")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("UserTasks")
                    .document(taskId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        taskArrayList.remove(position);
                        taskAdapter.notifyItemRemoved(position);
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(requireContext(), "Failed to delete task", Toast.LENGTH_SHORT).show();
                        taskAdapter.notifyItemChanged(position);
                    });
        } else {
            // Handle the case where taskId is null
            Log.e("DeleteTask", "TaskId is null for position: " + position);
            Toast.makeText(requireContext(), "TaskId is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCompleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Are you sure you want to complete this task?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        completeTask(position);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        taskAdapter.notifyItemChanged(position);
                    }
                })
                .show();
    }

    private void completeTask(final int position) {
        String taskId = taskArrayList.get(position).getTaskId();
        DbQuery.g_firestore.collection("Tasks")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("UserTasks")
                .document(taskId)
                .update("status", "Completed")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Update the local data and notify the adapter
                        taskArrayList.get(position).setTaskStatus("Completed");
                        taskArrayList.remove(position);
                        taskAdapter.notifyItemRemoved(position);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Toast.makeText(requireContext(), "Failed to complete task", Toast.LENGTH_SHORT).show();
                        taskAdapter.notifyItemChanged(position);
                    }
                });
    }

}