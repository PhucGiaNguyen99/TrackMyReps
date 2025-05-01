package com.example.workouttracker;

import androidx.annotation.Nullable;

import com.example.workouttracker.database.WorkoutDatabaseHelper;
import com.example.workouttracker.models.Exercise;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreSyncHelper {
    private final FirebaseFirestore firestore;
    private final WorkoutDatabaseHelper dbHelper;

    public FirestoreSyncHelper(FirebaseFirestore firestore, WorkoutDatabaseHelper dbHelper) {
        this.firestore = firestore;
        this.dbHelper = dbHelper;
    }

    public void syncExercisesFromFirestore(String userId, OnSyncCompleteListener listener) {
        firestore.collection("users")
                .document(userId)
                .collection("exercises")
                .get()
                .addOnSuccessListener(querySnapshots -> {
                    for (DocumentSnapshot doc : querySnapshots) {
                        Exercise exercise = doc.toObject(Exercise.class);
                        dbHelper.addExercise(exercise);
                    }
                    listener.onComplete(true, null);
                })
                .addOnFailureListener(e -> listener.onComplete(false, e.getMessage()));
    }

    public interface OnSyncCompleteListener {
        void onComplete(boolean success, @Nullable String error);
    }
}

