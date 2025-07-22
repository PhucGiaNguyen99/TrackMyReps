package com.example.workouttracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.workouttracker.models.Exercise;
import com.example.workouttracker.models.WorkoutPlan;

import java.util.List;

public class WorkoutPlanAdapter extends ArrayAdapter<WorkoutPlan> {

    private final Context context;
    private final List<WorkoutPlan> plans;

    public WorkoutPlanAdapter(Context context, List<WorkoutPlan> plans) {
        super(context, 0, plans);
        this.context = context;
        this.plans = plans;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WorkoutPlan plan = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_workout_plan, parent, false);
        }

        TextView exerciseText = convertView.findViewById(R.id.exerciseText);
        Button completeBtn = convertView.findViewById(R.id.completeButton);

        exerciseText.setText(plan.getName());
        return convertView;
    }
}
