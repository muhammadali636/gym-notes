/**
 * WorkoutLog.java
 *
 * This class represents a WorkoutLog entity with an ID, associated workout ID,
 * log date, weight used for the exercise, and the user's actual weight.
 */
public class WorkoutLog {
    private int id;
    private int workoutId;
    private String logDate; 
    private String weightUsed; // Can be "N/A" if not provided
    private String userWeight; // User's actual weight, can be "N/A"

    /**
     * Constructor to initialize a WorkoutLog object.
     *
     * @param id          The unique identifier for the workout log.
     * @param workoutId   The ID of the associated workout.
     * @param logDate     The date when the workout was performed (format: YYYY-MM-DD).
     * @param weightUsed  The weight used during the workout (optional).
     * @param userWeight  The user's actual weight (optional).
     */
    public WorkoutLog(int id, int workoutId, String logDate, String weightUsed, String userWeight) {
        this.id = id;
        this.workoutId = workoutId;
        this.logDate = logDate;
        this.weightUsed = weightUsed;
        this.userWeight = userWeight;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public String getLogDate() {
        return logDate;
    }

    public String getWeightUsed() {
        return weightUsed;
    }

    public String getUserWeight() {
        return userWeight;
    }

    @Override
    public String toString() {
        return "Date: " + logDate + ", Workout ID: " + workoutId + ", Weight Used: " + weightUsed + ", User Weight: " + userWeight;
    }
}
