package com.example.workouttracker;


import android.app.AlertDialog;
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
        Button createPlanBtn = findViewById(R.id.createPlanButton);

        List<Exercise> exercises = dbHelper.getAllExercises();
        Log.d("PlanWorkout", "Found " + exercises.size() + " exercises");

        adapter = new WorkoutSelectionAdapter(exercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        createPlanBtn.setOnClickListener(v -> {
            List<Exercise> selected = adapter.getSelectedExercises();
            if (selected.isEmpty()) {
                Toast.makeText(this, "Select at least one exercise", Toast.LENGTH_SHORT).show();
            } else {
                // Build a message showing selected exercises
                StringBuilder message = new StringBuilder("You have selected:\n\n");
                for (Exercise ex : selected) {
                    message.append("• ").append(ex.getName()).append("\n");
                }
                message.append("\nCreate this workout plan?");

                new AlertDialog.Builder(this)
                        .setTitle("Confirm Plan")
                        .setMessage(message.toString())
                        .setPositiveButton("Yes", (dialog, which) -> {
                            WorkoutPlanManager.setPlan(selected); // Store it globally
                            Intent intent = new Intent(PlanWorkoutActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }
}
