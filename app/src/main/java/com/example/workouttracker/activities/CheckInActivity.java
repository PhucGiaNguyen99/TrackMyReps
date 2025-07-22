package com.example.workouttracker.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workouttracker.R;
import com.example.workouttracker.database.WorkoutDatabaseHelper;
import com.example.workouttracker.models.CheckIn;

import java.util.List;

public class CheckInActivity extends AppCompatActivity {
    private TextView countText, historyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        countText = findViewById(R.id.countText);
        historyText = findViewById(R.id.historyText);

        WorkoutDatabaseHelper dbHelper = new WorkoutDatabaseHelper(this);

        int count = dbHelper.getCheckInCount();
        List<CheckIn> checkIns = dbHelper.getAllCheckIns();

        countText.setText("Total Check-ins: " + count);

        StringBuilder sb = new StringBuilder();
        for (CheckIn ci : checkIns) {
            sb.append("• ").append(ci.getTimestamp()).append("\n");
        }

        historyText.setText(sb.toString());
    }
}
