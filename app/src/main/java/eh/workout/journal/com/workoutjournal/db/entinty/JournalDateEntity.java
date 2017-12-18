package eh.workout.journal.com.workoutjournal.db.entinty;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "journal_date_entities")
public class JournalDateEntity {
    @PrimaryKey
    @NonNull
    private Long id;
    public long timestamp;

    public JournalDateEntity() {
    }

    public JournalDateEntity(@NonNull Long id, long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
