package com.example.workouttracker;

import android.app.Instrumentation;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.workouttracker.database.WorkoutDatabaseHelper;
import com.example.workouttracker.models.Exercise;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.intent.Intents.intended;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class PlanWorkoutActivityTest {

    @Before
    public void setup() {
        Intents.init();

        // Preload data if needed
        WorkoutDatabaseHelper db = new WorkoutDatabaseHelper(
                androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext());
        db.clearDatabase();  // optional
        db.addExercise(new Exercise("Push-up", 3, 10, 0));
        db.addExercise(new Exercise("Squat", 3, 12, 0));

        ActivityScenario.launch(PlanWorkoutActivity.class);
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testSelectedExercisesArePassed() {
        // Select first and second items
        onView(withText("Push-up")).perform(click());
        onView(withText("Squat")).perform(click());

        // Click create plan
        onView(withId(R.id.createPlanButton)).perform(click());

        // Confirm the dialog
        onView(withText("Yes"))
                .inRoot(isDialog()) // Ensure it searches inside dialog window
                .check(matches(isDisplayed())) // Wait for it to be visible
                .perform(click()); // Then click


        // Verify that an intent was sent to MainActivity
        intended(IntentMatchers.hasComponent(MainActivity.class.getName()));
    }
}
