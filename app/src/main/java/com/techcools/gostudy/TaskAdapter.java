package com.techcools.gostudy;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private ArrayList<TaskModel> taskList;

    public TaskAdapter(ArrayList<TaskModel> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskModel task = taskList.get(position);
        holder.taskTitle.setText(task.getTaskTitle());
        holder.taskDate.setText(task.getTaskDate());
        holder.taskTime.setText(task.getTaskTime());
        holder.taskStatus.setText(task.getTaskStatus());

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView taskTitle;
        public TextView taskDate;
        public TextView taskTime;
        public TextView taskStatus;

        public ViewHolder(View view) {
            super(view);
            taskTitle = view.findViewById(R.id.task_name);
            taskDate = view.findViewById(R.id.task_date);
            taskTime = view.findViewById(R.id.task_time);
            taskStatus = view.findViewById(R.id.task_status);
        }
    }

    public void removeTask(int position) {
        taskList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

}
