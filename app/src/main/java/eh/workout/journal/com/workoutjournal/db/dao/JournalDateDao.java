package eh.workout.journal.com.workoutjournal.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.JournalDateEntity;

@Dao
public interface JournalDateDao {
    @Insert
    void insertDates(JournalDateEntity... dateEntity);

    @Query("DELETE FROM journal_date_entities WHERE id == :dateId")
    void deleteDateAndRelations(String dateId);

    @Query("SELECT * FROM journal_date_entities WHERE timestamp BETWEEN  :start AND :end")
    JournalDateEntity getDateRun(long start, long end);

    @Query("SELECT * FROM journal_date_entities")
    LiveData<List<JournalDateEntity>> getAllDates();
}
