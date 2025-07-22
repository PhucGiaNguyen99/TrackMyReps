package com.example.workouttracker.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workouttracker.R;
import com.example.workouttracker.database.WorkoutDatabaseHelper;
import com.example.workouttracker.models.Exercise;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", nameInput.getText().toString());
        outState.putString("sets", setsInput.getText().toString());
        outState.putString("reps", repsInput.getText().toString());
        outState.putString("weight", weightInput.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nameInput.setText(savedInstanceState.getString("name"));
        setsInput.setText(savedInstanceState.getString("sets"));
        repsInput.setText(savedInstanceState.getString("reps"));
        weightInput.setText(savedInstanceState.getString("weight"));
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

            // Update the record in SQLite
            boolean updated = dbHelper.updateExercise(exercise);

            if (!updated) {
                Toast.makeText(this, "Failed to update exercise", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create new exercise object
            Exercise updatedExercise = new Exercise(exercise.getName(), sets, reps, weight);

            // Get the userId
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(this, "Not logged in. Can't sync to Firestore.", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = currentUser.getUid();

            // Update the record in Firestore
            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(userId)
                    .collection("exercises")
                    .document(exercise.getName())
                    .set(updatedExercise)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Exercise updated in Firestore", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to update in Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid numeric input", Toast.LENGTH_SHORT).show();
        }
    }
}
