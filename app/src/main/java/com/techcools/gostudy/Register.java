package com.techcools.gostudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    TextView logIn;

    public static final String TAG = "TAG";
    TextInputEditText editTextEmail, editTextPassword, editTextFullName, editTextUserName;
    Button buttonReg;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;

    // Defining Password Pattern
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{4,}" +                // at least 4 characters
                    "$");

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^" +
                    "(?=\\S+$)" +            // no white spaces
                    ".{4,}" +                // at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //Firebase FireStore
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Referencing Email, Full Name, User Name, Password, Register button, Login and Progress Bar
        editTextEmail = findViewById(R.id.email);
        editTextFullName = findViewById(R.id.fullName);
        editTextUserName = findViewById(R.id.userName);
        editTextPassword = findViewById(R.id.password);
        buttonReg = findViewById(R.id.register);
        logIn = findViewById(R.id.loginHere);

        // Login Page Navigate
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
                finish();
            }
        });

        //Register Button
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password, fName, userName;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                fName = String.valueOf(editTextFullName.getText());
                userName = String.valueOf(editTextUserName.getText());

                // Checking Full Name Field is Empty or Not
                if (fName.isEmpty()){
                    editTextFullName.setError("Full Name is Required!");
                    editTextFullName.requestFocus();
                    return;
                }

                // Checking User Name Field is Empty or Not
                if (userName.isEmpty()){
                    editTextUserName.setError("User Name is Required!");
                    editTextUserName.requestFocus();
                    return;
                }
                // if username does not matches to the pattern, it will display an error message
                else if (!USERNAME_PATTERN.matcher(userName).matches()) {
                    editTextUserName.setError("Try Different Username!");
                    return;
                }

                // Checking Email Field is Empty or Not
                if (email.isEmpty()){
                    editTextEmail.setError("Email is Required!");
                    editTextEmail.requestFocus();
                    return;
                }

                // Valid Email Address
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editTextEmail.setError("Please Provide a Valid Email Address!");
                    editTextEmail.requestFocus();
                    return;
                }

                // Checking Password Field is Empty or Not
                if (password.isEmpty()){
                    editTextPassword.setError("Password is Required!");
                    editTextPassword.requestFocus();
                    return;
                }
                // if password does not matches to the pattern, it will display an error message "Password is too weak"
                else if (!PASSWORD_PATTERN.matcher(password).matches()) {
                    editTextPassword.setError("Password is too weak");
                    return;
                }


                //Register the user in FireBase
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Authentication success.", Toast.LENGTH_SHORT).show();
                            DbQuery.createUserData(fName, email, userName, new AppCompleteListener(){

                                @Override
                                public void onSuccess() {

                                    DbQuery.loadData(new AppCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            Intent intent = new Intent(getApplicationContext(), LogIn.class);
                                            startActivity(intent);
                                            Register.this.finish();
                                        }

                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(Register.this, "Something Went Wrong ! ",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }

                                @Override
                                public void onFailure() {

                                    Toast.makeText(Register.this, "Something Went Wrong ! Please Try Again Later.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void updateUI(FirebaseUser user) {

        Intent intent = new Intent(Register.this, LogIn.class);
        startActivity(intent);
        finish();
    }
}