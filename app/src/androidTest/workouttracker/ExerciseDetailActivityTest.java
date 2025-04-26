package com.example.workouttracker;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class ExerciseDetailActivityTest {

    @Before
    public void launchActivity() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ExerciseDetailActivity.class);
        ActivityScenario.launch(intent);
    }

    @Test
    public void testEmptyFields_showToast() {
        onView(withId(R.id.saveExerciseButton)).perform(click());
        onView(withText("Please fill in all fields"))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testNegativeValues_showToast() {
        onView(withId(R.id.exerciseNameInput)).perform(typeText("Pushup"), closeSoftKeyboard());
        onView(withId(R.id.setsInput)).perform(typeText("-2"), closeSoftKeyboard());
        onView(withId(R.id.repsInput)).perform(typeText("10"), closeSoftKeyboard());
        onView(withId(R.id.weightInput)).perform(typeText("5"), closeSoftKeyboard());

        onView(withId(R.id.saveExerciseButton)).perform(click());

        onView(withText("Values must be positive"))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testInvalidNumericInput_showToast() {
        onView(withId(R.id.exerciseNameInput)).perform(typeText("Plank"), closeSoftKeyboard());
        onView(withId(R.id.setsInput)).perform(typeText("two"), closeSoftKeyboard());
        onView(withId(R.id.repsInput)).perform(typeText("10"), closeSoftKeyboard());
        onView(withId(R.id.weightInput)).perform(typeText("0"), closeSoftKeyboard());

        onView(withId(R.id.saveExerciseButton)).perform(click());

        onView(withText("Invalid numeric input"))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }
}
