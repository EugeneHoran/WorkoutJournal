package eh.workout.journal.com.workoutjournal.db.entinty;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "exercise_lift_entities")
public class ExerciseLiftEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private boolean recent;
    @Ignore
    private boolean isShownInRecent = false;

    public ExerciseLiftEntity() {
    }

    @Ignore
    public ExerciseLiftEntity(ExerciseLiftEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.recent = entity.getRecent();
        this.isShownInRecent = true;
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

    @Ignore
    public boolean getIsShownInRecent() {
        return isShownInRecent;
    }

    @Ignore
    public void setShownInRecent(boolean shownInRecent) {
        isShownInRecent = shownInRecent;
    }
}
