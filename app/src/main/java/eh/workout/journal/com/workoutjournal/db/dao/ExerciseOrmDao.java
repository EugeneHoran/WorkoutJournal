package eh.workout.journal.com.workoutjournal.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseOrmEntity;

@Dao
public interface ExerciseOrmDao {
    @Insert
    void insertOrms(ExerciseOrmEntity... exerciseOrmEntities);

    @Update
    void updateOrms(ExerciseOrmEntity... exerciseOrmEntities);

    @Delete
    void deleteOrms(ExerciseOrmEntity... exerciseOrmEntities);

    @Query("SELECT * from exercise_orm_entities WHERE exerciseId = :exerciseId")
    ExerciseOrmEntity getSet(String exerciseId);
}
