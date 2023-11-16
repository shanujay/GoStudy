// ProgressStatisticsFragment.java
package com.techcools.gostudy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class ProgressStatisticsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO: Implement the logic to create and display the progress statistics chart
        View view = inflater.inflate(R.layout.fragment_progress_statistics, container, false);

        // Your logic to create and display the progress statistics chart goes here

        return view;
    }
}
