package eh.workout.journal.com.workoutjournal.ui.journal;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanDayEntity;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;
import eh.workout.journal.com.workoutjournal.db.relations.PlanDaySetRelation;
import eh.workout.journal.com.workoutjournal.db.relations.RoutineSetRelation;
import eh.workout.journal.com.workoutjournal.util.DateHelper;


public class JournalParentViewModel extends AndroidViewModel {
    private JournalRepository repository;
    private Long timestamp;
    private MediatorLiveData<List<ExerciseSetRepRelation>> observeSetAndReps;
    private MutableLiveData<List<Object>> routinePlanList;

    public JournalParentViewModel(@NonNull Application application) {
        super(application);
        repository = ((JournalApplication) application).getRepository();
    }

    MediatorLiveData<List<ExerciseSetRepRelation>> getSetAndRepsList() {
        if (observeSetAndReps == null) {
            observeSetAndReps = new MediatorLiveData<>();
        }
        return observeSetAndReps;
    }

    MutableLiveData<List<Object>> getRoutinePlanList() {
        if (routinePlanList == null) {
            routinePlanList = new MutableLiveData<>();
        }
        return routinePlanList;
    }

    void initJournalData(final Long timestamp) {
        this.timestamp = timestamp;
        getSetAndRepsList().setValue(null);
        getSetAndRepsList().addSource(repository.getExerciseSetRepRelationLive(DateHelper.getStartAndEndTimestamp(timestamp)), new Observer<List<ExerciseSetRepRelation>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseSetRepRelation> exerciseSetRepRelations) {
                getSetAndRepsList().setValue(exerciseSetRepRelations);
                resetRoutinePlanTasks();
            }
        });
    }

    void resetRoutinePlanTasks() {
        if (timestamp != null) {
            new RoutinePlanTask().execute(timestamp);
        }
    }

    void deletePlanDayEntity(PlanDayEntity planDayEntity) {
        repository.deletePlanDayEntity(planDayEntity);
    }

    @SuppressLint("StaticFieldLeak")
    class RoutinePlanTask extends AsyncTask<Long, Void, List<Object>> {
        @Override
        protected List<Object> doInBackground(Long... integers) {
            List<RoutineSetRelation> routineSetRelations = repository.getRoutineSetRelationList(DateHelper.getDayOfWeek(integers[0]));
            List<PlanDaySetRelation> planDaySetRelations = repository.getPlanSetDayRelationList(integers[0]);
            return new JournalHelper().getCompleteList(getSetAndRepsList().getValue(), routineSetRelations, planDaySetRelations);
        }

        @Override
        protected void onPostExecute(List<Object> objectList) {
            super.onPostExecute(objectList);
            getRoutinePlanList().setValue(objectList);
        }
    }
}
