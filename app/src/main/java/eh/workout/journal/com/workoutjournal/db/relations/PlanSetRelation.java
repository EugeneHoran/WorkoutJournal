package eh.workout.journal.com.workoutjournal.db.relations;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.PlanEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanSetEntity;

public class PlanSetRelation {
    @Embedded
    public PlanEntity planEntity;

    @Relation(parentColumn = "id", entityColumn = "planTempId", entity = PlanSetEntity.class)
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
}
