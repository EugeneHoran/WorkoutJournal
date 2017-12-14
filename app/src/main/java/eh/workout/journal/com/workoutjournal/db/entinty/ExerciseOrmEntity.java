package eh.workout.journal.com.workoutjournal.db.entinty;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "exercise_orm_entities")
public class ExerciseOrmEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private double oneRepMax;
    private String weight;
    private String reps;
    private long timestamp;
    private String exerciseId;
    private String repId;

    public ExerciseOrmEntity() {
    }

    public ExerciseOrmEntity(@NonNull String id, String name, double oneRepMax, String weight, String reps, long timestamp, String exerciseId, String repId) {
        this.id = id;
        this.name = name;
        this.oneRepMax = oneRepMax;
        this.weight = weight;
        this.reps = reps;
        this.timestamp = timestamp;
        this.exerciseId = exerciseId;
        this.repId = repId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getOneRepMax() {
        return oneRepMax;
    }

    public void setOneRepMax(double oneRepMax) {
        this.oneRepMax = oneRepMax;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
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

    public String getRepId() {
        return repId;
    }

    public void setRepId(String repId) {
        this.repId = repId;
    }
}
