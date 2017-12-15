package eh.workout.journal.com.workoutjournal.db.entinty;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "exercise_group_entities")
public class ExerciseGroupEntity {
    @PrimaryKey
    @NonNull
    private int id;
    private String name;

    public ExerciseGroupEntity(@NonNull int id, String name) {
        this.id = id;
        this.name = name;
    }

    @NonNull
    public int getId() {
        return id;
    }

    @NonNull
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
