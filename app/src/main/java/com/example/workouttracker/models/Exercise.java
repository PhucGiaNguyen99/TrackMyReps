package com.example.workouttracker.models;

import java.io.Serializable;

public class Exercise implements Serializable {
    private int id;
    private String name;
    private int sets;
    private int reps;
    private int weight;

    // Added the no-arg constructor for Firestore
    public Exercise() {
    }

    public Exercise(int id, String name, int sets, int reps, int weight) {
        this.id = id;
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }

    public Exercise(String name, int sets, int reps, int weight) {
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
