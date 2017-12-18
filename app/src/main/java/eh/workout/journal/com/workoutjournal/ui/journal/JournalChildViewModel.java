package eh.workout.journal.com.workoutjournal.ui.journal;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;

import java.util.List;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;
import eh.workout.journal.com.workoutjournal.util.DateHelper;


public class JournalChildViewModel extends AndroidViewModel implements LifecycleObserver {
    private JournalRepository repository;
    private LiveData<List<ExerciseSetRepRelation>> observeSetAndReps;
    boolean deleteSet = true;

    public JournalChildViewModel(@NonNull Application application, Long timestamp) {
        super(application);
        JournalApplication journalApplication = (JournalApplication) application;
        repository = journalApplication.getRepository();
        observeSetAndReps = repository.getExerciseSetRepRelationLive(DateHelper.getStartAndEndTimestamp(timestamp));
    }

    LiveData<List<ExerciseSetRepRelation>> getSetAndReps() {
        return observeSetAndReps;
    }

    void deleteSet(final JournalSetEntity setEntity) {
        new CountDownTimer(2500, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                if (deleteSet) {
                    repository.deleteSet(setEntity);
                }
                deleteSet = true;
            }
        }.start();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void initObserver() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void removeObserver() {

    }

    @Override
    protected void onCleared() {
        super.onCleared();

    }
}
