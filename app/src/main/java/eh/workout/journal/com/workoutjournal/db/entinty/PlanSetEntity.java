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
                childColumns = "planId",
                onDelete = ForeignKey.CASCADE)})
public class PlanSetEntity {
    @PrimaryKey
    @NonNull
    public String id;
    private String name;
    private String exerciseId;
    private int exerciseInputType;
    private String planId;
    @Ignore
    private boolean setCompleted = false;

    public PlanSetEntity(@NonNull String id, String name, String exerciseId, int exerciseInputType, String planId) {
        this.id = id;
        this.name = name;
        this.exerciseId = exerciseId;
        this.exerciseInputType = exerciseInputType;
        this.planId = planId;
    }


    public PlanSetEntity(ExerciseLiftEntity liftEntity, String planId) {
        this.id = UUID.randomUUID().toString();
        this.name = liftEntity.getName();
        this.exerciseId = liftEntity.getId();
        this.exerciseInputType = liftEntity.getExerciseInputType();
        this.planId = planId;
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

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
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
