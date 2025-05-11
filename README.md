# TrackMyReps
**By Phuc Nguyen**

**TrackMyReps** is a fitness-focused Android app designed to help users plan workouts, manage exercises, and stay on top of their fitness routine. It offers a clean, motivating UI and securely syncs user data using Firebase Authentication and Firestore, while supporting offline access with a local SQLite database.

---

## Features

### Exercise Management

- Add new exercises with:
  - Unique name (required)
  - Optional sets, reps, and weight fields (default to 0 if empty)
- Validate input with helpful hints and error checking
- View all exercises in a vertically scrolling list using clean UI cards
- Tap to view or edit exercise details
- Long-press to delete with confirmation
- Automatically includes 3 default exercises on first launch

### Workout Planning

- Select multiple exercises to create a new workout plan
- Confirm selected items before saving the plan
- Only one plan is active at a time
- Plan state is remembered via a global manager (survives screen rotation)

### Workout Session Tracking

- Display the selected workout plan as a to-do list
- Mark exercises as complete with a confirmation button
- Remove completed items dynamically
- Show “Workout Completed!” toast when all items are marked

---

## Technical Overview

### Tech Stack

| Component        | Technology                    |
|------------------|-------------------------------|
| Language         | Java                          |
| Platform         | Android SDK                   |
| Local Database   | SQLite (via SQLiteOpenHelper) |
| Remote Database  | Firebase Firestore            |
| Authentication   | Firebase Authentication       |
| UI Components    | RecyclerView, Material Design Buttons, TextInputLayouts, CardView |

### Key Components

- `models/Exercise.java`: Model class for exercise data
- `WorkoutDatabaseHelper.java`: Manages local database CRUD operations and default values
- `WorkoutPlanManager.java`: Manages global state for current workout plan
- `ExerciseAdapter.java`: Binds exercises to the RecyclerView list
- `WorkoutPlanAdapter.java`: Manages the session list and item completion

---

## Data Persistence and Syncing

### Local (SQLite)

- Stores all exercise data for fast access and offline support

### Remote (Firestore)

- Stores user-specific exercise data
- Automatically syncs on login:
  - Downloads all user exercises from Firestore
  - Refreshes local SQLite database

### Update Strategy

- Firestore is updated *only after* local changes succeed
  - Add/Edit:
    - Update SQLite immediately
    - Then update Firestore asynchronously
  - Delete:
    - Remove from SQLite
    - Then delete from Firestore

---

## Input and Error Handling

- Input fields use validation and numeric-only constraints
- Toast messages provide feedback on user actions
- Confirmation dialogs are used for delete actions

---

## Firebase Integration

### Authentication

- Uses Firebase Auth for secure login
- All user data is scoped by `userId`

### Firestore Structure

- Path: `users/{userId}/exercises/{exerciseName}`
- Each exercise is stored as a document under the user's collection

### Offline Support

- Firestore reads fallback to local cache when offline
- SQLite provides full offline functionality
- On login or manual refresh, Firestore is synced with the latest local changes

---

## Recent Update (as of 05/10)

- Modified the app to allow users to add **multiple instances** of the same item to a workout plan

---

## Future Improvements

- Support multiple named workout plans
- Track workout completion history with dates and performance stats
- Add visual progress charts and analytics
- Enable dark mode and custom color themes
- Add optional user profile with name and picture

---

## Credits

© 2025 Phuc Nguyen  
Built with dedication and a mission to simplify fitness tracking.
