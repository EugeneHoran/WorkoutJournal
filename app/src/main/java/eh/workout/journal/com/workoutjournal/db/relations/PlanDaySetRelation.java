package eh.workout.journal.com.workoutjournal.db.relations;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.PlanDayEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanDaySetEntity;


public class PlanDaySetRelation {

    @Embedded
    public PlanDayEntity planDayEntity;

    @Relation(parentColumn = "id", entityColumn = "planTempId", entity = PlanDaySetEntity.class)
    private List<PlanDaySetEntity> planDaySetEntityList;

    public PlanDayEntity getPlanDayEntity() {
        return planDayEntity;
    }


    @Ignore
    public boolean areAllSetsCompleted() {
        for (int i = 0; i < getPlanDaySetEntityList().size(); i++) {
            if (!getPlanDaySetEntityList().get(i).isSetCompleted()) {
                return false;
            }
        }
        return true;
    }

    public void setPlanDayEntity(PlanDayEntity planDayEntity) {
        this.planDayEntity = planDayEntity;
    }

    public List<PlanDaySetEntity> getPlanDaySetEntityList() {
        return planDaySetEntityList;
    }

    public void setPlanDaySetEntityList(List<PlanDaySetEntity> planDaySetEntityList) {
        this.planDaySetEntityList = planDaySetEntityList;
    }
}
