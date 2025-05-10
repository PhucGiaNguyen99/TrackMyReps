package com.example.workouttracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workouttracker.models.Exercise;

import java.util.List;

public class WorkoutSelectionAdapter extends RecyclerView.Adapter<WorkoutSelectionAdapter.ViewHolder> {
    private List<Exercise> exercises;

    // Changed to allow multiple instances of an item
    // private boolean[] checked;
    int[] quantities;

    public WorkoutSelectionAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
        this.quantities = new int[exercises.size()];
    }

    public List<Exercise> getSelectedExercises() {
        List<Exercise>  selected = new java.util.ArrayList<>();
        for (int i = 0; i < exercises.size(); i++) {
            for (int j = 0; j < quantities[i]; j++) {
                selected.add(exercises.get(i));
            }
        }
        return selected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise_checkbox, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);

        holder.nameText.setText(exercise.getName());

        holder.increaseBtn.setOnClickListener(v -> {
            quantities[holder.getAdapterPosition()]++;
            holder.quantityText.setText(String.valueOf(quantities[holder.getAdapterPosition()]));
        });

        holder.decreaseBtn.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (quantities[pos] > 0) {
                quantities[pos]--;
                holder.quantityText.setText(String.valueOf(quantities[pos]));
            }
        });

    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, quantityText;
        Button increaseBtn, decreaseBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.exerciseNameCheckbox);
            // checkbox = itemView.findViewById(R.id.checkbox);
            quantityText = itemView.findViewById(R.id.quantityText);
            increaseBtn = itemView.findViewById(R.id.increaseButton);
            decreaseBtn = itemView.findViewById(R.id.decreaseButton);
        }
    }

    // For testing
    public void setQuantity(int index, int value) {
        if (index >= 0 && index < quantities.length) {
            quantities[index] = Math.max(0, value);
        }
    }

}
