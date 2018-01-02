package eh.workout.journal.com.workoutjournal.ui.routine.edit;

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
import eh.workout.journal.com.workoutjournal.db.entinty.RoutineEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.RoutineSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.RoutineSetRelation;
import eh.workout.journal.com.workoutjournal.model.DaySelector;
import eh.workout.journal.com.workoutjournal.util.DataHelper;


public class EditRoutineViewModel extends AndroidViewModel {
    public ObservableBoolean noSelectedDays = new ObservableBoolean(false);
    boolean titleSet = false;

    private String routineId;
    private JournalRepository repository;
    private RoutineSetRelation routineSet;
    private final MutableLiveData<RoutineSetRelation> routineSetRelation;
    private final MutableLiveData<List<ExerciseLiftEntity>> allExercises;
    private final MutableLiveData<List<ExerciseLiftEntity>> allSelectedExercises;
    private final MutableLiveData<List<DaySelector>> allDaysOfWeek;
    private final MutableLiveData<List<DaySelector>> selectedDaysOfWeek;

    public EditRoutineViewModel(@NonNull JournalApplication application, String routineId) {
        super(application);
        this.routineId = routineId;
        repository = application.getRepository();
        routineSetRelation = new MutableLiveData<>();
        allExercises = new MutableLiveData<>();
        allSelectedExercises = new MutableLiveData<>();
        allDaysOfWeek = new MutableLiveData<>();
        selectedDaysOfWeek = new MutableLiveData<>();
    }

    void initEditData() {
        new TaskRoutine().execute(routineId);
    }

    void updatePlan(String planTitle) {
        RoutineEntity newRoutineEntity = new RoutineEntity();
        newRoutineEntity.setId(routineSet.getRoutineEntity().getId());
        newRoutineEntity.setRoutineDayListString(getDaysString());
        newRoutineEntity.setRoutineName(planTitle);
        List<RoutineSetEntity> newPlanSetList = new ArrayList<>();
        for (int i = 0; i < getAllSelectedExercises().getValue().size(); i++) {
            newPlanSetList.add(new RoutineSetEntity(getAllSelectedExercises().getValue().get(i), newRoutineEntity.getId()));
        }
        repository.deleteAndInsertRoutine(routineSet.getRoutineEntity(), newRoutineEntity, newPlanSetList);
    }

    void deletePlan() {
        repository.deleteRoutine(routineSet.getRoutineEntity());
    }

    MutableLiveData<RoutineSetRelation> getPlanSetRelation() {
        return routineSetRelation;
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
    class TaskRoutine extends AsyncTask<String, Void, RoutineSetRelation> {
        @Override
        protected RoutineSetRelation doInBackground(String... strings) {
            return repository.getRoutineSetRelation(strings[0]);
        }

        @Override
        protected void onPostExecute(RoutineSetRelation planSetResponse) {
            routineSet = planSetResponse;
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
                RoutineSetEntity set = getPlanSetRelation().getValue().getPlanSetEntityList().get(i);
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
        List<DaySelector> daySelectorList = new DataHelper().getDays();
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
