package eh.workout.journal.com.workoutjournal.db.entinty;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "routines")
public class RoutineEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String routineName;
    private String routineDayListString;
    private Long timestamp;

    public RoutineEntity() {
    }

    @Ignore
    public RoutineEntity(@NonNull String id, String routineName, String routineDayListString) {
        this.id = id;
        this.routineName = routineName;
        this.routineDayListString = routineDayListString;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getRoutineName() {
        if (routineName == null) {
            return "Workout routine";
        }
        return routineName;
    }

    public void setRoutineName(String routineName) {
        this.routineName = routineName;
    }

    public String getRoutineDayListString() {
        return routineDayListString;
    }

    public void setRoutineDayListString(String routineDayListString) {
        this.routineDayListString = routineDayListString;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Days
     */
    public List<Integer> getDayIntList() {
        if (TextUtils.isEmpty(getRoutineDayListString())) {
            return null;
        }
        List<Integer> daysIntList = new ArrayList<>();
        String[] dayStringArray = getRoutineDayListString().split(",");
        for (String aDayStringArray : dayStringArray) {
            daysIntList.add(Integer.valueOf(aDayStringArray));
        }
        return daysIntList;
    }
}
