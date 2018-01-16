
package eh.workout.journal.com.workoutjournal.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseCategory;

public class ExerciseCategories {

    @SerializedName("exercise_category")
    @Expose
    private List<ExerciseCategory> exerciseCategory = null;

    public List<ExerciseCategory> getExerciseCategory() {
        return exerciseCategory;
    }

    public void setExerciseCategory(List<ExerciseCategory> exerciseCategory) {
        this.exerciseCategory = exerciseCategory;
    }
}
