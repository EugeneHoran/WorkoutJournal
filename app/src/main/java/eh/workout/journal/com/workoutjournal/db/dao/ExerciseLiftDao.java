package eh.workout.journal.com.workoutjournal.db.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
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

    @Delete
    void deleteExercises(ExerciseLiftEntity... exerciseEntities);


    @Query("SELECT * FROM ExerciseLiftEntity WHERE recent = :isRecent")
    LiveData<List<ExerciseLiftEntity>> loadAllRecentExercises(boolean isRecent);

    @Query("SELECT * FROM ExerciseLiftEntity  ORDER BY name")
    LiveData<List<ExerciseLiftEntity>> loadAllExercises();

    @Query("SELECT * FROM ExerciseLiftEntity")
    List<ExerciseLiftEntity> loadExerciseList();

    @Query("select * FROM ExerciseLiftEntity WHERE id = :id")
    LiveData<ExerciseLiftEntity> loadExerciseLive(String id);

    @Query("select * FROM ExerciseLiftEntity WHERE id = :id")
    ExerciseLiftEntity loadExercise(String id);
}
