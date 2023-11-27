package com.techcools.gostudy;

public class TaskModel {

    private String taskTitle;
    private String taskDate;
    private String taskTime;

    private String taskId;
    private String taskStatus;

    public TaskModel() {
        // Default constructor required for Firestore
    }

    public TaskModel(String taskTitle, String taskDate, String taskTime, String taskStatus) {
        this.taskTitle = taskTitle;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
        this.taskStatus = taskStatus;
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
