package com.techcools.gostudy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    CommunityFragment community = new CommunityFragment();
    CalendarFragment calendarFragment = new CalendarFragment();
    HomeFragment homeFragment = new HomeFragment();
    ProgressFragment progressFragment = new ProgressFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bottom Navigation Bar
        bottomNavigationView = findViewById(R.id.bottom_nav);

        getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, homeFragment).commit();
                    return true;

                case R.id.community:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, community).commit();
                    return true;

                case R.id.calendar:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, calendarFragment).commit();
                    return true;

                case R.id.progress:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, progressFragment).commit();
                    return true;
            }

            return false;
        });

    }
}