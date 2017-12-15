package eh.workout.journal.com.workoutjournal.db.entinty;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "journal_set_entities")
public class JournalSetEntity {
    @PrimaryKey
    @NonNull
    public String id;
    private String name;
    private int exerciseType;
    private long timestamp;
    private String exerciseId;
    private Long dateId;
    private int exerciseInputType;

    public JournalSetEntity() {
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(int exerciseType) {
        this.exerciseType = exerciseType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }

    public Long getDateId() {
        return dateId;
    }

    public void setDateId(Long dateId) {
        this.dateId = dateId;
    }

    public int getExerciseInputType() {
        return exerciseInputType;
    }

    public void setExerciseInputType(int exerciseInputType) {
        this.exerciseInputType = exerciseInputType;
    }
}
