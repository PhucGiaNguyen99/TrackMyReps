// WorkoutPlanManager.java
package com.example.workouttracker;

import com.example.workouttracker.models.Exercise;
import com.example.workouttracker.models.WorkoutPlan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WorkoutPlanManager {
    private static final List<WorkoutPlan> plans = new ArrayList<>();

    public static void addPlan(WorkoutPlan plan) {
        plans.add(plan);
    }

    public static List<WorkoutPlan> getPlans() {
        return plans;
    }

    public static WorkoutPlan getPlanByName(String name) {
        for (WorkoutPlan plan : plans) {
            if (plan.getName().equals(name)) return plan;
        }
        return null;
    }

    // UPDATE WITH NEW NAME
    public static boolean updatePlanName(String oldName, String newName) {
        WorkoutPlan plan = getPlanByName(oldName);
        if (plan != null) {
            plan.setName(newName);
            return true;
        }
        return false;
    }

    // UPDATE WITH NEW EXERCISE
    public static boolean updatePlanItem(String planName, List<Exercise> newExercises) {
        WorkoutPlan plan = getPlanByName(planName);
        if (plan != null) {
            plan.setExercises(newExercises);
            return true;
        }
        return false;
    }

    // DELETE BY NAME
    public static boolean deletePlan(String name) {
        Iterator<WorkoutPlan> iterator = plans.iterator();
        while (iterator.hasNext()) {
            WorkoutPlan plan = iterator.next();
            if (plan.getName().equalsIgnoreCase(name)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    // CLEAR ALL PLANS
    public static void clearAllPlans() {
        plans.clear();
    }

}
