package com.techcools.gostudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    sleep(6000);
                }catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    startActivity(new Intent(SplashScreen.this, LogIn.class));
                }
            }
        };

        thread.start();
    }
}