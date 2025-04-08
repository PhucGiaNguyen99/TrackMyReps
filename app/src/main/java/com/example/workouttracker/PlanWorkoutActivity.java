package com.example.workouttracker;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workouttracker.database.WorkoutDatabaseHelper;
import com.example.workouttracker.models.Exercise;

import java.io.Serializable;
import java.util.List;

public class PlanWorkoutActivity extends AppCompatActivity {
    private WorkoutSelectionAdapter adapter;
    private WorkoutDatabaseHelper dbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_workout);

        dbHelper = new WorkoutDatabaseHelper(this);
        RecyclerView recyclerView = findViewById(R.id.planWorkoutRecyclerView);
        Button startButton = findViewById(R.id.startWorkoutButton);

        List<Exercise> exercises = dbHelper.getAllExercises();
        Log.d("PlanWorkout", "Found " + exercises.size() + " exercises");

        adapter = new WorkoutSelectionAdapter(exercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        startButton.setOnClickListener(v -> {
            List<Exercise> selected = adapter.getSelectedExercises();
            if (selected.isEmpty()) {
                Toast.makeText(this, "Select at least one exercise", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(this, WorkoutSessionActivity.class);
                intent.putExtra("selected_exercises", (Serializable) selected);
                startActivity(intent);
            }
        });
    }
}
