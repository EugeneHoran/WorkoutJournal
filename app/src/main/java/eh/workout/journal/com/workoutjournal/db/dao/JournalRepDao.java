package eh.workout.journal.com.workoutjournal.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;

@Dao
public interface JournalRepDao {
    @Insert
    void insertReps(JournalRepEntity... repEntity);

    @Delete
    void deleteReps(JournalRepEntity... repEntity);

    @Update
    void updateReps(JournalRepEntity... repEntity);

    @Update
    void updateRepList(List<JournalRepEntity> repEntityList);

    @Query("SELECT * from journal_rep_entities")
    LiveData<List<JournalRepEntity>> getAllReps();

    @Query("SELECT * from journal_rep_entities WHERE journalSetId = :setId")
    LiveData<List<JournalRepEntity>> getRepsInSet(String setId);
}
