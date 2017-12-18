package eh.workout.journal.com.workoutjournal.db.entinty;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "exercise_lift_entities",
        foreignKeys = {@ForeignKey(entity = ExerciseGroupEntity.class,
                parentColumns = "id",
                childColumns = "exerciseGroupId",
                deferred = true)})
public class ExerciseLiftEntity implements Comparable<ExerciseLiftEntity> {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private boolean recent;
    private Long timestampRecent;
    private int exerciseGroupId;
    private int exerciseInputType;
    @Ignore
    private boolean isShownInRecent = false;
    @Ignore
    private boolean selected = false;

    public ExerciseLiftEntity() {
    }

    @Ignore
    public ExerciseLiftEntity(ExerciseLiftEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.recent = entity.getRecent();
        this.timestampRecent = entity.getTimestampRecent();
        this.isShownInRecent = true;
        this.exerciseGroupId = entity.getExerciseGroupId();
        this.exerciseInputType = entity.getExerciseInputType();
    }

    @Ignore
    public ExerciseLiftEntity(@NonNull String id, String name, boolean recent) {
        this.id = id;
        this.name = name;
        this.recent = recent;
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

    public boolean getRecent() {
        return recent;
    }

    public void setRecent(boolean recent) {
        this.recent = recent;
    }

    public Long getTimestampRecent() {
        return timestampRecent;
    }

    public void setTimestampRecent(Long timestampRecent) {
        this.timestampRecent = timestampRecent;
    }

    public int getExerciseGroupId() {
        return exerciseGroupId;
    }

    public void setExerciseGroupId(int exerciseGroupId) {
        this.exerciseGroupId = exerciseGroupId;
    }

    public int getExerciseInputType() {
        return exerciseInputType;
    }

    public void setExerciseInputType(int exerciseInputType) {
        this.exerciseInputType = exerciseInputType;
    }

    @Ignore
    public boolean getIsShownInRecent() {
        return isShownInRecent;
    }

    @Ignore
    public void setShownInRecent(boolean shownInRecent) {
        isShownInRecent = shownInRecent;
    }

    @Ignore
    public boolean isSelected() {
        return selected;
    }

    @Ignore
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Comparator
     */
    @Override
    public int compareTo(@NonNull ExerciseLiftEntity exerciseLiftEntity) {
        return (this.timestampRecent).compareTo(exerciseLiftEntity.timestampRecent);
    }

}
