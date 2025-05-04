package com.example.workouttracker;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workouttracker.database.WorkoutDatabaseHelper;
import com.example.workouttracker.models.Exercise;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddExerciseActivity extends AppCompatActivity {

    private EditText nameInput, setsInput, repsInput, weightInput;
    private WorkoutDatabaseHelper dbHelper;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        dbHelper = new WorkoutDatabaseHelper(this);

        db = FirebaseFirestore.getInstance();

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

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter the exercise name.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Modified that any of those 3 can be optional
            int sets = TextUtils.isEmpty(setsStr) ? 0 : Integer.parseInt(setsStr);
            int reps = TextUtils.isEmpty(repsStr) ? 0 : Integer.parseInt(repsStr);
            int weight = TextUtils.isEmpty(weightStr) ? 0 : Integer.parseInt(weightStr);

            // Ensure at least one of three is non-zero
            if (sets == 0 && reps == 0 && weight == 0) {
                Toast.makeText(this, "Please enter at least one detail (sets, reps, or weight).", Toast.LENGTH_SHORT).show();
                return;
            }

            //if (sets <= 0 || reps <= 0 || weight < 0) {
            //    Toast.makeText(this, "Values must be positive", Toast.LENGTH_SHORT).show();
            //    return;
            //}

            if (dbHelper.exerciseExists(name)) {
                Toast.makeText(this, "Exercise with that name already exists", Toast.LENGTH_SHORT).show();
                return;
            }

            Exercise newExercise = new Exercise(name, sets, reps, weight);

            // Add the new exercise to the SQLite
            boolean success = dbHelper.addExercise(newExercise);

            // After adding to SQLite successfully, upload the exercise to Firestore
            // Save each exercise as a document in the "exercises" collection and specific to each user
            //db.collection("exercises")
            //        .add(newExercise)
            //        .addOnSuccessListener(documentReference -> {
            //            Toast.makeText(this, "Exercise added to Firestore", Toast.LENGTH_SHORT).show();
            //        })
            //        .addOnFailureListener(e -> {
            //            Toast.makeText(this, "Failed to add to Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            //        });


            if (success) {
                // HAVE TO CHECK IF CURRENT USER IS NULL
                // Save to Firestore under current user
                // String userId = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                Log.d("AuthCheck", "Current user: " + (currentUser != null ? currentUser.getUid() : "null"));

                if (currentUser == null) {
                    Toast.makeText(this, "Not logged in. Can't sync to Firestore.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String userId = currentUser.getUid();

                Log.d("FIREBASE_WRITE", "Trying to write to Firestore");

                // Use userId for each user and exercise name as doc ID
                db.collection("users")
                                .document(userId)
                                .collection("exercises")
                                .document(name)
                                .set(newExercise)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("FIREBASE_WRITE", "Exercise uploaded successfully");
                                    Toast.makeText(this, "Exercise added to Firestore", Toast.LENGTH_SHORT).show();
                                    finish();   // ONLY FINISH AFTER SUCCESS TOAST
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("FIREBASE_WRITE", "Failed to add exercise", e);
                                    Toast.makeText(this, "Failed to add to Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("FirestoreError", "Error uploading to Firestore", e);
                                });

                Toast.makeText(this, "Exercise added", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Failed to add exercise", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid numeric input", Toast.LENGTH_SHORT).show();
        }
    }
}
