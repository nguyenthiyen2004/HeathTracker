package com.example.samsunghealthclone.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.samsunghealthclone.R;
import com.example.samsunghealthclone.models.StepData;
import com.example.samsunghealthclone.utils.DatabaseHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StepsFragment extends Fragment {

    private TextView tvStepCount, tvDistance, tvCalories;
    private BarChart stepsHistoryChart;
    private DatabaseHelper dbHelper;
    private StepCountReceiver stepCountReceiver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps, container, false);

        tvStepCount = view.findViewById(R.id.tvStepCount);
        tvDistance = view.findViewById(R.id.tvDistance);
        tvCalories = view.findViewById(R.id.tvCalories);
        stepsHistoryChart = view.findViewById(R.id.stepsHistoryChart);

        dbHelper = new DatabaseHelper(getContext());

        setupStepCountReceiver();
        setupChart();
        loadTodayStepData();

        return view;
    }

    private void setupStepCountReceiver() {
        stepCountReceiver = new StepCountReceiver();
        IntentFilter intentFilter = new IntentFilter("step-count-update");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(stepCountReceiver, intentFilter);
    }

    private void setupChart() {
        stepsHistoryChart.getDescription().setEnabled(false);
        stepsHistoryChart.setDrawGridBackground(false);
        stepsHistoryChart.setDrawBarShadow(false);
        stepsHistoryChart.setDrawValueAboveBar(true);
        stepsHistoryChart.setPinchZoom(false);
        stepsHistoryChart.setDrawGridBackground(false);

        XAxis xAxis = stepsHistoryChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        loadChartData();
    }

    private void loadChartData() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.getDefault());

        for (int i = 6; i >= 0; i--) {
            Calendar day = (Calendar) calendar.clone();
            day.add(Calendar.DAY_OF_WEEK, -i);

            String dayOfWeek = sdf.format(day.getTime());
            labels.add(dayOfWeek);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String date = dateFormat.format(day.getTime());

            StepData stepData = dbHelper.getStepDataByDate(date);
            float steps = 0;
            if (stepData != null) {
                steps = stepData.getStepCount();
            }

            entries.add(new BarEntry(6-i, steps));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Bước chân trong 7 ngày qua");
        dataSet.setColor(getResources().getColor(R.color.colorPrimary));

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);

        stepsHistoryChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        stepsHistoryChart.setData(data);
        stepsHistoryChart.invalidate();
    }

    private void loadTodayStepData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = dateFormat.format(Calendar.getInstance().getTime());

        StepData stepData = dbHelper.getStepDataByDate(today);
        if (stepData != null) {
            updateStepUI(stepData.getStepCount());
        } else {
            updateStepUI(0);
        }
    }

    private void updateStepUI(int steps) {
        tvStepCount.setText(String.valueOf(steps));
        // Tính toán khoảng cách (ước tính 0.762m cho mỗi bước chân)
        double distance = steps * 0.762 / 1000;
        tvDistance.setText(String.format(Locale.getDefault(), "%.2f km", distance));

        // Tính toán calo (ước tính 0.04 calo cho mỗi bước chân)
        double calories = steps * 0.04;
        tvCalories.setText(String.format(Locale.getDefault(), "%.0f calo", calories));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(stepCountReceiver);
    }

    private class StepCountReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("step-count-update")) {
                int steps = intent.getIntExtra("steps", 0);
                updateStepUI(steps);
                loadChartData();
            }
        }
    }
}