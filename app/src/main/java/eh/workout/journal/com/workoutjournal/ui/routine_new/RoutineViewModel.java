package eh.workout.journal.com.workoutjournal.ui.routine_new;

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
import eh.workout.journal.com.workoutjournal.db.entinty.PlanEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanSetEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.RoutineEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.RoutineSetEntity;
import eh.workout.journal.com.workoutjournal.model.DaySelector;

public class RoutineViewModel extends AndroidViewModel {
    private JournalRepository repository;
    private LiveData<List<ExerciseLiftEntity>> liftList;
    private List<ExerciseLiftEntity> exerciseLiftEntities;
    public boolean routineAdded = false;

    private List<ExerciseLiftEntity> liftListSelected = new ArrayList<>();
    private List<DaySelector> daySelectorList;
    private String daysString;

    public RoutineViewModel(@NonNull Application application) {
        super(application);
        repository = ((JournalApplication) application).getRepository();
        liftList = repository.getAllExercisesLive();
    }

    public void setSelectedList(List<ExerciseLiftEntity> exerciseLiftEntities) {
        this.exerciseLiftEntities = exerciseLiftEntities;
    }

    public List<ExerciseLiftEntity> getExerciseLiftEntities() {
        return exerciseLiftEntities;
    }

    /**
     * Insert Routine
     */
    public void insertNewPlan(String planName) {
        routineAdded = true;
        String planId = UUID.randomUUID().toString();
        RoutineEntity routineEntity = new RoutineEntity(planId, planName, daysString);
        List<RoutineSetEntity> planSetEntityList = new ArrayList<>();
        for (int i = 0; i < liftListSelected.size(); i++) {
            ExerciseLiftEntity exerciseLiftEntity = liftListSelected.get(i);
            RoutineSetEntity routineSetEntity = new RoutineSetEntity(
                    UUID.randomUUID().toString(),
                    exerciseLiftEntity.getName(),
                    exerciseLiftEntity.getId(),
                    exerciseLiftEntity.getExerciseInputType(),
                    planId);
            planSetEntityList.add(routineSetEntity);
        }
        repository.insertRoutine(routineEntity, planSetEntityList);
    }


    /**
     * Insert Plan
     */
    public void insertPlan(String planName) {
        String planId = UUID.randomUUID().toString();
        PlanEntity planEntity = new PlanEntity(planId, planName);
        List<PlanSetEntity> planSetEntityList = new ArrayList<>();
        for (int i = 0; i < liftListSelected.size(); i++) {
            ExerciseLiftEntity exerciseLiftEntity = liftListSelected.get(i);
            planSetEntityList.add(new PlanSetEntity(planId, exerciseLiftEntity));
        }
        repository.insertPlanSets(planEntity, planSetEntityList);
    }

    /**
     * Days
     */

    public void setDaysString(String daysString) {
        this.daysString = daysString;
    }

    public void setDaySelectorList(List<DaySelector> daySelectorList) {
        this.daySelectorList = daySelectorList;
    }

    public List<DaySelector> getDaySelectorList() {
        return daySelectorList;
    }

    /**
     * Lifts
     */
    public LiveData<List<ExerciseLiftEntity>> getLiftList() {
        return liftList;
    }

    public void setLiftListSelected(List<ExerciseLiftEntity> exerciseLiftEntities) {
        liftListSelected.clear();
        liftListSelected.addAll(exerciseLiftEntities);
    }

    public List<ExerciseLiftEntity> getLiftListSelected() {
        return liftListSelected;
    }
}
