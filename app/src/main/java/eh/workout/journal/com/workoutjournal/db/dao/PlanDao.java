package eh.workout.journal.com.workoutjournal.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.PlanDayEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanDaySetEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.PlanDaySetRelation;
import eh.workout.journal.com.workoutjournal.db.relations.PlanSetRelation;

@Dao
public abstract class PlanDao {
    @Transaction
    @Query("SELECT * FROM plans")
    public abstract LiveData<List<PlanSetRelation>> getPlanSetRelationListLive();

    @Transaction
    @Query("SELECT * FROM plan_days WHERE timestamp == :timestamp")
    public abstract LiveData<List<PlanDaySetRelation>> getPlanSetDayRelationListLive(Long timestamp);

    @Transaction
    @Query("SELECT * FROM plan_days WHERE timestamp == :timestamp")
    public abstract List<PlanDaySetRelation> getPlanSetDayRelationList(Long timestamp);

    @Transaction
    @Query("SELECT * FROM plan_days WHERE id == :planId")
    public abstract PlanDaySetRelation getPlanDaySetRelation(String planId);

    @Insert
    public abstract void insertPlan(PlanEntity planEntity);

    @Insert
    public abstract void insertPlanDay(PlanDayEntity planDayEntity);

    @Insert
    public abstract void insertPlanSetEntity(List<PlanSetEntity> planSetEntities);

    @Insert
    public abstract void insertPlanDaySetEntity(List<PlanDaySetEntity> planDaySetEntities);

    @Delete
    public abstract void deletePlanEntity(PlanEntity planEntity);

    @Delete
    public abstract void deletePlanDaySetEntities(List<PlanDaySetEntity> planDaySetEntity);

    @Update
    public abstract void updatePlanDay(PlanDayEntity planDayEntity);

    @Delete
    public abstract void deletePlanDayEntity(PlanDayEntity planDayEntity);

    @Transaction
    public void insertPlanSets(PlanEntity planEntity, List<PlanSetEntity> planSetEntities) {
        insertPlan(planEntity);
        insertPlanSetEntity(planSetEntities);
    }

    @Transaction
    public void updatePlanDaySets(PlanDayEntity planDayEntity, List<PlanDaySetEntity> planDaySetEntityList) {
        updatePlanDay(planDayEntity);
        insertPlanDaySetEntity(planDaySetEntityList);
    }

    @Transaction
    public void insertPlanDaySets(PlanDayEntity planDayEntity, List<PlanDaySetEntity> planDaySetEntityList) {
        insertPlanDay(planDayEntity);
        insertPlanDaySetEntity(planDaySetEntityList);
    }
}
