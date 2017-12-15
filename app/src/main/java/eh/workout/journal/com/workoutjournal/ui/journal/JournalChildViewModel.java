package eh.workout.journal.com.workoutjournal.ui.journal;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;
import eh.workout.journal.com.workoutjournal.util.DateHelper;


public class JournalChildViewModel extends AndroidViewModel implements LifecycleObserver {
    private JournalRepository repository;
    private LiveData<List<ExerciseSetRepRelation>> observeSetAndReps;
    private LiveData<List<JournalSetEntity>> setEntityList;

    private Long timestamp;

    public JournalChildViewModel(@NonNull Application application, Long timestamp) {
        super(application);
        JournalApplication journalApplication = (JournalApplication) application;
        this.timestamp = timestamp;
        repository = journalApplication.getRepository();
        setEntityList = repository.getSetListByIdLive(DateHelper.getStartAndEndTimestamp(timestamp));
        observeSetAndReps = repository.getExerciseSetRepRelationLive(DateHelper.getStartAndEndTimestamp(timestamp));
    }

    LiveData<List<ExerciseSetRepRelation>> getSetAndReps() {
        return observeSetAndReps;
    }

    void deleteSet(JournalSetEntity setEntity) {
        repository.deleteSet(setEntity);
    }

    private Observer<List<JournalSetEntity>> ormObserver = new Observer<List<JournalSetEntity>>() {
        @Override
        public void onChanged(@Nullable List<JournalSetEntity> exerciseOrmEntity) {
            if (exerciseOrmEntity != null) {
                if (exerciseOrmEntity.size() == 0) {
                    repository.deleteDateSetsNull(timestamp);
                }
            }
        }
    };

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void initObserver() {
        if (!setEntityList.hasActiveObservers()) {
            setEntityList.observeForever(ormObserver);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void removeObserver() {
        if (setEntityList.hasActiveObservers()) {
            setEntityList.removeObserver(ormObserver);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (setEntityList.hasActiveObservers()) {
            setEntityList.removeObserver(ormObserver);
        }
    }
}
