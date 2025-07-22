package com.example.workouttracker;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workouttracker.models.Exercise;
import com.example.workouttracker.models.WorkoutPlan;

import java.util.ArrayList;
import java.util.List;

public class WorkoutSessionActivity extends AppCompatActivity {

    private ListView sessionListView;
    private List<WorkoutPlan> workoutList;
    private WorkoutPlanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_session);

        sessionListView = findViewById(R.id.sessionListView);

        // Get the workout plan directly from the global manager
        workoutList = new ArrayList<>(WorkoutPlanManager.getPlans());


        if (workoutList == null || workoutList.isEmpty()) {
            Toast.makeText(this, "No workout plan found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        adapter = new WorkoutPlanAdapter(this, workoutList) {
            @Override
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
                if (workoutList.isEmpty()) {
                    Toast.makeText(WorkoutSessionActivity.this, "Workout Completed!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        };

        sessionListView.setAdapter(adapter);
    }
}
