Overview
GymNotes is a Java based desktop app used to help users in managing weight and workout routines. This app enables users to add new workouts (like a push, pull, or leg day), track their fitness progress in terms of their weight, log workouts and view their progress..

The GymNotes application is a repurposed version of a previous class project called DiscussionBoard.java. While DiscBoard served for discussion, GymNotes specializes towards fitness, maintaining a similar style for the GUI and logic. 

Project Structure
Ensure that the project structure is organized as follows:

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
1. Open the terminal, go to the project directory, and run the following command:
    javac -cp "lib/sqlite-jdbc-3.47.1.0.jar" -d bin src/*.java
Ensure that the manifest.txt file contains the following content:
Manifest-Version: 1.0
Main-Class: GymNotes
Class-Path: lib/sqlite-jdbc-3.47.1.0.jar
Important: Verify that there is a newline at the end of the manifest file. IF there is no newline, the database will not be connected and thats a problem with java. 

Create the JAR file using the following command (OPTIONAL BUT HANDY):
    jar cfm GymNotes.jar manifest.txt -C bin/ .

Running the Application
    OPTION A: Running Manually via Terminal
        java -cp "GymNotes.jar:lib/sqlite-jdbc-3.47.1.0.jar" GymNotes
    OPTION B: Running by Double-Clicking the JAR
        To run the app by clicking the GymNotes.jar file:

Functional Testing
1. Launch the app by double-clicking the JAR file. Verify that the GymNotes app starts without errors.
2. Launch the app from the terminal. 
3. Add a new workout note and confirm that it is saved and displayed in the notes list.
4. View existing workout notes to ensure all saved notes are displayed properly.
5. Edit an existing workout note and check that the changes are saved and reflected in the notes list.
6. Delete a workout note and ensure that it is removed from the notes list.
7. Attempt to save a workout note with empty fields and verify that the app displays an appropriate error message.
8. Search for a specific workout note and confirm that the app displays the matching notes correctly.
9. Click search with empty search fields, it should display everything. 

FUTURE:
Perhaps in the future we can use a more modern GUI then swing. Also can develop a web app using java spring. 