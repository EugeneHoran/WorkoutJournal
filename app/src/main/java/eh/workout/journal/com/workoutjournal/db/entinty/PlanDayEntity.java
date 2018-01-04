package eh.workout.journal.com.workoutjournal.db.entinty;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.UUID;

@Entity(tableName = "plan_days")
public class PlanDayEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String planName;
    private long timestamp;
    private String planEntityId;

    public PlanDayEntity() {
    }

    @Ignore
    public PlanDayEntity(String planName, long timestamp, String planEntityId) {
        this.id = UUID.randomUUID().toString();
        this.planName = planName;
        this.timestamp = timestamp;
        this.planEntityId = planEntityId;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getPlanName() {
        if (TextUtils.isEmpty(planName)) {
            return "Workout Plan";
        }
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPlanEntityId() {
        return planEntityId;
    }

    public void setPlanEntityId(String planEntityId) {
        this.planEntityId = planEntityId;
    }
}
