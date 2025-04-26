package com.example.workouttracker;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workouttracker.models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class WorkoutSelectionActivity extends AppCompatActivity {

    private ListView sessionListView;
    private List<Exercise> workoutList;
    private WorkoutPlanAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_selection);

        sessionListView = findViewById(R.id.workoutListView);

        // Get selected plan from global manager
        workoutList = new ArrayList<>(WorkoutPlanManager.getPlan());

        if (workoutList.isEmpty()) {
            Toast.makeText(this, "No workout plan found. Please create one first.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        adapter = new WorkoutPlanAdapter(this, workoutList);
        sessionListView.setAdapter(adapter);
    }

    public void showConfirmationDialog(Exercise exercise) {
        new AlertDialog.Builder(this)
                .setTitle("Complete Exercise")
                .setMessage("Mark \"" + exercise.getName() + "\" as completed?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    WorkoutPlanManager.removeExercise(exercise);
                    workoutList.remove(exercise);
                    adapter.notifyDataSetChanged();
                    if (workoutList.isEmpty()) {
                        Toast.makeText(this, "Workout Completed!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
