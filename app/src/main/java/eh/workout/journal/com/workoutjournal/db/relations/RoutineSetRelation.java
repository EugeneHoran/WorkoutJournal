package eh.workout.journal.com.workoutjournal.db.relations;


import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.RoutineEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.RoutineSetEntity;

public class RoutineSetRelation {
    @Embedded
    public RoutineEntity routineEntity;

    @Relation(parentColumn = "id", entityColumn = "routineId", entity = RoutineSetEntity.class)
    private List<RoutineSetEntity> planSetEntityList;

    public RoutineEntity getRoutineEntity() {
        return routineEntity;
    }

    public void setRoutineEntity(RoutineEntity routineEntity) {
        this.routineEntity = routineEntity;
    }

    public List<RoutineSetEntity> getPlanSetEntityList() {
        return planSetEntityList;
    }

    public void setPlanSetEntityList(List<RoutineSetEntity> planSetEntityList) {
        this.planSetEntityList = planSetEntityList;
    }

    public List<Integer> getDaysList() {
        List<Integer> daysIntList = new ArrayList<>();
        String[] dayStringArray = routineEntity.getRoutineDayListString().split(",");
        for (String aDayStringArray : dayStringArray) {
            daysIntList.add(Integer.valueOf(aDayStringArray));
        }
        return daysIntList;
    }
}
