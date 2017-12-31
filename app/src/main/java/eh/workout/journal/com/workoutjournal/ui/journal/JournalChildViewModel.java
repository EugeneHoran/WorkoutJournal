package eh.workout.journal.com.workoutjournal.ui.journal;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;
import eh.workout.journal.com.workoutjournal.db.relations.RoutineSetRelation;
import eh.workout.journal.com.workoutjournal.util.DateHelper;


public class JournalChildViewModel extends AndroidViewModel {
    private JournalRepository repository;
    private MediatorLiveData<List<ExerciseSetRepRelation>> observeSetAndReps;
    private MutableLiveData<List<RoutineSetRelation>> observeRoutines;
    private Long timestamp;

    public JournalChildViewModel(@NonNull Application application, final Long timestamp) {
        super(application);
        this.timestamp = timestamp;
        JournalApplication journalApplication = (JournalApplication) application;
        repository = journalApplication.getRepository();
        observeSetAndReps = new MediatorLiveData<>();
        observeSetAndReps.addSource(repository.getExerciseSetRepRelationLive(DateHelper.getStartAndEndTimestamp(timestamp)), new Observer<List<ExerciseSetRepRelation>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseSetRepRelation> exerciseSetRepRelations) {
                if (deletingSet == null) {
                    observeSetAndReps.setValue(exerciseSetRepRelations);
                }
                new RoutineTask().execute(timestamp);
            }
        });
    }

    void resetTasks() {
        new RoutineTask().execute(timestamp);
    }

    LiveData<List<ExerciseSetRepRelation>> getSetAndReps() {
        return observeSetAndReps;
    }

    MutableLiveData<List<RoutineSetRelation>> getRoutines() {
        if (observeRoutines == null) {
            observeRoutines = new MutableLiveData<>();
        }
        return observeRoutines;
    }

    private JournalSetEntity deletingSet;


    void cancelDeleteSet() {
        deleteTimer.cancel();
        deletingSet = null;
    }

    void deleteSet(final JournalSetEntity setEntity) {
        deleteTimer.cancel();
        if (deletingSet != null) {
            repository.deleteSet(deletingSet);
            deletingSet = null;
        }
        deletingSet = setEntity;
        deleteTimer.start();
    }

    private CountDownTimer deleteTimer = new CountDownTimer(2500, 1000) {
        @Override
        public void onTick(long l) {
        }

        @Override
        public void onFinish() {
            repository.deleteSet(deletingSet);
            deletingSet = null;
        }
    };

    @SuppressLint("StaticFieldLeak")
    class RoutineTask extends AsyncTask<Long, Void, List<RoutineSetRelation>> {
        @Override
        protected List<RoutineSetRelation> doInBackground(Long... integers) {
            List<RoutineSetRelation> routineSetRelations = repository.getRoutineSetRelationList(DateHelper.getDayOfWeek(integers[0]));
            return new JournalHelper().getPlanSetsCompleteList(getSetAndReps().getValue(), routineSetRelations);
        }

        @Override
        protected void onPostExecute(List<RoutineSetRelation> routineSetRelations) {
            super.onPostExecute(routineSetRelations);
            getRoutines().setValue(routineSetRelations);
        }
    }
}
