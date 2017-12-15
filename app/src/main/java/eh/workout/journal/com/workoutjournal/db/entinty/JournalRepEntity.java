package eh.workout.journal.com.workoutjournal.db.entinty;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import eh.workout.journal.com.workoutjournal.util.EquationsHelper;

@Entity(tableName = "journal_rep_entities",
        foreignKeys = {@ForeignKey(entity = JournalSetEntity.class,
                parentColumns = "id",
                childColumns = "journalSetId",
                onDelete = ForeignKey.CASCADE)})
public class JournalRepEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private long timestamp;
    private int position;
    private String liftName;
    private String reps;
    private String weight;
    private double oneRepMax;
    private String journalSetId;
    private String exerciseId;
    private int exerciseInputType;

    @Ignore
    private int tempPosition;

    public JournalRepEntity() {
    }

    public JournalRepEntity(JournalRepEntity repEntity) {
        this.id = repEntity.getId();
        this.timestamp = repEntity.getTimestamp();
        this.position = repEntity.getPosition();
        this.liftName = repEntity.getLiftName();
        this.reps = repEntity.getReps();
        this.weight = repEntity.getWeight();
        this.journalSetId = repEntity.getJournalSetId();
        this.exerciseId = repEntity.getExerciseId();
        this.oneRepMax = repEntity.getOneRepMax();
        this.tempPosition = repEntity.getTempPosition();
        this.exerciseInputType = repEntity.getExerciseInputType();
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getLiftName() {
        return liftName;
    }

    public void setLiftName(String liftName) {
        this.liftName = liftName;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public double getOneRepMax() {
        return oneRepMax;
    }

    public void setOneRepMax(double oneRepMax) {
        this.oneRepMax = oneRepMax;
    }

    @Ignore
    public int getOrmInt() {
        return EquationsHelper.getOneRepMaxInt(getOneRepMax());
    }

    public String getJournalSetId() {
        return journalSetId;
    }

    public void setJournalSetId(String journalSetId) {
        this.journalSetId = journalSetId;
    }

    public int getTempPosition() {
        return tempPosition;
    }

    public void setTempPosition(int tempPosition) {
        this.tempPosition = tempPosition;
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getExerciseInputType() {
        return exerciseInputType;
    }

    public void setExerciseInputType(int exerciseInputType) {
        this.exerciseInputType = exerciseInputType;
    }
}
