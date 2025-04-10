package com.example.workouttracker;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workouttracker.database.WorkoutDatabaseHelper;
import com.example.workouttracker.models.Exercise;
import com.google.android.material.textfield.TextInputEditText;

public class ExerciseDetailActivity extends AppCompatActivity {

    private WorkoutDatabaseHelper dbHelper;
    private Exercise exercise;

    private TextInputEditText nameInput, setsInput, repsInput, weightInput;
    private Button saveBtn, cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        dbHelper = new WorkoutDatabaseHelper(this);

        nameInput = findViewById(R.id.addExerciseNameInput);
        setsInput = findViewById(R.id.addSetsInput);
        repsInput = findViewById(R.id.addRepsInput);
        weightInput = findViewById(R.id.addWeightInput);
        saveBtn = findViewById(R.id.saveExerciseButton);
        cancelBtn = findViewById(R.id.cancelExerciseButton);

        int exerciseId = getIntent().getIntExtra("exercise_id", -1);
        if (exerciseId == -1) {
            Toast.makeText(this, "No exercise ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        exercise = dbHelper.getExerciseById(exerciseId);
        if (exercise == null) {
            Toast.makeText(this, "Exercise not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Populate UI with current data
        nameInput.setText(exercise.getName());
        setsInput.setText(String.valueOf(exercise.getSets()));
        repsInput.setText(String.valueOf(exercise.getReps()));
        weightInput.setText(String.valueOf(exercise.getWeight()));

        saveBtn.setOnClickListener(v -> updateExercise());
        cancelBtn.setOnClickListener(v -> finish());
    }

    private void updateExercise() {
        String setsStr = setsInput.getText().toString().trim();
        String repsStr = repsInput.getText().toString().trim();
        String weightStr = weightInput.getText().toString().trim();

        if (TextUtils.isEmpty(setsStr) || TextUtils.isEmpty(repsStr) || TextUtils.isEmpty(weightStr)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int sets = Integer.parseInt(setsStr);
            int reps = Integer.parseInt(repsStr);
            int weight = Integer.parseInt(weightStr);

            if (sets <= 0 || reps <= 0 || weight < 0) {
                Toast.makeText(this, "Values must be positive", Toast.LENGTH_SHORT).show();
                return;
            }

            exercise.setSets(sets);
            exercise.setReps(reps);
            exercise.setWeight(weight);

            boolean updated = dbHelper.updateExercise(exercise);
            if (updated) {
                Toast.makeText(this, "Exercise updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update exercise", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid numeric input", Toast.LENGTH_SHORT).show();
        }
    }
}
