package com.techcools.gostudy;

import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.text.ParseException;
public class DbQuery {


    public static FirebaseFirestore g_firestore;
    public static ProfileModel myProfile = new ProfileModel("NA", null, "NA");

    public static void createUserData(String name, String email, String userName, AppCompleteListener completeListener) {

        // Store User Details in Firebase FireStore When User Register
        Map<String,Object> user = new HashMap<>();
        user.put("fullName", name);
        user.put("userEmail", email);
        user.put("userName", userName);

        DocumentReference userDoc = g_firestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        WriteBatch batch = g_firestore.batch();

        batch.set(userDoc, user);

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {

                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();

                    }
                });
    }

    public static void getUserData(AppCompleteListener completeListener) {

        g_firestore.collection("Users").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        myProfile.setName(documentSnapshot.getString("fullName"));
                        myProfile.setEmail(documentSnapshot.getString("userEmail"));
                        myProfile.setUsername(documentSnapshot.getString("userName"));

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    // Create Task Details
    public static void createTask(String taskTitle, String taskDate, String taskTime, String taskStatus, AppCompleteListener completeListener) {
        // Store Task Details in Firebase Firestore
        Map<String, Object> task = new HashMap<>();
        task.put("taskTitle", taskTitle);

        // Convert the taskDate to a suitable format (assuming it's a string in "dd/MM/yyyy" format)
        try {
            SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = sdfInput.parse(taskDate);
            task.put("taskDate", sdfOutput.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            completeListener.onFailure();
            return;
        }

        task.put("taskTime", taskTime);
        task.put("status", "Not Completed");

        // Generate a unique document ID for each task
        String taskId = g_firestore.collection("Tasks").document().getId();

        DocumentReference taskDoc = g_firestore.collection("Tasks")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("UserTasks")
                .document(taskId);

        WriteBatch batch = g_firestore.batch();

        batch.set(taskDoc, task);

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }


    public static void loadData(AppCompleteListener completeListener) {

        getUserData(completeListener);

    }

    public static void saveProfileData(String name, String username, AppCompleteListener completeListener) {

        Map<String, Object> profileData = new ArrayMap<>();

        profileData.put("fullName", name);
        profileData.put("userName", username);

        g_firestore.collection("Users").document(FirebaseAuth.getInstance().getUid())
                .update(profileData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        myProfile.setName(name);
                        myProfile.setUsername(username);

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });

    }

    // Get Task Details
    public static void getTaskDataList(TaskDataListCompleteListener completeListener) {
        // Get the current date in the "yyyy-MM-dd" format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        g_firestore.collection("Tasks")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("UserTasks")
                .whereEqualTo("status", "Not Completed")
                .whereGreaterThanOrEqualTo("taskDate", currentDate)
                .orderBy("taskDate")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<TaskModel> taskList = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            TaskModel task = documentSnapshot.toObject(TaskModel.class);
                            task.setTaskId(documentSnapshot.getId());
                            taskList.add(task);
                        }
                        completeListener.onSuccess(taskList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    // Get Completed Task Details
    public static void getCompletedTaskDataList(TaskDataListCompleteListener completeListener) {
        g_firestore.collection("Tasks")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("UserTasks")
                .whereEqualTo("status", "Completed")
                .orderBy("taskDate")  // Order tasks by date
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<TaskModel> taskList = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            TaskModel task = documentSnapshot.toObject(TaskModel.class);
                            task.setTaskId(documentSnapshot.getId()); // Manually set taskId
                            Log.d("FirestoreData", "Task: " + documentSnapshot.getData());
                            taskList.add(task);
                        }
                        completeListener.onSuccess(taskList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    // Get Overdue Task Data List
    public static void getOverdueTaskDataList(TaskDataListCompleteListener completeListener) {
        g_firestore.collection("Tasks")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("UserTasks")
                .whereEqualTo("status", "Not Completed")
                .orderBy("taskDate")  // Order tasks by date
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<TaskModel> taskList = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            TaskModel task = documentSnapshot.toObject(TaskModel.class);
                            task.setTaskId(documentSnapshot.getId()); // Manually set taskId

                            // Include only overdue tasks
                            if (task.isOverdue()) {
                                taskList.add(task);
                            }
                        }
                        completeListener.onSuccess(taskList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }


}
