package eh.workout.journal.com.workoutjournal.ui.plan.edit;

import android.annotation.SuppressLint;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.CustomTypeConverters;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.PlanSetRelation;
import eh.workout.journal.com.workoutjournal.model.DaySelector;
import eh.workout.journal.com.workoutjournal.util.ExerciseDataHelper;


public class EditPlanViewModel extends AndroidViewModel {
    public ObservableBoolean noSelectedDays = new ObservableBoolean(false);
    boolean titleSet = false;

    private String planId;
    private JournalRepository repository;
    private PlanSetRelation planSet;
    private final MutableLiveData<PlanSetRelation> planSetRelation;
    private final MutableLiveData<List<ExerciseLiftEntity>> allExercises;
    private final MutableLiveData<List<ExerciseLiftEntity>> allSelectedExercises;
    private final MutableLiveData<List<DaySelector>> allDaysOfWeek;
    private final MutableLiveData<List<DaySelector>> selectedDaysOfWeek;

    public EditPlanViewModel(@NonNull JournalApplication application, String planId) {
        super(application);
        this.planId = planId;
        repository = application.getRepository();
        planSetRelation = new MutableLiveData<>();
        allExercises = new MutableLiveData<>();
        allSelectedExercises = new MutableLiveData<>();
        allDaysOfWeek = new MutableLiveData<>();
        selectedDaysOfWeek = new MutableLiveData<>();
    }

    void initEditData() {
        new TaskPlan().execute(planId);
    }

    void updatePlan(String planTitle) {
        PlanEntity newPlanEntity = new PlanEntity();
        newPlanEntity.setId(planSet.getPlanEntity().getId());
        newPlanEntity.setPlanDayString(getDaysString());
        newPlanEntity.setPlanName(planTitle);
        List<PlanSetEntity> newPlanSetList = new ArrayList<>();
        for (int i = 0; i < getAllSelectedExercises().getValue().size(); i++) {
            newPlanSetList.add(new PlanSetEntity(getAllSelectedExercises().getValue().get(i), newPlanEntity.getId()));
        }
        repository.deleteAndInsertPlan(planSet.getPlanEntity(), newPlanEntity, newPlanSetList);
    }

    void deletePlan() {
        repository.deletePlan(planSet.getPlanEntity());
    }

    MutableLiveData<PlanSetRelation> getPlanSetRelation() {
        return planSetRelation;
    }

    // Days
    MutableLiveData<List<DaySelector>> getAllDaysOfWeek() {
        return allDaysOfWeek;
    }

    MutableLiveData<List<DaySelector>> getSelectedDaysOfWeek() {
        return selectedDaysOfWeek;
    }

    // Exercises
    MutableLiveData<List<ExerciseLiftEntity>> getAllExercises() {
        return allExercises;
    }

    MutableLiveData<List<ExerciseLiftEntity>> getAllSelectedExercises() {
        return allSelectedExercises;
    }


    @SuppressLint("StaticFieldLeak")
    class TaskPlan extends AsyncTask<String, Void, PlanSetRelation> {
        @Override
        protected PlanSetRelation doInBackground(String... strings) {
            return repository.getPlanSetRelation(strings[0]);
        }

        @Override
        protected void onPostExecute(PlanSetRelation planSetResponse) {
            planSet = planSetResponse;
            super.onPostExecute(planSetResponse);
            getPlanSetRelation().setValue(planSetResponse);
            getAllDaysOfWeek().setValue(daySelectorList(planSetResponse.getDaysList()));
            titleSet = true;
            new TaskLifts().execute();
        }
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
            List<ExerciseLiftEntity> selectedExercises = new ArrayList<>();
            for (int i = 0; i < getPlanSetRelation().getValue().getPlanSetEntityList().size(); i++) {
                PlanSetEntity set = getPlanSetRelation().getValue().getPlanSetEntityList().get(i);
                for (ExerciseLiftEntity lift : exerciseLiftEntities) {
                    if (set.getExerciseId().equalsIgnoreCase(lift.getId())) {
                        lift.setSelected(true);
                        selectedExercises.add(lift);
                    }
                }
            }
            getAllSelectedExercises().setValue(selectedExercises);
            getAllExercises().setValue(exerciseLiftEntities);
        }
    }

    private List<DaySelector> daySelectorList(List<Integer> selectedDaysInt) {
        noSelectedDays.set(selectedDaysInt.size() == 0);
        List<DaySelector> selectedDaysOfWeek = new ArrayList<>();
        List<DaySelector> daySelectorList = ExerciseDataHelper.getDays();
        for (int i = 0; i < daySelectorList.size(); i++) {
            DaySelector daySelector = daySelectorList.get(i);
            for (int j = 0; j < selectedDaysInt.size(); j++) {
                if (daySelector.getDayInt() == selectedDaysInt.get(j)) {
                    daySelector.setSelected(true);
                    selectedDaysOfWeek.add(daySelector);
                }
            }
        }
        getSelectedDaysOfWeek().setValue(selectedDaysOfWeek);
        return daySelectorList;
    }


    private String getDaysString() {
        List<Integer> daySelectorList = new ArrayList<>();
        for (int i = 0; i < getSelectedDaysOfWeek().getValue().size(); i++) {
            daySelectorList.add(getSelectedDaysOfWeek().getValue().get(i).getDayInt());
        }
        return CustomTypeConverters.fromDayListToString(daySelectorList);
    }

}
