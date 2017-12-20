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

public class AddPlanViewModel extends AndroidViewModel {
    private JournalRepository repository;
    private List<ExerciseLiftEntity> liftEntities = new ArrayList<>();
    private MutableLiveData<List<ExerciseLiftEntity>> listLiveData;
    private List<ExerciseLiftEntity> liftEntityList;
    private String daysString;

    public AddPlanViewModel(@NonNull JournalApplication application) {
        super(application);
        repository = application.getRepository();
    }

    void initLifts() {
        new TaskLifts().execute();
    }

    LiveData<List<ExerciseLiftEntity>> getLiftsLiveData() {
        if (listLiveData == null) {
            listLiveData = new MutableLiveData<>();
        }
        return listLiveData;
    }

    private void dataLoaded() {
        listLiveData.setValue(liftEntities);
    }

    void setLifts(List<ExerciseLiftEntity> exerciseLiftEntityList) {
        this.liftEntityList = exerciseLiftEntityList;
    }

    void setDaysString(String daysString) {
        this.daysString = daysString;
    }

    void insertNewPlan() {
        String planId = UUID.randomUUID().toString();
        PlanEntity planEntity = new PlanEntity(planId, daysString);
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
            dataLoaded();
        }
    }
}
