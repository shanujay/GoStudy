package com.techcools.gostudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();

        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    sleep(6000);
                }catch (Exception e) {
                    e.printStackTrace();
                }

                if (mAuth.getCurrentUser() != null) {

                    DbQuery.loadData(new AppCompleteListener() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(intent);
                            SplashScreen.this.finish();
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                } else {
                    Intent intent = new Intent(SplashScreen.this, LogIn.class);
                    startActivity(intent);
                    SplashScreen.this.finish();
                }
            }
        };

        thread.start();

        DbQuery.g_firestore = FirebaseFirestore.getInstance();
    }
}