
package eh.workout.journal.com.workoutjournal.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import eh.workout.journal.com.workoutjournal.db.entinty.Exercise;

public class Exercises {

    @SerializedName("exercises")
    @Expose
    private List<Exercise> exercises = null;

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

}
