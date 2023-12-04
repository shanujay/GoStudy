package com.techcools.gostudy;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new UpcomingTaskFragment();
        }
        else if (position == 1)
        {
            fragment = new CompletedTaskFragment();
        }
        else if (position == 2)
        {
            fragment = new OverdueTaskFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Upcoming";
        }
        else if (position == 1)
        {
            title = "Completed";
        }
        else if (position == 2)
        {
            title = "Overdue";
        }
        return title;
    }
}
