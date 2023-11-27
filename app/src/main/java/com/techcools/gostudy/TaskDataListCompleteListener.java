package com.techcools.gostudy;

import java.util.ArrayList;

public interface TaskDataListCompleteListener {

    void onSuccess(ArrayList<TaskModel> tasks);
    void onFailure();

}
