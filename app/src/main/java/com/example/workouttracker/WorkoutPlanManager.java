// WorkoutPlanManager.java
package com.example.workouttracker;

import com.example.workouttracker.models.Exercise;
import java.util.ArrayList;
import java.util.List;

public class WorkoutPlanManager {
    private static final List<Exercise> selectedExercises = new ArrayList<>();

    public static void setPlan(List<Exercise> exercises) {
        selectedExercises.clear();
        selectedExercises.addAll(exercises);
    }

    public static List<Exercise> getPlan() {
        return selectedExercises;
    }

    public static void clearPlan() {
        selectedExercises.clear();
    }

    public static boolean hasPlan() {
        return !selectedExercises.isEmpty();
    }

    public static void removeExercise(Exercise e) {
        selectedExercises.remove(e);
    }

}
