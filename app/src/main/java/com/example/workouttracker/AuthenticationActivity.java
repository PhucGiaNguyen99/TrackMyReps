package com.example.workouttracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workouttracker.database.WorkoutDatabaseHelper;
import com.example.workouttracker.models.Exercise;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
// import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private Button loginButton, registerButton;
    private ProgressBar loadingSpinner;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        FirebaseApp.initializeApp(this);
        Toast.makeText(this, "Firebase initialized", Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();

        // Check if user is already logged in
        // if (mAuth.getCurrentUser() != null) {
        //     // If use is logged in already, skip the login screen
        //     goToMainActivity();
        //    return;
        // }

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        loadingSpinner = findViewById(R.id.loadingSpinner);

        loadingSpinner.setVisibility(View.GONE); // Hide spinner at first

        loginButton.setOnClickListener(v -> loginUser());
        registerButton.setOnClickListener(v -> registerUser());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            goToMainActivity();
        }
    }


    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter email and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // goToMainActivity();
                        // Sync data - fetch the user's exercises from Firestore and insert them into the local WorkoutDatabaseHelper
                        // Implement the asynchronuous operation
                        syncFromFirestoreAndContinue();

                        // Cannot navigate Main Activity immediately
                        // gotToMainActivity()

                    } else {
                        Toast.makeText(this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void syncFromFirestoreAndContinue() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Error: No logged in user", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WorkoutDatabaseHelper dbHelper = new WorkoutDatabaseHelper(this);

        db.collection("users")
                .document(userId)
                .collection("exercises")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    dbHelper.clearAllExercises();   // To avoid duplicates
                    for (DocumentSnapshot doc : querySnapshot) {
                        Exercise exercise = doc.toObject(Exercise.class);
                        if (exercise != null) {
                            dbHelper.addExercise(exercise);
                        }
                    }

                    Toast.makeText(this, "Exercises synced from Firestore", Toast.LENGTH_SHORT).show();

                    // Navigate to Main Activity only after complete sync
                    goToMainActivity();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Sync failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    goToMainActivity(); // Still proceed
                });
    }

    private void registerUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter email and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                        goToMainActivity();
                    } else {
                        Toast.makeText(this, "Registration failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
