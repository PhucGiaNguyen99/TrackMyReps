package com.example.workouttracker;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workouttracker.database.WorkoutDatabaseHelper;
import com.example.workouttracker.models.Exercise;

public class AddExerciseActivity extends AppCompatActivity {
    private EditText nameInput, setsInput, repsInput, weightInput;
    private WorkoutDatabaseHelper dbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        dbHelper = new WorkoutDatabaseHelper(this);

        nameInput = findViewById(R.id.exerciseNameInput);
        setsInput = findViewById(R.id.setsInput);
        repsInput = findViewById(R.id.repsInput);
        weightInput = findViewById(R.id.weightInput);

        findViewById(R.id.saveExerciseButton).setOnClickListener(v -> saveExercise());
    }

    private void saveExercise() {
        String name = nameInput.getText().toString().trim();
        String setsStr = setsInput.getText().toString().trim();
        String repsStr = repsInput.getText().toString().trim();
        String weightStr = weightInput.getText().toString().trim();

        // If any of the input fields are empty, show notifications
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(setsStr) ||
                TextUtils.isEmpty(repsStr) || TextUtils.isEmpty(weightStr)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int sets = Integer.parseInt(setsStr);
        int reps = Integer.parseInt(repsStr);
        int weight = Integer.parseInt(weightStr);

        Exercise newExercise = new Exercise(name, sets, reps, weight);
        boolean success = dbHelper.addExercise(newExercise);

        if (success) {
            Toast.makeText(this, "Exercise added", Toast.LENGTH_SHORT).show();
            finish(); // return to MainActivity
        } else {
            Toast.makeText(this, "Exercise with that name already exists", Toast.LENGTH_SHORT).show();
        }
    }
}
