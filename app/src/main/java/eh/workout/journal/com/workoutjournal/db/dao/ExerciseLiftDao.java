package eh.workout.journal.com.workoutjournal.db.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;

@Dao
public interface ExerciseLiftDao {
    @Insert
    void insertExercises(ExerciseLiftEntity... exerciseEntities);

    @Insert
    void insertExerciseList(List<ExerciseLiftEntity> exerciseEntities);

    @Update
    void updateExercises(ExerciseLiftEntity... exerciseEntities);

    @Query("SELECT * FROM exercise_lift_entities  ORDER BY name")
    LiveData<List<ExerciseLiftEntity>> loadAllExercises();

    @Query("select * FROM exercise_lift_entities WHERE id = :id")
    ExerciseLiftEntity loadExercise(String id);
}
