package eh.workout.journal.com.workoutjournal.ui.plan.edit;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
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
import eh.workout.journal.com.workoutjournal.db.relations.PlanDaySetRelation;


public class PlanDayEditViewModel extends AndroidViewModel {

    private MutableLiveData<String> planName;
    private MutableLiveData<List<ExerciseLiftEntity>> exercises;
    private MutableLiveData<List<ExerciseLiftEntity>> exercisesSelected;
    private JournalRepository repository;
    private PlanDaySetRelation planDaySetRelation;

    public PlanDayEditViewModel(@NonNull Application application, String planId) {
        super(application);
        repository = ((JournalApplication) application).getRepository();
        exercisesSelected = new MutableLiveData<>();
        new TaskPlan().execute(planId);
    }

    void updatePlan() {
        PlanDayEntity planDayEntity = planDaySetRelation.getPlanDayEntity();
        planDayEntity.setPlanName(getPlanName().getValue());
        List<PlanDaySetEntity> planSetDayEntityList = new ArrayList<>();
        for (int i = 0; i < getExercisesSelected().getValue().size(); i++) {
            ExerciseLiftEntity exerciseLiftEntity = getExercisesSelected().getValue().get(i);
            planSetDayEntityList.add(new PlanDaySetEntity(planDayEntity.getId(), exerciseLiftEntity));
        }
        repository.updatePlanDaySetRelation(planDayEntity, planDaySetRelation.getPlanDaySetEntityList(), planSetDayEntityList);
    }

    MutableLiveData<String> getPlanName() {
        if (planName == null) {
            planName = new MutableLiveData<>();
        }
        return planName;
    }

    MutableLiveData<List<ExerciseLiftEntity>> getExercises() {
        if (exercises == null) {
            exercises = new MutableLiveData<>();
        }
        return exercises;
    }

    MutableLiveData<List<ExerciseLiftEntity>> getExercisesSelected() {
        if (exercisesSelected == null) {
            exercisesSelected = new MutableLiveData<>();
        }
        return exercisesSelected;
    }

    @SuppressLint("StaticFieldLeak")
    class TaskPlan extends AsyncTask<String, Void, PlanDaySetRelation> {
        @Override
        protected PlanDaySetRelation doInBackground(String... strings) {
            return repository.getPlanDaySetRelation(strings[0]);
        }

        @Override
        protected void onPostExecute(PlanDaySetRelation planSetResponse) {
            planDaySetRelation = planSetResponse;
            planName.setValue(planDaySetRelation.getPlanDayEntity().getPlanName());
            new TaskLifts().execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class TaskLifts extends AsyncTask<Void, Void, List<ExerciseLiftEntity>> {
        @Override
        protected List<ExerciseLiftEntity> doInBackground(Void... voids) {
            List<ExerciseLiftEntity> lifts = repository.getAllExercisesList();
            for (int i = 0; i < planDaySetRelation.getPlanDaySetEntityList().size(); i++) {
                PlanDaySetEntity set = planDaySetRelation.getPlanDaySetEntityList().get(i);
                for (ExerciseLiftEntity lift : lifts) {
                    if (set.getExerciseId().equalsIgnoreCase(lift.getId())) {
                        lift.setSelected(true);
                    }
                }
            }
            return lifts;
        }

        @Override
        protected void onPostExecute(List<ExerciseLiftEntity> exerciseLiftEntities) {
            super.onPostExecute(exerciseLiftEntities);
            getExercises().setValue(exerciseLiftEntities);
            List<ExerciseLiftEntity> selectedList = new ArrayList<>();
            for (int i = 0; i < exerciseLiftEntities.size(); i++) {
                if (exerciseLiftEntities.get(i).isSelected()) {
                    selectedList.add(exerciseLiftEntities.get(i));
                }
            }
            getExercisesSelected().setValue(selectedList);
        }
    }
}
