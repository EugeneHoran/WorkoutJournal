package eh.workout.journal.com.workoutjournal.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.PlanEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.PlanSetRelation;


@Dao
public abstract class PlanDao {
    @Transaction
    @Query("SELECT * FROM plans WHERE planDayString LIKE :day")
    public abstract LiveData<List<PlanSetRelation>> getPlanSetRelationListLive(String day);

    @Transaction
    @Query("SELECT * FROM plans WHERE planDayString LIKE :day")
    public abstract List<PlanSetRelation> getPlanSetRelationList(String day);

    @Insert
    public abstract void insertPlan(PlanEntity planEntity);

    @Insert
    public abstract void insertSetEntity(List<PlanSetEntity> planSetEntities);

    @Transaction
    public void insertNewPlan(PlanEntity planEntity, List<PlanSetEntity> planSetEntityList) {
        insertPlan(planEntity);
        insertSetEntity(planSetEntityList);
    }
}
