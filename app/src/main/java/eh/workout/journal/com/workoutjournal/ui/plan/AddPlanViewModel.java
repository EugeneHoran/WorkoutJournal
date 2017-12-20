package eh.workout.journal.com.workoutjournal.ui.plan;

import android.annotation.SuppressLint;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanSetEntity;
import eh.workout.journal.com.workoutjournal.model.DaySelector;

public class AddPlanViewModel extends AndroidViewModel {
    private JournalRepository repository;
    private List<ExerciseLiftEntity> liftEntities = new ArrayList<>();
    private MutableLiveData<List<ExerciseLiftEntity>> listLiveData;
    private List<ExerciseLiftEntity> liftEntityList;
    private List<ExerciseLiftEntity> retainedLiftList;
    private List<DaySelector> daySelectorList;
    private String daysString;
    private String planName;

    public AddPlanViewModel(@NonNull JournalApplication application) {
        super(application);
        repository = application.getRepository();
    }

    LiveData<List<ExerciseLiftEntity>> getLiftsLiveData() {
        if (listLiveData == null) {
            listLiveData = new MutableLiveData<>();
        }
        return listLiveData;
    }

    void initLifts() {
        if (retainedLiftList == null) {
            new TaskLifts().execute();
        } else {
            listLiveData.setValue(retainedLiftList);
        }
    }

    void setRetainedLiftList(List<ExerciseLiftEntity> retainedLiftList) {
        this.retainedLiftList = retainedLiftList;
    }

    void setLifts(List<ExerciseLiftEntity> liftEntityList) {
        this.liftEntityList = liftEntityList;
    }

    List<ExerciseLiftEntity> getLifts() {
        return liftEntityList;
    }

    void setDaysString(String daysString) {
        this.daysString = daysString;
    }

    void setDaySelectorList(List<DaySelector> daySelectorList) {
        this.daySelectorList = daySelectorList;
    }

    List<DaySelector> getDaySelectorList() {
        return daySelectorList;
    }

    void setPlanName(String planName) {
        this.planName = planName;
    }

    void insertNewPlan() {
        String planId = UUID.randomUUID().toString();
        PlanEntity planEntity = new PlanEntity(planId, planName, daysString);
        List<PlanSetEntity> planSetEntityList = new ArrayList<>();
        for (int i = 0; i < liftEntityList.size(); i++) {
            ExerciseLiftEntity exerciseLiftEntity = liftEntityList.get(i);
            PlanSetEntity planSetEntity = new PlanSetEntity(
                    UUID.randomUUID().toString(),
                    exerciseLiftEntity.getName(),
                    exerciseLiftEntity.getId(),
                    exerciseLiftEntity.getExerciseInputType(),
                    planId);
            planSetEntityList.add(planSetEntity);
        }
        repository.insertPlan(planEntity, planSetEntityList);
    }

    @SuppressLint("StaticFieldLeak")
    class TaskLifts extends AsyncTask<Void, Void, List<ExerciseLiftEntity>> {
        @Override
        protected List<ExerciseLiftEntity> doInBackground(Void... voids) {
            return repository.getAllExercisesList();
        }

        @Override
        protected void onPostExecute(List<ExerciseLiftEntity> exerciseLiftEntities) {
            super.onPostExecute(exerciseLiftEntities);
            liftEntities.clear();
            liftEntities.addAll(exerciseLiftEntities);
            listLiveData.setValue(liftEntities);
        }
    }
}
