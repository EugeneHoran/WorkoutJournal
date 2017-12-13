package eh.workout.journal.com.workoutjournal.db.entinty;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import eh.workout.journal.com.workoutjournal.model.JournalSet;

@Entity(tableName = "journal_set_entity",
        foreignKeys = {
                @ForeignKey(entity = JournalDateEntity.class,
                        parentColumns = "id",
                        childColumns = "journalDateId",
                        onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = "journalDateId")
        })
public class JournalSetEntity implements JournalSet {
    @PrimaryKey
    @NonNull
    public String id;
    private String name;
    private long timestamp;
    private String exerciseId;
    private String journalDateId;

    public JournalSetEntity() {
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }

    @Override
    public String getJournalDateId() {
        return journalDateId;
    }

    public void setJournalDateId(String journalDateId) {
        this.journalDateId = journalDateId;
    }
}
