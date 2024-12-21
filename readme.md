Overview
    GymNotes is a Java-based desktop app that helps users manage their weight and workout routines. 
    The app allows users to add new workouts (like push, pull, or leg day), log workouts, and track fitness progress. 
    It includes a HashMap-based feature for efficient search and retrieval of workout logs, making it easy to view progress and stay organized.
Project Structure
    check that the project structure is organized as follows:

    GymNotes/
        GymNotes.jar
        lib/
            sqlite-jdbc-3.47.1.0.jar
        manifest.txt
        bin/
            compiled class files
        src/
            GymNotes.java
            Workout.java
            WorkoutLog.java


How to Use GymNotes:

    After launching the app, a welcome screen will appear along with a menu on the top left. The menu contains these options:
    1. Adding Workout Notes: Input details regarding workouts, such as exercises performed, duration, and additional performance notes.
    2. Viewing Existing Notes: Browse through saved workouts to monitor progress over time.
    3. Editing or Deleting Entries: Modify existing notes or remove them as needed.
    4. Saving Data: GymNotes uses SQLite as a lightweight database to securely store workout notes, ensuring data can be accessed whenever necessary.

    Compilation Steps
    To compile the application and create an executable JAR file, follow these steps:
        Make sure that the manifest.txt file contains the following content:
        Manifest-Version: 1.0
        Main-Class: GymNotes
        Class-Path: lib/sqlite-jdbc-3.47.1.0.jar
        IMPORTANT: make sure that there is a newline at the end of the manifest file. IF there is no newline, the database will not be connected and thats a problem with java. 


    1. Compilation: Open the terminal, go to the project directory, and run the following command:
        javac -cp "lib/sqlite-jdbc-3.47.1.0.jar" -d bin src/*.java
    2. Create the JAR file using the following command 
        jar cfm GymNotes.jar manifest.txt -C bin/ .
    3. Running the App
        To run the app by clicking the GymNotes.jar file:

Testing
    1. Launch app by double-clicking the JAR file. Verify that the GymNotes app starts without errors.
    2. Launch app from the terminal. 
    3. Add a new workout note and confirm that it is saved and displayed in the notes list.
    4. see existing workout notes to ensure all saved notes are displayed properly.
    5. Edit an existing workout note and check that the changes are saved and reflected in the notes list.
    6. Delete a workout note and ensure that it is removed from the notes list.
    7. Attempt to save a workout note with empty fields and verify that the app displays an appropriate error message.
    8. Search for a specific workout note and confirm that the app displays the matching notes correctly.
    9. Click search with empty search fields, it should display everything. 

LIMITS:
    1. Single User Only.
    2. Data Stored Locally: All data is saved on the user's computer. 
    3. Overly simple and outdated design: limited by java swing. 
    4. Limited Progress Analysis: The app tracks workouts and progress but doesn't have charts or progress in individual exercises.
    5. No Backup or Export: no option to save data elsewhere or export it to a file (for future). 

FUTURE:
    Perhaps in the future we can use a more modern GUI then swing, or turn this into a web app using java spring. For the latter, we can use Javascript/HTML/CSS for the 
    UI. Also we would need a better and more powerful database then Sqlite. Lastly, we should record what weight the user is lifting for each exercise of the workout type. 
    Perhaps we can also make a mobile app using springboot with react native. 