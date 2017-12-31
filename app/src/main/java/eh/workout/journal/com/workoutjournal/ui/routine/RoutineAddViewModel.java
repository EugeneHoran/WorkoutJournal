package eh.workout.journal.com.workoutjournal.ui.routine;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.RoutineEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.RoutineSetEntity;
import eh.workout.journal.com.workoutjournal.model.DaySelector;

public class RoutineAddViewModel extends AndroidViewModel {
    private JournalRepository repository;
    private MutableLiveData<List<ExerciseLiftEntity>> liftList;
    private List<ExerciseLiftEntity> liftListSelected = new ArrayList<>();
    private List<DaySelector> daySelectorList;
    private String daysString;

    public RoutineAddViewModel(@NonNull Application application) {
        super(application);
        repository = ((JournalApplication) application).getRepository();
        new TaskLifts().execute();
    }

    /**
     * Insert
     */

    void insertNewPlan(String planName) {
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
     * Days
     */

    void setDaysString(String daysString) {
        this.daysString = daysString;
    }

    void setDaySelectorList(List<DaySelector> daySelectorList) {
        this.daySelectorList = daySelectorList;
    }

    List<DaySelector> getDaySelectorList() {
        return daySelectorList;
    }

    /**
     * Lifts
     */
    MutableLiveData<List<ExerciseLiftEntity>> getLiftList() {
        if (liftList == null) {
            liftList = new MutableLiveData<>();
        }
        return liftList;
    }

    void setLiftListSelected(List<ExerciseLiftEntity> exerciseLiftEntities) {
        liftListSelected.clear();
        liftListSelected.addAll(exerciseLiftEntities);
    }

    List<ExerciseLiftEntity> getLiftListSelected() {
        return liftListSelected;
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
            for (int i = 0; i < exerciseLiftEntities.size(); i++) {
                Log.e("Testing", exerciseLiftEntities.get(i).getName());
            }
            getLiftList().setValue(exerciseLiftEntities);
        }
    }
}
