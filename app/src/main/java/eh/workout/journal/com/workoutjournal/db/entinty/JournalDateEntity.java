package eh.workout.journal.com.workoutjournal.db.entinty;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import eh.workout.journal.com.workoutjournal.model.JournalDate;

@Entity(tableName = "journal_date_entities")
public class JournalDateEntity implements JournalDate {
    @PrimaryKey
    @NonNull
    public String id;
    public long timestamp;

    public JournalDateEntity() {
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
