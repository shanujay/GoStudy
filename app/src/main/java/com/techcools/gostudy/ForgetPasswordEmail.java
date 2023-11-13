package com.techcools.gostudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordEmail extends AppCompatActivity {

    TextInputEditText textInputEditText_email;
    Button button_change_password;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_email);

        textInputEditText_email = (TextInputEditText) findViewById(R.id.get_user_email);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Firebase
        mAuth = FirebaseAuth.getInstance();

        // Change Password by Email
        button_change_password = (Button) findViewById(R.id.change_password_email);
        button_change_password.setOnClickListener((View v) -> {
                    resetPassword();
                    Intent intent = new Intent(getApplicationContext(), LogIn.class);
                    startActivity(intent);
                }

        );

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#D1C4E9"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);

        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.back_button);

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);


    }

    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // resetPassword()
    private void resetPassword() {
        String email = textInputEditText_email.getText().toString().trim();

        // Checking Email Field is Empty or Not
        if (email.isEmpty()){
            textInputEditText_email.setError("Email is Required!");
            textInputEditText_email.requestFocus();
            return;
        }

        // Valid Email Address
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            textInputEditText_email.setError("Please Provide a Valid Email Address!");
            textInputEditText_email.requestFocus();
            return;
        }

        // Progress Bar
        progressBar.setVisibility(View.VISIBLE);

        // Get Password Request Email
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                Toast.makeText(ForgetPasswordEmail.this, "Check Your Email to Reset Your Password!", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(ForgetPasswordEmail.this, "Try Again Something Happened!", Toast.LENGTH_LONG).show();

            }
        });
    }
}