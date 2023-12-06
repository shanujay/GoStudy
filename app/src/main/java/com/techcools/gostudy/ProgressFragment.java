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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        // Get TextView reference
        TextView textViewDate = view.findViewById(R.id.textViewDate);

        // Set current date with custom format
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        textViewDate.setText(currentDate);

        // Set up PieChart
        PieChart pieChart = view.findViewById(R.id.pieChart);
        setupPieChart(pieChart);

        return view;
    }

    private void setupPieChart(PieChart pieChart) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(25f));
        entries.add(new PieEntry(75f));


        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        // Disable legend
        pieChart.getLegend().setEnabled(false);

        // Disable value labels
        data.setDrawValues(false);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Task Completed");
        pieChart.animateY(1000);
    }
}