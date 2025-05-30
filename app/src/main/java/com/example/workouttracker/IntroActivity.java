package com.example.workouttracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

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
    }
}
