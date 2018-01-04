package eh.workout.journal.com.workoutjournal.ui.exercises;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanDayEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanDaySetEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.PlanDaySetRelation;
import eh.workout.journal.com.workoutjournal.db.relations.PlanSetRelation;
import eh.workout.journal.com.workoutjournal.db.relations.RoutineSetRelation;


public class ExerciseRoutineViewModel extends AndroidViewModel {
    private JournalRepository repository;
    private MutableLiveData<List<RoutineSetRelation>> routineSetRelationList;
    private LiveData<List<PlanSetRelation>> planSetRelationLive;
    private LiveData<List<PlanDaySetRelation>> planSetRelationDayLive;
    private Long timestamp;

    public ExerciseRoutineViewModel(@NonNull Application application, Long timestamp) {
        super(application);
        JournalApplication journalApplication = getApplication();
        this.timestamp = timestamp;
        repository = journalApplication.getRepository();
        routineSetRelationList = new MutableLiveData<>();
        planSetRelationLive = repository.getPlanSetRelationListLive();
        planSetRelationDayLive = repository.getPlanSetDayRelationListLive(timestamp);
        new TaskRoutine().execute();
    }

    LiveData<List<PlanDaySetRelation>> getPlanSetRelationDayLive() {
        return planSetRelationDayLive;
    }

    void deletePlan(PlanEntity planEntity) {
        repository.deletePlanSets(planEntity);
    }

    void resetRoutine() {
        new TaskRoutine().execute();
    }

    void insertPlan(PlanSetRelation planSetRelation) {
        PlanEntity planEntity = planSetRelation.getPlanEntity();
        PlanDayEntity planDay = new PlanDayEntity(planEntity.getPlanName(), timestamp, planEntity.getId());
        List<PlanDaySetEntity> planSetDayList = new ArrayList<>();
        for (int i = 0; i < planSetRelation.getPlanSetEntityList().size(); i++) {
            PlanSetEntity planSet = planSetRelation.getPlanSetEntityList().get(i);
            planSetDayList.add(new PlanDaySetEntity(planDay.getId(), planSet));
        }
        repository.insertPlanDaySets(planDay, planSetDayList);
    }

    LiveData<List<PlanSetRelation>> getPlanSets() {
        return planSetRelationLive;
    }

    MutableLiveData<List<RoutineSetRelation>> getRoutineSetRelationList() {
        return routineSetRelationList;
    }

    @SuppressLint("StaticFieldLeak")
    class TaskRoutine extends AsyncTask<Void, Void, List<RoutineSetRelation>> {
        @Override
        protected List<RoutineSetRelation> doInBackground(Void... voids) {
            return repository.getAllRoutinesWithSets();
        }

        @Override
        protected void onPostExecute(List<RoutineSetRelation> routineSetRelations) {
            super.onPostExecute(routineSetRelations);
            getRoutineSetRelationList().setValue(routineSetRelations);
        }
    }
}
