TrackMyReps

TrackMyReps is a fitness-focused Android app designed to help users plan workouts, manage exercises, and stay on top of their fitness routine. It offers a clean, motivating UI and securely syncs user data using Firebase Authentication and Firestore, while supporting offline access with a local SQLite database.

Features

Exercise Management
- Add new exercises with:
   +  Unique name (required)
   +  Optional sets, reps, and weight fields (default to 0 if empty)
- Validate input with helpful hints and error checking
- View all exercises in a vertically scrolling list with clean UI cards
- Tap to view/edit exercise details
- Long-press to delete with confirmation
- Automatically includes 3 default exercises on first launch

Workout Planning
- Select multiple exercises to create a new workout plan
- Confirm selected items before saving the plan
- Only one plan is active at a time
- Plan is remembered via a global manager, even after screen rotation

Workout Session Tracking
- Display the selected workout plan as a to-do list
- Mark exercises as complete via a button with confirmation
- Remove completed items dynamically
- Show "Workout Completed!" toast when all items are marked

Technical Overview

Tech Stack
- Language: Java
- Platform: Android SDK
- Local Database: SQLite (via SQLiteOpenHelper)
- Remote Database: Firebase Firestore
- Authentication: Firebase Auth
- UI Components: RecyclerView, Material Design Buttons, TextInputLayouts, CardView

Key Components
- models/Exercise.java: Model class for exercise
- WorkoutDatabaseHelper.java: Manages local DB with CRUD + default values
- WorkoutPlanManager.java: Global state manager for workout plan
- ExerciseAdapter.java: Binds exercise list to RecyclerView
- WorkoutPlanAdapter.java: Tracks session list with completion

Data Persistence & Syncing
- SQLite stores exercises locally for fast access and offline support
- Firestore stores per-user data for backup and cross-device access
- Firestore sync happens at login:
- Download all exercises for user from Firestore
- Insert them into local SQLite (refreshing local state)
- Firestore is updated after any local update:
- Only update Firestore after a successful local SQLite insert/update/delete

Input & Error Handling
- Input fields support validation and numeric-only checks
- Toast messages guide the user for successful and failed actions
- Confirmation dialogs are used for sensitive actions like delete

Firebase Integration

Authentication
- Uses Firebase Auth for secure login
- Data is linked per user via their userId

Firestore Structure
- Path: users/{userId}/exercises/{exerciseName}
- Each user's exercises are stored as separate documents

Offline Support
- Firestore reads fallback to local cache when offline
- SQLite provides primary access when network is unavailable
- Upon next login or refresh, Firestore updates are synced

Real-Time Firestore Handling
- On Add/Edit:
   + Update SQLite immediately
   + Then update Firestore (asynchronously)
- On Delete:
   + Remove from SQLite
   + Then delete from Firestore

Future Improvements
- Allow users to name and save multiple workout plans
- Track completion history with dates and stats

- Add progress charts and analytics
- Enable dark mode and color theme options
- Add optional profile with display name and profile picture

Credits

© 2025 Phuc Nguyen
Built with dedication and a mission to simplify fitness tracking.
