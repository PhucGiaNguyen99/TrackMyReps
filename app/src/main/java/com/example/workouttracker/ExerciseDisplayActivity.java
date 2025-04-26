package com.example.workouttracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workouttracker.database.WorkoutDatabaseHelper;
import com.example.workouttracker.models.Exercise;

public class ExerciseDisplayActivity extends AppCompatActivity {

    private static final int REQUEST_UPDATE = 1;

    private WorkoutDatabaseHelper dbHelper;
    private TextView displayName, displaySets, displayReps, displayWeight;
    private Button updateButton, cancelButton;
    private Exercise exercise;
    private int exerciseId;

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

        exerciseId = getIntent().getIntExtra("exercise_id", -1);
        if (exerciseId == -1) {
            Toast.makeText(this, "No exercise found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadExercise();

        updateButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExerciseDetailActivity.class);
            intent.putExtra("exercise_id", exerciseId);
            startActivityForResult(intent, REQUEST_UPDATE);
        });

        cancelButton.setOnClickListener(v -> finish());
    }

    private void loadExercise() {
        exercise = dbHelper.getExerciseById(exerciseId);
        if (exercise == null) {
            Toast.makeText(this, "Exercise not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        displayName.setText(exercise.getName());
        displaySets.setText(String.valueOf(exercise.getSets()));
        displayReps.setText(String.valueOf(exercise.getReps()));
        displayWeight.setText(String.valueOf(exercise.getWeight()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_UPDATE && resultCode == RESULT_OK) {
            loadExercise();  // Reload updated values
        }
    }
}
