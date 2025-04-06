package com.example.workouttracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workouttracker.models.Exercise;

import java.util.List;

public class WorkoutSelectionAdapter extends RecyclerView.Adapter<WorkoutSelectionAdapter.ViewHolder> {
    private List<Exercise> exercises;
    private boolean[] checked;

    public WorkoutSelectionAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
        this.checked = new boolean[exercises.size()];
    }

    public List<Exercise> getSelectedExcercises() {
        List<Exercise>  selected = new java.util.ArrayList<>();
        for (int i = 0; i < exercises.size(); i++) {
            if (checked[i]) {
                selected.add(exercises.get(i));
            }
        }
        return selected;
    }

    @NonNull
    @Override
    public WorkoutSelectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise_checkbox, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutSelectionAdapter.ViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);

        holder.nameText.setText(exercise.getName());

        holder.checkbox.setOnCheckedChangeListener(null); // clear old listener
        holder.checkbox.setChecked(checked[position]);    // set correct value

        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checked[holder.getAdapterPosition()] = isChecked; // always get current adapter position
        });

    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        CheckBox checkbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.exerciseNameCheckbox);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }
}
