package com.example.workouttracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.workouttracker.models.Exercise;

import java.util.List;

public class WorkoutPlanAdapter extends ArrayAdapter<Exercise> {

    private final Context context;
    private final List<Exercise> exercises;

    public WorkoutPlanAdapter(Context context, List<Exercise> exercises) {
        super(context, 0, exercises);
        this.context = context;
        this.exercises = exercises;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Exercise exercise = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_workout_plan, parent, false);
        }

        TextView exerciseText = convertView.findViewById(R.id.exerciseText);
        Button completeBtn = convertView.findViewById(R.id.completeButton);

        exerciseText.setText(exercise.getName() + " - " +
                exercise.getSets() + " sets x " +
                exercise.getReps() + " reps, " +
                exercise.getWeight() + " lbs");

        completeBtn.setOnClickListener(v -> {
            if (context instanceof WorkoutSelectionActivity) {
                ((WorkoutSelectionActivity) context).showConfirmationDialog(exercise);
            }
        });

        return convertView;
    }
}
