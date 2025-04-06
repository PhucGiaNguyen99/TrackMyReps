package com.example.workouttracker;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workouttracker.models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class WorkoutSessionActivity extends AppCompatActivity {
    private List<Exercise> workoutList;
    private ExerciseAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_session);

        workoutList = (ArrayList<Exercise>) getIntent().getSerializableExtra("selected_exercises");

        RecyclerView recyclerView = findViewById(R.id.sessionRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ExerciseAdapter(workoutList, new ExerciseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Exercise exercise) {
                Toast.makeText(WorkoutSessionActivity.this, "Tap and hold to complete", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(Exercise exercise) {
                workoutList.remove(exercise);
                adapter.notifyDataSetChanged();
                if (workoutList.isEmpty()) {
                    Toast.makeText(WorkoutSessionActivity.this, "Workout Completed!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        recyclerView.setAdapter(adapter);
    }
}
