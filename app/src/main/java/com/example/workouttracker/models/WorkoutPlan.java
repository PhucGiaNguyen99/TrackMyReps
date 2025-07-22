package com.example.workouttracker.models;

import java.util.List;

public class WorkoutPlan {
    private String name;
    private List<Exercise> exercises;

    public WorkoutPlan(String name, List<Exercise> exercises) {
        this.name = name;
        this.exercises = exercises;
    }

    public String getName() {
        return name;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public void addExercise(Exercise exercise) {
        this.exercises.add(exercise);
    }

    public void removeExerciseByName(String exerciseName) {
        exercises.removeIf(ex -> ex.getName().equalsIgnoreCase(exerciseName));
    }

    public void updateExerciseByName(String name, Exercise newExercise) {
        for (int i = 0; i < exercises.size(); i++) {
            if (exercises.get(i).getName().equalsIgnoreCase(name)) {
                exercises.set(i, newExercise);
                break;
            }
        }
    }

    public boolean containsExercise(String exerciseName) {
        for (Exercise ex : exercises) {
            if (ex.getName().equalsIgnoreCase(exerciseName)) return true;
        }
        return false;
    }
}
