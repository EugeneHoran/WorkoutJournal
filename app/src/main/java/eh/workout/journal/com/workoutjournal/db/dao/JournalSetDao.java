package eh.workout.journal.com.workoutjournal.db.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;

@Dao
public interface JournalSetDao {
    @Insert
    void insertSets(JournalSetEntity... setEntity);

    @Delete
    void deleteSets(JournalSetEntity... setEntity);

    @Query("DELETE FROM journal_set_entities WHERE id == :setId")
    void seleteSerById(String setId);

    @Update
    void updateSets(JournalSetEntity... setEntity);

    @Query("SELECT * from journal_set_entities WHERE exerciseId = :exerciseId AND journalDateId = :dateId")
    JournalSetEntity getSet(String exerciseId, String dateId);

    @Query("SELECT * from journal_set_entities")
    LiveData<List<JournalSetEntity>> getAllSets();

    @Query("SELECT * from journal_set_entities WHERE journalDateId = :dateId")
    LiveData<List<JournalSetEntity>> getAllSetsInDate(String dateId);
}
