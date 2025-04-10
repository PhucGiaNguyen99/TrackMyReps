package com.example.workouttracker;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workouttracker.database.WorkoutDatabaseHelper;
import com.example.workouttracker.models.Exercise;

public class AddExerciseActivity extends AppCompatActivity {

    private EditText nameInput, setsInput, repsInput, weightInput;
    private WorkoutDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        dbHelper = new WorkoutDatabaseHelper(this);

        nameInput = findViewById(R.id.exerciseNameInput);
        setsInput = findViewById(R.id.setsInput);
        repsInput = findViewById(R.id.repsInput);
        weightInput = findViewById(R.id.weightInput);

        Button saveBtn = findViewById(R.id.saveExerciseButton);
        Button cancelBtn = findViewById(R.id.cancelExerciseButton);

        saveBtn.setOnClickListener(v -> saveExercise());
        cancelBtn.setOnClickListener(v -> finish()); // Return to previous screen
    }

    private void saveExercise() {
        String name = nameInput.getText().toString().trim();
        String setsStr = setsInput.getText().toString().trim();
        String repsStr = repsInput.getText().toString().trim();
        String weightStr = weightInput.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(setsStr)
                || TextUtils.isEmpty(repsStr) || TextUtils.isEmpty(weightStr)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
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

            if (dbHelper.exerciseExists(name)) {
                Toast.makeText(this, "Exercise with that name already exists", Toast.LENGTH_SHORT).show();
                return;
            }

            Exercise newExercise = new Exercise(name, sets, reps, weight);
            boolean success = dbHelper.addExercise(newExercise);

            if (success) {
                Toast.makeText(this, "Exercise added", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add exercise", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid numeric input", Toast.LENGTH_SHORT).show();
        }
    }
}
