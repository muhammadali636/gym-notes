/**
 * Workout.java
 *
 * This class represents a Workout entity with an ID, name, and description.
 * It serves as a model for storing and retrieving workout information from the database.
 */

 public class Workout {
    private int id;
    private String workoutName;
    private String description;

    /**
     * Constructor to initialize a Workout object.
     *
     * @param id           The unique identifier for the workout.
     * @param workoutName  The name of the workout (e.g., "Bench Press").
     * @param description  A brief description of the workout (optional).
     */
    public Workout(int id, String workoutName, String description) {
        this.id = id;
        this.workoutName = workoutName;
        this.description = description;
    }

    /**
     * Gets the ID of the workout.
     *
     * @return The workout ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the workout.
     *
     * @return The workout name.
     */
    public String getWorkoutName() {
        return workoutName;
    }

    /**
     * Gets the description of the workout.
     *
     * @return The workout description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns a string representation of the Workout object.
     *
     * @return The workout name followed by its description if available.
     */
    @Override
    public String toString() {
        return workoutName + (description != null && !description.isBlank() ? (" - " + description) : "");
    }
}
