package eh.workout.journal.com.workoutjournal.db.entinty;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import eh.workout.journal.com.workoutjournal.model.JournalRep;
import eh.workout.journal.com.workoutjournal.util.EquationsHelper;

@Entity(tableName = "journal_rep_entity",
        foreignKeys = {@ForeignKey(entity = JournalSetEntity.class,
                parentColumns = "id",
                childColumns = "journalSetId",
                onDelete = ForeignKey.CASCADE)})
public class JournalRepEntity implements JournalRep {
    @PrimaryKey
    @NonNull
    private String id;
    private long timestamp;
    private int position;
    private String liftName;
    private String reps;
    private String weight;
    private String journalSetId;
    private double oneRepMax;


    public JournalRepEntity() {
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

    @Override
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String getLiftName() {
        return liftName;
    }

    public void setLiftName(String liftName) {
        this.liftName = liftName;
    }

    @Override
    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    @Override
    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public String getJournalSetId() {
        return journalSetId;
    }

    public void setJournalSetId(String journalSetId) {
        this.journalSetId = journalSetId;
    }

    @Override
    public double getOneRepMax() {
        return oneRepMax;
    }

    @Ignore
    public int getOrmInt() {
        return EquationsHelper.getOneRepMaxInt(getOneRepMax());
    }

    public void setOneRepMax(double oneRepMax) {
        this.oneRepMax = oneRepMax;
    }

}
