package com.example.workouttracker;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workouttracker.models.Exercise;
import com.example.workouttracker.models.WorkoutPlan;

import java.util.ArrayList;
import java.util.List;

public class WorkoutSelectionActivity extends AppCompatActivity {

    private ListView sessionListView;
    private List<WorkoutPlan> workoutPlanList;
    private WorkoutPlanAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_selection);

        sessionListView = findViewById(R.id.workoutListView);

        // Get selected plan from global manager
        workoutPlanList = new ArrayList<>(WorkoutPlanManager.getPlans());

        if (workoutPlanList.isEmpty()) {
            Toast.makeText(this, "No workout plan found. Please create one first.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // adapter = new WorkoutPlanAdapter(this, workoutPlanList);
        // sessionListView.setAdapter(adapter);
    }

    public void showConfirmationDialog(WorkoutPlan workoutPlan) {
        new AlertDialog.Builder(this)
                .setTitle("Complete Exercise")
                .setMessage("Mark \"" + workoutPlan.getName() + "\" as completed?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    WorkoutPlanManager.deletePlan(workoutPlan.getName());
                    workoutPlanList.remove(workoutPlan);
                    adapter.notifyDataSetChanged();
                    if (workoutPlanList.isEmpty()) {
                        Toast.makeText(this, "Workout Completed!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
