package eh.workout.journal.com.workoutjournal.db.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseOrmEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalDateEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;

@Dao
public abstract class JournalDao {
    @Query("SELECT *, MAX(oneRepMax) FROM journal_rep_entities WHERE exerciseId == :exerciseId AND timestamp == :timestamp LIMIT 1")
    public abstract JournalRepEntity getSetRepsByMaxDate(String exerciseId, String timestamp);

    @Query("SELECT *, MAX(oneRepMax) FROM journal_rep_entities WHERE exerciseId == :exerciseId LIMIT 1")
    public abstract JournalRepEntity getSetRepsByMax(String exerciseId);

    @Query("SELECT *, MIN(oneRepMax) FROM journal_rep_entities WHERE exerciseId == :exerciseId LIMIT 1")
    public abstract JournalRepEntity getSetRepsByMin(String exerciseId);

    @Query("SELECT *, MAX(oneRepMax) FROM journal_rep_entities WHERE exerciseId == :exerciseId GROUP BY timestamp ORDER BY timestamp")
    public abstract List<JournalRepEntity> getSetRepsByMaxList(String exerciseId);

    //
    @Query("SELECT * FROM journal_set_entities GROUP BY timestamp ORDER BY timestamp")
    public abstract List<JournalSetEntity> getJournalSetEntityDatesList();

    @Query("SELECT * FROM journal_set_entities GROUP BY timestamp ORDER BY timestamp")
    public abstract LiveData<List<JournalSetEntity>> getJournalSetEntityDates();

    @Transaction
    @Query("SELECT * FROM journal_set_entities WHERE timestamp BETWEEN :start AND :end")
    public abstract LiveData<List<JournalSetEntity>> getSetListByIdLive(long start, long end);

    @Query("SELECT *, MAX(oneRepMax) FROM journal_rep_entities GROUP BY timestamp ORDER BY timestamp")
    public abstract LiveData<List<JournalRepEntity>> getSetRepsByMax();

    @Transaction
    @Query("SELECT * FROM journal_set_entities WHERE timestamp BETWEEN :start AND :end")
    public abstract LiveData<List<ExerciseSetRepRelation>> getExerciseSetRepRelationLive(long start, long end);


    // Set by Exercise and Date
    // Reps by Set
    // One Rep Max by Exercise
    @Transaction
    @Query("SELECT * FROM journal_set_entities WHERE exerciseId == :exerciseId AND timestamp BETWEEN :start AND :end")
    public abstract LiveData<ExerciseSetRepRelation> getEntrySetRepsAndOrmLive(String exerciseId, long start, long end);

    // Exercise
    @Query("select * FROM exercise_lift_entities WHERE id = :id")
    public abstract LiveData<ExerciseLiftEntity> getExerciseByIdLive(String id);

    // Exercise
    @Query("select * FROM exercise_lift_entities WHERE id = :id")
    public abstract ExerciseLiftEntity getExerciseById(String id);

    // Date
    @Query("SELECT * FROM journal_date_entities WHERE timestamp BETWEEN  :start AND :end")
    public abstract LiveData<JournalDateEntity> getDateByTimestampLive(long start, long end);

    // Date All
    @Query("SELECT * FROM journal_date_entities")
    public abstract List<JournalDateEntity> getDateList();

    // Date All
    @Query("SELECT * FROM journal_date_entities ORDER BY id DESC LIMIT :limit")
    public abstract LiveData<List<JournalDateEntity>> getDateListLimitLive(int limit);

    // Set
    @Query("SELECT * from journal_set_entities WHERE exerciseId = :exerciseId AND dateId = :dateId")
    public abstract LiveData<JournalSetEntity> getSetByExerciseIdAndDateIdLive(String exerciseId, Long dateId);

    // One Rep Max Live
    @Query("SELECT * from exercise_orm_entities WHERE exerciseId = :exerciseId")
    public abstract ExerciseOrmEntity getOneRepMaxByExerciseId(String exerciseId);

    // One Rep Max Live
    @Query("SELECT * from exercise_orm_entities WHERE exerciseId = :exerciseId")
    public abstract LiveData<ExerciseOrmEntity> getOneRepMaxByExerciseIdLive(String exerciseId);

    // One Rep Max Live
    @Query("SELECT * from journal_date_entities WHERE id = :dateId")
    public abstract JournalDateEntity getDateById(Long dateId);

    // Largest One Rep Max by Exercise
    @Query("SELECT * from journal_rep_entities WHERE exerciseId = :exerciseId  ORDER BY oneRepMax DESC LIMIT 1")
    public abstract JournalRepEntity getRepWithLargestOrm(String exerciseId);

    @Transaction
    @Query("SELECT * FROM journal_set_entities WHERE exerciseId == :exerciseId AND timestamp < :start")
    public abstract LiveData<List<ExerciseSetRepRelation>> getExerciseSetRepRelationHistoryLive(String exerciseId, long start);

    @Transaction
    @Query("SELECT * FROM journal_set_entities WHERE exerciseId == :exerciseId AND timestamp < :start  ORDER BY timestamp DESC")
    public abstract List<ExerciseSetRepRelation> getExerciseSetRepRelationHistory(String exerciseId, long start);

    @Transaction
    public void insertSetAndOrmTransaction(JournalRepEntity repEntity, ExerciseOrmEntity ormEntity) {
        insertReps(repEntity);
        insertOrms(ormEntity);
    }

    @Transaction
    public void insertSetAndUpdateOrmTransaction(JournalRepEntity repEntity, ExerciseOrmEntity ormEntity) {
        insertReps(repEntity);
        updateOrms(ormEntity);
    }

    @Transaction
    public void deleteRepAndUpdateListPositions(JournalRepEntity repEntity, List<JournalRepEntity> repEntityList) {
        deleteReps(repEntity);
        updateRepList(repEntityList);
    }

    @Transaction
    public void deleteRepAndUpdateListPositionsNew(JournalRepEntity repEntity, List<JournalRepEntity> repEntityList) {
        deleteReps(repEntity);
        updateRepList(repEntityList);
    }

    // Date
    @Insert
    public abstract void insertDates(JournalDateEntity... dateEntity);

    @Delete
    public abstract void deleteDates(JournalDateEntity... dateEntity);

    // Set
    @Insert
    public abstract void insertSets(JournalSetEntity... setEntity);

    @Delete
    public abstract void deleteSets(JournalSetEntity... setEntity);

    @Transaction
    public void deleteSetsWithUpdate(JournalSetEntity setEntity, boolean update, ExerciseOrmEntity ormEntity) {
        deleteSets(setEntity);
        if (update) {
            updateOrms(ormEntity);
        } else {
            if (ormEntity != null) {
                deleteOrms(ormEntity);
            }
        }
    }

    @Transaction
    public void deleteSet(JournalSetEntity setEntity) {
        deleteSets(setEntity);
    }

    @Transaction
    public void updateDeleteOrm(boolean update, ExerciseOrmEntity ormEntity) {
        if (update) {
            updateOrms(ormEntity);
        } else {
            if (ormEntity != null) {
                deleteOrms(ormEntity);
            }
        }
    }

    // Rep
    @Insert
    public abstract void insertReps(JournalRepEntity... repEntity);

    @Update
    public abstract void updateReps(JournalRepEntity... repEntity);

    @Update
    public abstract void updateRepList(List<JournalRepEntity> repEntityList);

    @Delete
    public abstract void deleteReps(JournalRepEntity... repEntity);

    // Orm
    @Insert
    public abstract void insertOrms(ExerciseOrmEntity... exerciseOrmEntities);

    @Update
    public abstract void updateOrms(ExerciseOrmEntity... exerciseOrmEntities);

    @Delete
    public abstract void deleteOrms(ExerciseOrmEntity... exerciseOrmEntities);
}
