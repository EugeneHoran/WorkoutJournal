package eh.workout.journal.com.workoutjournal.db.entinty;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "plans")
public class PlanEntity {

    @PrimaryKey
    @NonNull
    private String id;
    private String planDayString;
//    private Integer[] planDayList;

    public PlanEntity(@NonNull String id, String planDayString) {
        this.id = id;
        this.planDayString = planDayString;
//        this.planDayList = CustomTypeConverters.fromStringToDayList(planDayString);
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getPlanDayString() {
        return planDayString;
    }

    public void setPlanDayString(String planDayString) {
        this.planDayString = planDayString;
    }

//    public Integer[] getPlanDayList() {
//        return planDayList;
//    }
//
//    public void setPlanDayList(Integer[] planDayList) {
//        this.planDayList = planDayList;
//    }
}
