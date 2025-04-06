# TrackMyReps

**TrackMyReps** is a workout planning and tracking Android app built for users who want to manage their exercises and stay on top of their fitness routine. The app allows users to **create their own exercises**, **build a workout from them**, and **check off completed exercises during a session**.

---

## 📱 Features

### 🏋️ Exercise Management
- Add new exercises with fields for:
  - Name (must be unique)
  - Number of sets
  - Reps per set
  - Weight (optional, defaults to 0)
- View the list of all exercises in **a scrollable list**
- Tap an exercise to **view and edit its details** (except the name)
- **Long press** on an exercise to **delete** it (with confirmation)
- App **includes 3 default exercises** on first launch

### 📓 Workout Planning
- Select multiple exercises from the existing list to **build a custom workout session**
- Use **checkboxes to easily pick which exercises to include**

### ✅ Workout Session Tracking
- **View the planned workout** as a list
- Long-press exercises to **mark them as completed** (they get removed from the list)
- When all exercises are completed, **a message confirms workout completion**

---

## ⚙️ Technical Overview

### 🛠 Tech Stack
- **Language:** Java
- **Platform:** Android SDK
- **Database:** SQLite (with `SQLiteOpenHelper`)
- **UI Components:** RecyclerView, AlertDialogs, ScrollView, LinearLayout, Buttons, EditTexts

### 📂 Project Structure
- `models/Exercise.java`: Model class to store exercise info
- `database/WorkoutDatabaseHelper.java`: Handles database creation, insert/update/delete operations
- `activities/`:
  - `**MainActivity.java**`: Shows all exercises, navigation entry point
  - `**AddExerciseActivity.java**`: Form to add new exercises
  - `**ExerciseDetailActivity.java**`: Lets users view/edit sets, reps, weight
  - `**PlanWorkoutActivity.java**`: Select exercises for a session
  - `**WorkoutSessionActivity.java**`: Track progress during the workout

### 🔄 Data Persistence
- Uses SQLite to store exercise data locally
- CRUD operations are implemented manually through SQL and ContentValues
- Data persists across app launches

### 🧠 Input Handling
- Basic input validation for all fields (e.g., required, number-only)
- Duplicate exercise names are not allowed
- Confirmation dialogs to prevent accidental deletion

---

## 💡 Future Improvements
- Save past workout sessions and history
- Add date tracking for completed exercises
- Support for dark mode and theme customization
- Option to categorize or group exercises by body part

---

## 🎯 Why This App
This project was built to practice working with multiple activities, RecyclerViews, and local databases in Android. It also demonstrates good practices with user input handling, screen navigation, and database management.
