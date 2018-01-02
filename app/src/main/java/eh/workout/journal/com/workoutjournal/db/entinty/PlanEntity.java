package eh.workout.journal.com.workoutjournal.db.entinty;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "plans")
public class PlanEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String planName;

    public PlanEntity(@NonNull String id, String planName) {
        this.id = id;
        this.planName = planName;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getPlanName() {
        if (planName == null) {
            return "Workout plan";
        }
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

}
