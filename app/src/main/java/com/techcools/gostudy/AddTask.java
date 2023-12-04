package com.techcools.gostudy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddTask extends AppCompatActivity {

    DatePickerDialog datePickerDialog;
    private EditText editTextTaskTitle;
    private Button buttonSaveTask, buttonCancelTask, task_time_btn, task_date_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        task_date_btn = findViewById(R.id.editTextDueDate);
        task_time_btn = findViewById(R.id.editTextDueTime);
        editTextTaskTitle = findViewById(R.id.editTextTaskTitle);
        buttonSaveTask = findViewById(R.id.buttonSaveTask);
        buttonCancelTask = findViewById(R.id.buttonCancelTask);

        task_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(AddTask.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                task_date_btn.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        task_time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        task_time_btn.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        buttonSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });

        buttonCancelTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTaskAddition();
            }
        });

    }

    private void saveTask() {
        String taskTitle = editTextTaskTitle.getText().toString().trim();
        String taskDate = task_date_btn.getText().toString().trim();
        String taskTime = task_time_btn.getText().toString().trim();
        String taskStatus = "Not Completed";


        if (taskTitle.isEmpty() || taskDate.isEmpty() || taskTime.isEmpty()) {
            Toast.makeText(AddTask.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        DbQuery.createTask(taskTitle, taskDate, taskTime, taskStatus, new AppCompleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(AddTask.this, "Task added successfully", Toast.LENGTH_SHORT).show();
                // Finish the activity after saving the task
                finish();
            }

            @Override
            public void onFailure() {
                Toast.makeText(AddTask.this, "Failed to add task", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelTaskAddition() {
        finish();
    }

}