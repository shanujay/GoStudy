package com.techcools.gostudy;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskModel {

    private String taskTitle;
    private String taskDate;
    private String taskTime;

    private String taskId;
    private String taskStatus;

    public boolean isOverdue() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date currentDate = new Date();
            Date dueDate = sdf.parse(this.taskDate);
            return currentDate.after(dueDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public TaskModel() {
        // Default constructor required for Firestore
    }

    public TaskModel(String taskTitle, String taskDate, String taskTime, String taskStatus, String taskId) {
        this.taskTitle = taskTitle;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
        this.taskStatus = taskStatus;
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public String getTaskId() {
        // Check how taskId is initialized and return a non-null value
        Log.d("TaskModel", "getTaskId: " + taskId);
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

}
