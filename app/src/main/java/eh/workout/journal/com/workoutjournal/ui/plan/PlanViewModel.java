package eh.workout.journal.com.workoutjournal.ui.plan;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanDayEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanDaySetEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanSetEntity;

public class PlanViewModel extends AndroidViewModel {
    private JournalRepository repository;
    private LiveData<List<ExerciseLiftEntity>> exerciseLifts;
    private List<ExerciseLiftEntity> exerciseLiftEntities;
    private Long timestamp;
    boolean planAdded = false;


    public PlanViewModel(@NonNull Application application) {
        super(application);
        repository = ((JournalApplication) application).getRepository();
        exerciseLifts = repository.getAllExercisesLive();
    }


    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    LiveData<List<ExerciseLiftEntity>> getExerciseLifts() {
        return exerciseLifts;
    }

    void setSelectedList(List<ExerciseLiftEntity> exerciseLiftEntities) {
        this.exerciseLiftEntities = exerciseLiftEntities;
    }

    List<ExerciseLiftEntity> getExerciseLiftEntities() {
        return exerciseLiftEntities;
    }


    void insertPlan(String planName) {
        planAdded = true;
        String planId = UUID.randomUUID().toString();
        PlanEntity planEntity = new PlanEntity(planId, planName);
        PlanDayEntity planDayEntity = new PlanDayEntity(planName, timestamp, planId);
        List<PlanSetEntity> planSetEntityList = new ArrayList<>();
        List<PlanDaySetEntity> planSetDayEntityList = new ArrayList<>();
        for (int i = 0; i < getExerciseLiftEntities().size(); i++) {
            ExerciseLiftEntity exerciseLiftEntity = getExerciseLiftEntities().get(i);
            planSetEntityList.add(new PlanSetEntity(planId, exerciseLiftEntity));
            planSetDayEntityList.add(new PlanDaySetEntity(planDayEntity.getId(), exerciseLiftEntity));
        }
        repository.insertPlanSets(planEntity, planSetEntityList);
        repository.insertPlanDaySets(planDayEntity, planSetDayEntityList);
    }

}
