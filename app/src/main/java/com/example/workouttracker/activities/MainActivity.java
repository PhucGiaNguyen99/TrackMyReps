package com.example.workouttracker.activities;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workouttracker.R;
import com.example.workouttracker.adapters.ExerciseAdapter;
import com.example.workouttracker.database.WorkoutDatabaseHelper;
import com.example.workouttracker.models.Exercise;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;



public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private WorkoutDatabaseHelper dbHelper;
    private List<Exercise> exercises;
    private ExerciseAdapter adapter;
    private Button addExerciseButton, planWorkoutButton, startWorkoutButton, logoutButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        planWorkoutButton = findViewById(R.id.planWorkoutButton);
        startWorkoutButton = findViewById(R.id.startWorkoutButton);

        dbHelper = new WorkoutDatabaseHelper(this);

        recyclerView = findViewById(R.id.exerciseRecyclerView);
        addExerciseButton = findViewById(R.id.addExerciseButton);

        logoutButton = findViewById(R.id.logoutButton);

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

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Sign the user out
            Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
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
                    // Delete item from SQLite
                    boolean deleted = dbHelper.deleteExercise(exercise.getId());

                    if (!deleted) {
                        Toast.makeText(this, "Failed to delete from local database", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // After successfully delete from SQLite, delete from Firestore
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser == null) {
                        Toast.makeText(this, "Not logged in. Can't delete from Firestore.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String userId = currentUser.getUid();
                    FirebaseFirestore.getInstance()
                                    .collection("users")
                            .document(userId)
                            .collection("exercises")
                            .document(exercise.getName()).delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Exercise deleted", Toast.LENGTH_SHORT).show();

                                // No need to add, because you are working on and staying on MainActivtiy, so no need to terminate the current activity and communication back the previous screen
                                // If keeping those setResult() and finish(), it terminates the current screen and out of the app
                                // setResult(RESULT_OK);
                                // finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to delete from Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });

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

    // When user sign in successfully, their old exercises will be automatically downloaded and displayed
    private void syncExerciseFromCloud() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .document(userId)
                .collection("exercises")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    WorkoutDatabaseHelper dbHelper = new WorkoutDatabaseHelper(this);
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Exercise exercise = doc.toObject(Exercise.class);
                        dbHelper.addExercise(exercise);
                    }
                    refreshExerciseList();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to sync exercises: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}

