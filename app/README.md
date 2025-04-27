Project 3: Adding Firebase Authentication and Firestore Cloud Storage

** 1. Integrate Firebase Authentication:
- Firebase Authentication is used to enable secure user account management.
- Key features:
    + Register using their email and password.
    + Log in to access their personalized exercise data.
    + Maintain sessions so users remain signed in across app restarts.
    + Log out when needed to switch accounts or end their session.

- All protected activities check for a valid authenticated session before allowing access,
ensuring that user data remains private and secure.

- Detailed Implementation:
    + LoginActivity.java: create function signInWithEmailAndPassword(email, password). Redirects to MainActivity on success.
    + RegisterActivity.java: create function createUserWithEmailAndPassword(email, password). Redirects to sign in on success.
    + MainActivity.java: On startup, checks if user is authenticated. Also include a Logout button.

Setup Firebase:
- Created the project on Firebase Console with package name. 
- Downloaded and added the google-services.json to the project. 
- Chose Email/Password for Sign-in method.
- Implemented the AuthenticationActivity and xml for register and sign in.
- Modified the IntroActivity to direct from here to MainActivity.
- Handled the two buttons, validate input, call Firebase Authenticatioon to create or sign in users, and redirects users to MainActivity after successful login/registration.
- Validation of input for email and password: Firebase checked:
  + Checks if the email is a valid email format (like abc@domain.com).
  + Checks if the password meets minimum strength (for example: usually minimum 6 characters for email/password auth).
  + Prevents duplicate accounts with the same email.
  + Handles error cases like wrong password, user not found, email already in use, etc.
- We need to check if inputs are empty, give instant feedback, and any specific rules for password.
- Auto-skip AuthenticationActivity if a user is already logged in:
  + Check if user is already logged in. When a user signs in successfully, Firebase saves their session. when openning the app later, it automatically restores the session. getCurrentUser() returns the saved user if session is active.
  + If yes, skip AuthenticationActivity and go straight to MainActivity.
  + Otherwise, stay on AuthenticationActivity and let them log in.
** Added a quick loading spinner when the app opens AuthenticationActivity.
- Added logout button in MainActivity:
  + Signs them out of Firebase Authentication.
  + Sends them back to the AuthenticationActivity.
  + Clears the backstack so they cannot press back to MainActivity.
  
** 2. Cloud Firestore:
- Firestore will be used to extend the app's functionality by storing workout data in the cloud. This integration will support:
- Per-user exercise lists stored under secure, user-specific collections.
- Cloud backup and multi-device access, ensuring users can retrieve their data from any device.
- Real-time syncing between local and cloud data to enhance flexibility and data persistence.
- Seamless integration with existing features, allowing workout plans and completed sessions to be uploaded and retrieved without disrupting the local flow.

- Key features:
    + Add Exercise to Firestore.
    + Sync Workout Plan per User.
    + Store per-user collections using UID.
    + Sync with local SQLite for real-time cloud backup.

- Detailed Implementation:
    + WorkoutDatabaseHelper.java: Wrapped in Firestore.
    + WorkoutPlanManager.java: Extended to sync selected exercises with Firestore.
    + MainActivity.java: Modified to fetch workout from Firestore instead of SQLite.

- After adding successfully to SQLite, upload the exercise to Firestore. Each exercise is saved as a document in the "exercises" collection in Firestore.
- Download the saved exercises from Firestore and insert into local SQLite database, which ensures even if user use a different device, their saved exercises are still there.
  + After user successfully logs in: Retrieves the exercises from Firestore to the local Sqlite
    . Connect to Firestore.
    . Query the exercises collection.
    . For each document, create an Exercise object.
    . Insert into local SQLite database using WorkoutDatabaseHelper.
  + Improved: Only retrieved from Firestore and stored to the local when the local is empty to avoid
duplicate -> NEED TO FIGURE OUT WHEN TO DO AND WHEN DO NOT!!!!!!!
  + 