package com.example.workouttracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workouttracker.database.WorkoutDatabaseHelper;
import com.example.workouttracker.models.Exercise;

public class ExerciseDisplayActivity extends AppCompatActivity {

    private WorkoutDatabaseHelper dbHelper;
    private TextView displayName, displaySets, displayReps, displayWeight;
    private Button updateButton, cancelButton;
    private Exercise exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_display);

        dbHelper = new WorkoutDatabaseHelper(this);

        displayName = findViewById(R.id.addExerciseNameInput);
        displaySets = findViewById(R.id.addSetsInput);
        displayReps = findViewById(R.id.addRepsInput);
        displayWeight = findViewById(R.id.addWeightInput);
        updateButton = findViewById(R.id.updateExerciseButton);
        cancelButton = findViewById(R.id.cancelExerciseButton);

        int exerciseId = getIntent().getIntExtra("exercise_id", -1);
        if (exerciseId == -1) {
            Toast.makeText(this, "No exercise found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        exercise = dbHelper.getExerciseById(exerciseId);
        if (exercise == null) {
            Toast.makeText(this, "Exercise not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Display values
        displayName.setText(exercise.getName());
        displaySets.setText(String.valueOf(exercise.getSets()));
        displayReps.setText(String.valueOf(exercise.getReps()));
        displayWeight.setText(String.valueOf(exercise.getWeight()));

        updateButton.setOnClickListener(v -> {
            Intent intent = new Intent(ExerciseDisplayActivity.this, ExerciseDetailActivity.class);
            intent.putExtra("exercise_id", exercise.getId());
            startActivity(intent);
        });

        cancelButton.setOnClickListener(v -> finish());
    }
}
