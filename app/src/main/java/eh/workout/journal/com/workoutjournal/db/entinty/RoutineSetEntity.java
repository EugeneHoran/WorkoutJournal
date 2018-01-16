package eh.workout.journal.com.workoutjournal.db.entinty;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

@Entity(tableName = "routine_sets",
        foreignKeys = {@ForeignKey(entity = RoutineEntity.class,
                deferred = true,
                parentColumns = "id",
                childColumns = "routineId",
                onDelete = ForeignKey.CASCADE)})
public class RoutineSetEntity {
    @PrimaryKey
    @NonNull
    public String id;
    private String name;
    private String exerciseId;
    private int exerciseEquipmentId;
    private int exerciseInputType;
    private String routineId;
    @Ignore
    private boolean setCompleted = false;

    public RoutineSetEntity(@NonNull String id, String name, String exerciseId, int exerciseEquipmentId, int exerciseInputType, String routineId) {
        this.id = id;
        this.name = name;
        this.exerciseId = exerciseId;
        this.exerciseEquipmentId = exerciseEquipmentId;
        this.exerciseInputType = exerciseInputType;
        this.routineId = routineId;
    }

    public RoutineSetEntity(ExerciseLiftEntity liftEntity, String routineId) {
        this.id = UUID.randomUUID().toString();
        this.name = liftEntity.getName();
        this.exerciseId = liftEntity.getId();
        this.exerciseEquipmentId = liftEntity.getExerciseEquipmentId();
        this.exerciseInputType = liftEntity.getExerciseInputType();
        this.routineId = routineId;
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

    public int getExerciseEquipmentId() {
        return exerciseEquipmentId;
    }

    public void setExerciseEquipmentId(int exerciseEquipmentId) {
        this.exerciseEquipmentId = exerciseEquipmentId;
    }

    public int getExerciseInputType() {
        return exerciseInputType;
    }

    public void setExerciseInputType(int exerciseInputType) {
        this.exerciseInputType = exerciseInputType;
    }

    public String getRoutineId() {
        return routineId;
    }

    public void setRoutineId(String routineId) {
        this.routineId = routineId;
    }

    @Ignore
    public boolean isSetCompleted() {
        return setCompleted;
    }

    @Ignore
    public void setSetCompleted(boolean setCompleted) {
        this.setCompleted = setCompleted;
    }

//    @Ignore
//    public String getNameWithEquipment() {
//        return name + " (" + DataHelper.EXERCISE_EQUIPMENT[exerciseEquipmentId] + ")";
//    }
}
