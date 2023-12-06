package com.techcools.gostudy;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;

public class ProgressFragment extends Fragment {

    private PieChart pieChart;
    private TextView totalFocusTIme;
    private TextView totalBreakTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        // Get TextView reference
        TextView textViewDate = view.findViewById(R.id.textViewDate);
        totalFocusTIme = view.findViewById(R.id.totalFocusTime_progress);
        totalBreakTime = view.findViewById(R.id.totalBreakTime_progress);

        // Set current date with custom format
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        textViewDate.setText(currentDate);

        // Set up PieChart
        pieChart = view.findViewById(R.id.pieChart);

        // Fetch Pomodoro timer and break timer data and update the PieChart
        fetchPomodoroDataAndUpdateChart();
        fetchBreakDataAndUpdateChart();


        return view;
    }

    private void fetchPomodoroDataAndUpdateChart() {
        DbQuery.getPomodoroTimerDataForCurrentDate(new TimerDataCompleteListener() {
            @Override
            public void onSuccess(long totalFocusDuration) {
                totalFocusTIme.setText(formatDuration(totalFocusDuration));
                // Fetch break timer data inside the success callback for focus time
                fetchBreakDataAndUpdateChart();
            }

            @Override
            public void onFailure() {
                // Handle failure, e.g., show an error message
            }
        });
    }

    private void fetchBreakDataAndUpdateChart() {
        DbQuery.getBreakTimerDataForCurrentDate(new TimerDataCompleteListener() {
            @Override
            public void onSuccess(long totalBreakDuration) {
                totalBreakTime.setText(formatDuration(totalBreakDuration));


            }

            @Override
            public void onFailure() {
                // Handle failure, e.g., show an error message
            }
        });
    }

    private void updatePieChart(long totalFocusDuration, long totalBreakDuration, long difference) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(totalFocusDuration, "Focus Time"));
        entries.add(new PieEntry(totalBreakDuration, "Break Time"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        pieChart.getLegend().setEnabled(false);
        data.setDrawValues(false);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Time Distribution\nDifference: " + formatDuration(Math.abs(difference)));
        pieChart.animateY(1000);
    }

    private String formatDuration(long milliseconds) {
        long minutes = (milliseconds / (1000 * 60)) % 60;
        long hours = milliseconds / (1000 * 60 * 60);
        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes);
    }

}