package com.example.workouttracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class WorkoutSelectionActivity extends AppCompatActivity {

    private ListView workoutListView;
    private List<String> workoutPlans; // Assume this list is populated with workout plan names

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_selection);

        workoutListView = findViewById(R.id.workoutListView);

        // Populate the workoutPlans list with your workout plan names
        // For example, workoutPlans = Arrays.asList("Plan A", "Plan B");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, workoutPlans);
        workoutListView.setAdapter(adapter);

        workoutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedWorkout = workoutPlans.get(position);
                // Start the WorkoutSessionActivity with the selected workout plan
                Intent intent = new Intent(WorkoutSelectionActivity.this, WorkoutSessionActivity.class);
                intent.putExtra("WORKOUT_PLAN_NAME", selectedWorkout);
                startActivity(intent);
            }
        });
    }
}
