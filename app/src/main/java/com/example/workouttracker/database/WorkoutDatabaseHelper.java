package com.example.workouttracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.workouttracker.models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class WorkoutDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "workout.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_EXERCISES = "exercises";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SETS = "sets";
    public static final String COLUMN_REPS = "reps";
    public static final String COLUMN_WEIGHT = "weight";

    public WorkoutDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_EXERCISES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                COLUMN_SETS + " INTEGER, " +
                COLUMN_REPS + " INTEGER, " +
                COLUMN_WEIGHT + " INTEGER)";
        db.execSQL(createTable);

        // Insert default exercises to the dtb
        insertDefaultExercises(db);
    }

    // Insert default 3 exercises to the database
    private void insertDefaultExercises(SQLiteDatabase db) {
        insertExercise(db, new Exercise("Push-ups", 3, 15, 0));
        insertExercise(db, new Exercise("Squats", 3, 20, 0));
        insertExercise(db, new Exercise("Bicep Curls", 3, 12, 10));
    }

    private void insertExercise(SQLiteDatabase db, Exercise exercise) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, exercise.getName());
        values.put(COLUMN_SETS, exercise.getSets());
        values.put(COLUMN_REPS, exercise.getReps());
        values.put(COLUMN_WEIGHT, exercise.getWeight());
        db.insert(TABLE_EXERCISES, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        onCreate(db);
    }

    // Implement CRUD operations: public methods for Add, Get All, Update, and Delete
    public boolean addExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, exercise.getName());
        values.put(COLUMN_SETS, exercise.getSets());
        values.put(COLUMN_REPS, exercise.getReps());
        values.put(COLUMN_WEIGHT, exercise.getWeight());

        long result = db.insert(TABLE_EXERCISES, null, values);
        return result != -1;
    }

    public List<Exercise> getAllExercises() {
        List<Exercise> exercises = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXERCISES, null, null, null, null, null, COLUMN_NAME);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                int sets = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SETS));
                int reps = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REPS));
                int weight = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT));

                exercises.add(new Exercise(id, name, sets, reps, weight));
            } while (cursor.moveToNext());

            cursor.close();
        }

        return exercises;
    }

    public boolean updateExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SETS, exercise.getSets());
        values.put(COLUMN_REPS, exercise.getReps());
        values.put(COLUMN_WEIGHT, exercise.getWeight());

        int rows = db.update(TABLE_EXERCISES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(exercise.getId())});
        return rows > 0;
    }

    public boolean deleteExercise(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_EXERCISES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public boolean exerciseExists(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT 1 FROM " + TABLE_EXERCISES + " WHERE " + COLUMN_NAME + " = ? LIMIT 1",
                new String[]{name}
        );

        boolean exists = (cursor != null && cursor.moveToFirst());
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return exists;
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXERCISES, null, null); // Replace "exercises" with your actual table name if it's different
        db.close();
    }

    public Exercise getExerciseById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Exercise exercise = null;

        Cursor cursor = db.query(
                TABLE_EXERCISES,
                null,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            int sets = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SETS));
            int reps = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REPS));
            int weight = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT));

            exercise = new Exercise(id, name, sets, reps, weight);
            cursor.close();
        }

        return exercise;
    }


}
