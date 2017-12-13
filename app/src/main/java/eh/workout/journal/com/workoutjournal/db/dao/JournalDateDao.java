package eh.workout.journal.com.workoutjournal.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.JournalDateEntity;

@Dao
public interface JournalDateDao {
    @Insert
    void insertDates(JournalDateEntity... dateEntity);

    @Update
    void updateDates(JournalDateEntity... dateEntity);

    @Delete
    void deleteDates(JournalDateEntity... dateEntity);

    @Query("SELECT * FROM journal_date_entity WHERE timestamp BETWEEN  :start AND :end")
    JournalDateEntity getDateRun(long start, long end);

    @Query("SELECT * FROM journal_date_entity WHERE timestamp BETWEEN  :start AND :end")
    LiveData<JournalDateEntity> getDate(long start, long end);

    @Query("SELECT * FROM journal_date_entity")
    LiveData<List<JournalDateEntity>> getAllDates();
}
