package com.example.samsunghealthclone.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.samsunghealthclone.R;
import com.example.samsunghealthclone.models.HeartRateData;
import com.example.samsunghealthclone.utils.DatabaseHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class HeartRateFragment extends Fragment {

    private TextView tvCurrentHeartRate, tvMinHeartRate, tvMaxHeartRate, tvAvgHeartRate;
    private LineChart heartRateHistoryChart;
    private Button btnMeasureHeartRate;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_heart_rate, container, false);

        tvCurrentHeartRate = view.findViewById(R.id.tvCurrentHeartRate);
        tvMinHeartRate = view.findViewById(R.id.tvMinHeartRate);
        tvMaxHeartRate = view.findViewById(R.id.tvMaxHeartRate);
        tvAvgHeartRate = view.findViewById(R.id.tvAvgHeartRate);
        heartRateHistoryChart = view.findViewById(R.id.heartRateHistoryChart);
        btnMeasureHeartRate = view.findViewById(R.id.btnMeasureHeartRate);

        dbHelper = new DatabaseHelper(getContext());

        setupChart();
        loadHeartRateData();

        btnMeasureHeartRate.setOnClickListener(v -> measureHeartRate());

        return view;
    }

    private void setupChart() {
        heartRateHistoryChart.getDescription().setEnabled(false);
        heartRateHistoryChart.setDrawGridBackground(false);
        heartRateHistoryChart.setTouchEnabled(true);
        heartRateHistoryChart.setDragEnabled(true);
        heartRateHistoryChart.setScaleEnabled(true);
        heartRateHistoryChart.setPinchZoom(true);

        XAxis xAxis = heartRateHistoryChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        loadChartData();
    }

    private void loadChartData() {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // Giả lập dữ liệu nhịp tim qua thời gian
        for (int i = 6; i >= 0; i--) {
            Calendar time = (Calendar) calendar.clone();
            time.add(Calendar.HOUR, -i);

            String timeLabel = timeFormat.format(time.getTime());
            labels.add(timeLabel);

            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            String dateTime = dateTimeFormat.format(time.getTime());

            HeartRateData heartRateData = dbHelper.getHeartRateDataByDateTime(dateTime);
            float heartRate = 0;
            if (heartRateData != null) {
                heartRate = heartRateData.getHeartRate();
            } else {
                // Giả lập dữ liệu nếu không có
                Random random = new Random();
                heartRate = 60 + random.nextInt(30);

                HeartRateData newData = new HeartRateData();
                newData.setDateTime(dateTime);
                newData.setHeartRate((int)heartRate);
                dbHelper.addHeartRateData(newData);
            }

            entries.add(new Entry(6-i, heartRate));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Nhịp tim");
        dataSet.setColor(getResources().getColor(R.color.colorAccent));
        dataSet.setDrawCircles(true);
        dataSet.setCircleColor(getResources().getColor(R.color.colorAccent));
        dataSet.setLineWidth(2f);
        dataSet.setDrawValues(true);

        LineData lineData = new LineData(dataSet);

        heartRateHistoryChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        heartRateHistoryChart.setData(lineData);
        heartRateHistoryChart.invalidate();

        // Cập nhật số liệu thống kê
        updateHeartRateStats();
    }

    private void updateHeartRateStats() {
        List<HeartRateData> heartRateList = dbHelper.getAllHeartRateData();

        if (heartRateList.size() > 0) {
            int min = Integer.MAX_VALUE;
            int max = 0;
            int sum = 0;

            for (HeartRateData data : heartRateList) {
                int rate = data.getHeartRate();
                min = Math.min(min, rate);
                max = Math.max(max, rate);
                sum += rate;
            }

            int avg = sum / heartRateList.size();

            tvMinHeartRate.setText(String.valueOf(min));
            tvMaxHeartRate.setText(String.valueOf(max));
            tvAvgHeartRate.setText(String.valueOf(avg));

            // Hiển thị nhịp tim mới nhất
            HeartRateData latest = heartRateList.get(heartRateList.size() - 1);
            tvCurrentHeartRate.setText(String.valueOf(latest.getHeartRate()));
        }
    }

    private void loadHeartRateData() {
        // Tải dữ liệu từ database và cập nhật UI
        List<HeartRateData> heartRateList = dbHelper.getAllHeartRateData();

        if (heartRateList.size() > 0) {
            HeartRateData latest = heartRateList.get(heartRateList.size() - 1);
            tvCurrentHeartRate.setText(String.valueOf(latest.getHeartRate()));
        } else {
            // Tạo dữ liệu mẫu nếu không có dữ liệu
            Random random = new Random();
            int heartRate = 70 + random.nextInt(10);
            tvCurrentHeartRate.setText(String.valueOf(heartRate));

            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            String dateTime = dateTimeFormat.format(Calendar.getInstance().getTime());

            HeartRateData newData = new HeartRateData();
            newData.setDateTime(dateTime);
            newData.setHeartRate(heartRate);
            dbHelper.addHeartRateData(newData);
        }

        updateHeartRateStats();
    }

    private void measureHeartRate() {
        // Giả lập quá trình đo nhịp tim
        btnMeasureHeartRate.setText("Đang đo...");
        btnMeasureHeartRate.setEnabled(false);

        new Thread(() -> {
            try {
                Thread.sleep(3000);

                // Giả lập kết quả đo
                Random random = new Random();
                int heartRate = 65 + random.nextInt(20);

                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                String dateTime = dateTimeFormat.format(Calendar.getInstance().getTime());

                HeartRateData newData = new HeartRateData();
                newData.setDateTime(dateTime);
                newData.setHeartRate(heartRate);
                dbHelper.addHeartRateData(newData);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        tvCurrentHeartRate.setText(String.valueOf(heartRate));
                        btnMeasureHeartRate.setText("Đo nhịp tim");
                        btnMeasureHeartRate.setEnabled(true);

                        loadChartData();
                        updateHeartRateStats();
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}