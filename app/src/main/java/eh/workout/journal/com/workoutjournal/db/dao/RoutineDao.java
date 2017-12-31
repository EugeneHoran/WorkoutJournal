package eh.workout.journal.com.workoutjournal.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.RoutineEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.RoutineSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.RoutineSetRelation;


@Dao
public abstract class RoutineDao {
    @Transaction
    @Query("SELECT * FROM routines WHERE routineDayListString LIKE :day")
    public abstract LiveData<List<RoutineSetRelation>> getRoutineSetRelationListLive(String day);

    @Transaction
    @Query("SELECT * FROM routines WHERE routineDayListString LIKE :day")
    public abstract List<RoutineSetRelation> getRoutineSetRelationList(String day);

    @Transaction
    @Query("SELECT * FROM routines")
    public abstract List<RoutineSetRelation> getAllRoutinesWithSets();

    @Transaction
    @Query("SELECT * FROM routines WHERE id = :routineId")
    public abstract RoutineSetRelation getRoutineSetRelation(String routineId);

    @Insert
    public abstract void insertRoutine(RoutineEntity routineEntity);

    @Insert
    public abstract void insertSetEntity(List<RoutineSetEntity> planSetEntities);

    @Delete
    public abstract void deleteRoutineEntity(RoutineEntity routineEntity);

    @Transaction
    public void insertNewRoutine(RoutineEntity routineEntity, List<RoutineSetEntity> planSetEntityList) {
        insertRoutine(routineEntity);
        insertSetEntity(planSetEntityList);
    }
}
