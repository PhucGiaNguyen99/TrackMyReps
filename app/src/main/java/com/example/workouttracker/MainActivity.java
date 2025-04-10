package com.example.workouttracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workouttracker.database.WorkoutDatabaseHelper;
import com.example.workouttracker.models.Exercise;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private WorkoutDatabaseHelper dbHelper;
    private List<Exercise> exercises;
    private ExerciseAdapter adapter;
    private Button addExerciseButton, planWorkoutButton, startWorkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        planWorkoutButton = findViewById(R.id.planWorkoutButton);
        startWorkoutButton = findViewById(R.id.startWorkoutButton);

        dbHelper = new WorkoutDatabaseHelper(this);

        recyclerView = findViewById(R.id.exerciseRecyclerView);
        addExerciseButton = findViewById(R.id.addExerciseButton);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        exercises = dbHelper.getAllExercises();

        // Set click listeners
        planWorkoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlanWorkoutActivity.class);
            startActivity(intent);
        });

        startWorkoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WorkoutSelectionActivity.class);
            startActivity(intent);
        });

        adapter = new ExerciseAdapter(exercises, new ExerciseAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(Exercise exercise) {
                Toast.makeText(MainActivity.this, "Clicked: " + exercise.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ExerciseDisplayActivity.class);
                intent.putExtra("exercise_id", exercise.getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(Exercise exercise) {
                confirmDelete(exercise);
            }
        });

        recyclerView.setAdapter(adapter);

        addExerciseButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExerciseActivity.class);
            startActivity(intent);
        });
    }

    private void confirmDelete(Exercise exercise) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Exercise")
                .setMessage("Are you sure you want to delete " + exercise.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteExercise(exercise.getId());
                    refreshExerciseList();
                    Toast.makeText(this, "Exercise deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();

    }

    private void refreshExerciseList() {
        exercises.clear();
        exercises.addAll(dbHelper.getAllExercises());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshExerciseList();
    }
}