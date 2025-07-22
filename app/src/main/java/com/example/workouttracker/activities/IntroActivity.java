package com.example.workouttracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workouttracker.R;
import com.example.workouttracker.database.WorkoutDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IntroActivity extends AppCompatActivity {
    WorkoutDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Button startBtn = findViewById(R.id.startAppButton);
        startBtn.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, AuthenticationActivity.class);
            startActivity(intent);
            finish(); // Optional: Prevent going back to intro screen
        });

        Button checkInButton = findViewById(R.id.checkInButton);
        Button viewCheckInHistoryButton = findViewById(R.id.viewCheckInHistoryButton);

        dbHelper = new WorkoutDatabaseHelper(this);

        checkInButton.setOnClickListener(v -> {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            dbHelper.insertCheckIn(timestamp);
            Toast.makeText(this, "Check-in saved!", Toast.LENGTH_SHORT).show();
        });

        // View History Button
        viewCheckInHistoryButton.setOnClickListener(v -> {
            startActivity(new Intent(this, CheckInActivity.class));
        });
    }
}
