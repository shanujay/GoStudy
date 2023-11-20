package com.techcools.gostudy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class HelpCenterActivity extends AppCompatActivity {

    // creating object of ViewPager
    ViewPager mViewPager;

    // images array
    int[] images = {R.drawable.pomodoro_timer_ui, R.drawable.study_music_ui,
            R.drawable.questionnaire_ui};

    // Creating Object of ViewPagerAdapter
    HelpCenterViewPagerAdapter helpCenterViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);

        // Initializing the ViewPager Object
        mViewPager = (ViewPager)findViewById(R.id.viewPagerMain);

        // Initializing the ViewPagerAdapter
        helpCenterViewPagerAdapter = new HelpCenterViewPagerAdapter(HelpCenterActivity.this, images);

        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(helpCenterViewPagerAdapter);
    }
}