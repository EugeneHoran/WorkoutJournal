package eh.workout.journal.com.workoutjournal.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.relations.DateSetRepRelation;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;


@Dao
public interface RelationDao {
    @Query("SELECT * FROM journal_set_entities WHERE timestamp BETWEEN :start AND :end")
    LiveData<List<DateSetRepRelation>> getDateSetRepList(long start, long end);

    @Query("SELECT * FROM journal_set_entities WHERE exerciseId == :exerciseId AND timestamp BETWEEN :start AND :end")
    LiveData<ExerciseSetRepRelation> getExerciseSetRep(String exerciseId, long start, long end);

    @Query("SELECT * FROM journal_set_entities WHERE exerciseId == :exerciseId AND timestamp < :start")
    LiveData<List<ExerciseSetRepRelation>> getExerciseSetRepHistory(String exerciseId, long start);
}
