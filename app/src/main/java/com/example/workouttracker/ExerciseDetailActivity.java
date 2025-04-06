package com.example.workouttracker;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workouttracker.database.WorkoutDatabaseHelper;
import com.example.workouttracker.models.Exercise;

public class ExerciseDetailActivity extends AppCompatActivity {
    private WorkoutDatabaseHelper dbHelper;
    private Exercise exercise;

    private TextView nameText;
    private EditText setsInput, repsInput, weightInput;
    private Button saveBtn, cancelBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        dbHelper = new WorkoutDatabaseHelper(this);

        nameText = findViewById(R.id.exerciseNameText); // TextView for name (non-editable)
        setsInput = findViewById(R.id.detailSetsInput);
        repsInput = findViewById(R.id.detailRepsInput);
        weightInput = findViewById(R.id.detailWeightInput);
        saveBtn = findViewById(R.id.saveDetailButton);
        cancelBtn = findViewById(R.id.cancelDetailButton);

        int exerciseId = getIntent().getIntExtra("exercise_id", -1);
        if (exerciseId == -1) {
            Toast.makeText(this, "No exercise found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        for (Exercise e : dbHelper.getAllExercises()) {
            if (e.getId() == exerciseId) {
                exercise = e;
                break;
            }
        }

        if (exercise == null) {
            Toast.makeText(this, "Exercise not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Populate fields
        nameText.setText(exercise.getName());
        setsInput.setText(String.valueOf(exercise.getSets()));
        repsInput.setText(String.valueOf(exercise.getReps()));
        weightInput.setText(String.valueOf(exercise.getWeight()));

        saveBtn.setOnClickListener(v -> saveChanges());
        cancelBtn.setOnClickListener(v -> finish());
    }

    private void saveChanges() {
        String setsStr = setsInput.getText().toString().trim();
        String repsStr = repsInput.getText().toString().trim();
        String weightStr = weightInput.getText().toString().trim();

        if (TextUtils.isEmpty(setsStr) || TextUtils.isEmpty(repsStr) || TextUtils.isEmpty(weightStr)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        exercise.setSets(Integer.parseInt(setsStr));
        exercise.setReps(Integer.parseInt(repsStr));
        exercise.setWeight(Integer.parseInt(weightStr));

        boolean updated = dbHelper.updateExercise(exercise);
        if (updated) {
            Toast.makeText(this, "Exercise updated", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show();
        }

    }
}
