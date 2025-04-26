package com.example.workouttracker;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.workouttracker.database.WorkoutDatabaseHelper;
import com.example.workouttracker.models.Exercise;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class WorkoutDatabaseHelperTest {

    private WorkoutDatabaseHelper dbHelper;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new WorkoutDatabaseHelper(context);
        dbHelper.clearDatabase(); // Optional helper method you can add to clear data for clean tests
    }

    @After
    public void tearDown() {
        dbHelper.close();
    }

    @Test
    public void testAddExercise() {
        Exercise exercise = new Exercise("Push Ups", 3, 12, 0);
        boolean added = dbHelper.addExercise(exercise);
        assertTrue(added);
    }

    @Test
    public void testPreventDuplicateExerciseName() {
        Exercise e1 = new Exercise("Push Ups", 3, 12, 0);
        Exercise e2 = new Exercise("Push Ups", 4, 10, 5);
        dbHelper.addExercise(e1);
        boolean added = dbHelper.addExercise(e2);
        assertFalse(added);
    }

    @Test
    public void testDeleteExercise() {
        Exercise exercise = new Exercise("Squats", 3, 10, 20);
        dbHelper.addExercise(exercise);
        List<Exercise> list = dbHelper.getAllExercises();
        assertFalse(list.isEmpty());

        boolean deleted = dbHelper.deleteExercise(list.get(0).getId());
        assertTrue(deleted);
    }

    @Test
    public void testUpdateExercise() {
        Exercise exercise = new Exercise("Lunges", 3, 10, 15);
        dbHelper.addExercise(exercise);

        Exercise stored = dbHelper.getAllExercises().get(0);
        stored.setReps(20);
        stored.setWeight(25);

        boolean updated = dbHelper.updateExercise(stored);
        assertTrue(updated);

        Exercise updatedFromDb = dbHelper.getExerciseById(stored.getId());
        assertEquals(20, updatedFromDb.getReps());
        assertEquals(25, updatedFromDb.getWeight());
    }

    @Test
    public void testGetAllExercises() {
        dbHelper.addExercise(new Exercise("Crunches", 3, 15, 0));
        dbHelper.addExercise(new Exercise("Planks", 2, 1, 0));

        List<Exercise> exercises = dbHelper.getAllExercises();
        assertTrue(exercises.size() >= 2);
    }
}
