package eh.workout.journal.com.workoutjournal.db.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseGroupEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;

@Dao
public abstract class ExerciseLiftDao {

    /**
     * Groups
     */
    @Insert
    public abstract void insertExerciseGroupList(List<ExerciseGroupEntity> exerciseEntities);

    @Query("SELECT * FROM exercise_group_entities")
    public abstract LiveData<List<ExerciseGroupEntity>> getAllExercisesGroups();

    /**
     * Exercises
     */
    @Insert
    public abstract void insertExercises(ExerciseLiftEntity... exerciseEntities);

    @Insert
    public abstract void insertExerciseList(List<ExerciseLiftEntity> exerciseEntities);

    @Update
    public abstract void updateExercises(ExerciseLiftEntity... exerciseEntities);

    @Query("SELECT * FROM exercise_lift_entities  ORDER BY name")
    public abstract LiveData<List<ExerciseLiftEntity>> getAllExercises();

    @Transaction
    public void insertGroupAndExercises(List<ExerciseGroupEntity> exerciseGroupEntities, List<ExerciseLiftEntity> exerciseLiftEntities) {
        insertExerciseGroupList(exerciseGroupEntities);
        insertExerciseList(exerciseLiftEntities);
    }
}
