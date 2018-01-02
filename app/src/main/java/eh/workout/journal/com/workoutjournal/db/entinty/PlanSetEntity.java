package eh.workout.journal.com.workoutjournal.db.entinty;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

@Entity(tableName = "plan_sets",
        foreignKeys = {@ForeignKey(entity = PlanEntity.class,
                deferred = true,
                parentColumns = "id",
                childColumns = "planTempId",
                onDelete = ForeignKey.CASCADE)})
public class PlanSetEntity {
    @PrimaryKey
    @NonNull
    public String id;
    private String name;
    private String exerciseId;
    private int exerciseInputType;
    private String planTempId;
    @Ignore
    private boolean setCompleted = false;

    public PlanSetEntity() {
    }

    public PlanSetEntity(String planTempId, ExerciseLiftEntity set) {
        this.id = UUID.randomUUID().toString();
        this.name = set.getName();
        this.exerciseId = set.getId();
        this.exerciseInputType = set.getExerciseInputType();
        this.planTempId = planTempId;
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

    public String getPlanTempId() {
        return planTempId;
    }

    public void setPlanTempId(String planTempId) {
        this.planTempId = planTempId;
    }

    @Ignore
    public boolean isSetCompleted() {
        return setCompleted;
    }

    @Ignore
    public void setSetCompleted(boolean setCompleted) {
        this.setCompleted = setCompleted;
    }
}
