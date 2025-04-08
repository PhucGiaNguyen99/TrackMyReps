package com.example.workouttracker;

import com.example.workouttracker.models.Exercise;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class WorkoutSelectionAdapterTest {

    private WorkoutSelectionAdapter adapter;

    @Before
    public void setUp() {
        List<Exercise> mockExercises = Arrays.asList(
                new Exercise("Push-ups", 3, 10, 0),
                new Exercise("Squats", 4, 15, 0),
                new Exercise("Lunges", 3, 12, 0)
        );
        adapter = new WorkoutSelectionAdapter(mockExercises);
        adapter.setChecked(1, true); // Only select "Squats"
    }

    @Test
    public void testGetSelectedExercises_returnsCorrectItems() {
        List<Exercise> selected = adapter.getSelectedExercises();
        assertEquals(1, selected.size());
        assertEquals("Squats", selected.get(0).getName());
    }
}
