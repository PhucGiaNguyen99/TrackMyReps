TrackMyReps
TrackMyReps is a workout planning and tracking Android app built for users who want to manage their exercises and stay on top of their fitness routine. The app allows users to create and edit exercises, build a workout plan, and track completion during a session.

📱 Features
🏋️ Exercise Management
Add new exercises with:

Name (must be unique)

Sets, Reps, Weight (numeric-only with validation)

Input hints are provided for user guidance

View exercises in a scrollable grid layout (2-column)

Tap an exercise to view/edit its details

Long-press an exercise to delete it with confirmation

3 default exercises are added on first launch

📓 Workout Planning
Select multiple exercises from the list to create a workout plan

Confirm the selected items before creating the plan

Only one plan is tracked at a time

Plan is remembered using a global manager, even if the screen rotates

✅ Workout Session Tracking
View the selected workout plan as a list

Each item includes a “Complete” button with confirmation dialog

When marked complete, exercises are removed from the list

When all items are completed, a “Workout Completed!” toast appears

⚙️ Technical Overview
🛠 Tech Stack
Language: Java

Platform: Android SDK

Database: SQLite with SQLiteOpenHelper

UI Components: RecyclerView, ListView, AlertDialogs, ScrollView, EditText, Buttons

📂 Key Components
models/Exercise.java: Model class for exercise details

WorkoutDatabaseHelper.java: Handles database creation, default values, and CRUD

WorkoutPlanManager.java: Static helper to store the current selected workout plan

ExerciseAdapter.java: Used in MainActivity for displaying all exercises

WorkoutPlanAdapter.java: Used in session tracking to display and mark items as complete

🔄 Data Persistence & Rotation Handling
SQLite is used for storing exercises (data persists across app restarts)

Exercises are reloaded on resume in the main screen

WorkoutPlanManager ensures session plans survive orientation changes

🧠 Input & Error Handling
Input fields only accept proper numeric values

Validation and error checking prevent crashes

Confirmation dialogs for deletion and workout planning

💡 Future Improvements
Save and name multiple workout plans

Add completed date tracking & session history

Add progress graphs and analytics

Support for dark mode and themes


** Save new exercise to SQLite locally first, then save it to Firestore for each user **
- Path: "users/{userId}/exercises/{exerciseName}"
