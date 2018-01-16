package eh.workout.journal.com.workoutjournal.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.Exercise;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseCategory;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseCategoryRelation;

@Dao
public abstract class ExerciseDao {
    @Query("SELECT * FROM exercise_categories")
    public abstract LiveData<List<ExerciseCategoryRelation>> getExerciseCategoryRelations();

    @Insert
    public abstract void insertExerciseCategories(List<ExerciseCategory> exercises);

    //ExerciseCategoryRelation
    @Query("SELECT * FROM exercise_categories")
    public abstract LiveData<List<ExerciseCategory>> getExerciseCategories();

    @Insert
    public abstract void insertExercises(List<Exercise> exercises);

    @Query("SELECT * FROM exercise")
    public abstract LiveData<List<Exercise>> getExercises();


}
