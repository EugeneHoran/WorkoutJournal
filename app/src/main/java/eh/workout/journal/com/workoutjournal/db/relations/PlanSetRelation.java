package eh.workout.journal.com.workoutjournal.db.relations;


import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.PlanEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanSetEntity;

public class PlanSetRelation {
    @Embedded
    public PlanEntity planEntity;

    @Relation(parentColumn = "id", entityColumn = "planId", entity = PlanSetEntity.class)
    private List<PlanSetEntity> planSetEntityList;

    public PlanEntity getPlanEntity() {
        return planEntity;
    }

    public void setPlanEntity(PlanEntity planEntity) {
        this.planEntity = planEntity;
    }

    public List<PlanSetEntity> getPlanSetEntityList() {
        return planSetEntityList;
    }

    public void setPlanSetEntityList(List<PlanSetEntity> planSetEntityList) {
        this.planSetEntityList = planSetEntityList;
    }

    public List<Integer> getDaysList() {
        List<Integer> daysIntList = new ArrayList<>();
        String[] dayStringArray = planEntity.getPlanDayString().split(",");
        for (String aDayStringArray : dayStringArray) {
            daysIntList.add(Integer.valueOf(aDayStringArray));
        }
        return daysIntList;
    }
}
