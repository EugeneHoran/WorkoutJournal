package eh.workout.journal.com.workoutjournal.ui.entry;


import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseOrmEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalDateEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;
import eh.workout.journal.com.workoutjournal.util.DateHelper;
import eh.workout.journal.com.workoutjournal.util.EquationsHelper;

public class EntryViewModelNew extends AndroidViewModel {
    public ObservableField<String> toolbarTitle = new ObservableField<>("Workout");
    public ObservableField<Boolean> showNoItems = new ObservableField<>(false);
    public ObservableField<Boolean> dataLoaded = new ObservableField<>(false);
    private final MediatorLiveData<ExerciseSetRepRelation> observableSetReps;

    private JournalRepository repository;
    private Long dateId;
    private LiveData<ExerciseLiftEntity> liftEntityLiveData;
    private ExerciseLiftEntity liftEntity;
    private LiveData<ExerciseOrmEntity> ormEntityLiveData;
    private ExerciseOrmEntity ormEntity;
    private LiveData<JournalDateEntity> dateEntityLiveData;
    private LiveData<JournalSetEntity> setEntityLiveData;
    private JournalSetEntity setEntity;
    private List<JournalRepEntity> repEntityList = new ArrayList<>();

    private boolean onPauseDeleteCalled = true;

    void onPauseDelete() {
        if (repEntityList.size() == 0) {
            removeObservers();
            onPauseDeleteCalled = true;
            if (setEntity != null) {
                repository.deleteSet(setEntity);
            }
        }
    }

    void onResume() {
        if (onPauseDeleteCalled) {
            liftEntityLiveData.observeForever(exerciseLiftObserver);
            onPauseDeleteCalled = false;
        }
    }

    public EntryViewModelNew(@NonNull JournalApplication application, String exerciseId, Long timestamp) {
        super(application);
        this.repository = application.getRepository();
        Long[] startEndTime = DateHelper.getStartAndEndTimestamp(timestamp);
        this.dateId = startEndTime[0];
        // Observe Forever Data
        liftEntityLiveData = repository.getExerciseByIdLive(exerciseId);
        dateEntityLiveData = repository.getDateByTimestampLive(startEndTime);
        setEntityLiveData = repository.getSetByExerciseIdAndDateId(exerciseId, dateId);
        ormEntityLiveData = repository.getOneRepMaxByExerciseId(exerciseId);
        // Observe List data
        observableSetReps = new MediatorLiveData<>();
        observableSetReps.addSource(
                repository.getEntrySetRepsAndOrm(exerciseId, startEndTime[0], startEndTime[1]),
                new Observer<ExerciseSetRepRelation>() {
                    @Override
                    public void onChanged(@Nullable ExerciseSetRepRelation exerciseSetRepRelation) {
                        if (exerciseSetRepRelation != null) {
                            repEntityList.clear();
                            repEntityList.addAll(exerciseSetRepRelation.getJournalRepEntityList());
                            showNoItems.set(exerciseSetRepRelation.getJournalRepEntityList().size() == 0);
                            observableSetReps.setValue(exerciseSetRepRelation);
                            setEntity = exerciseSetRepRelation.getJournalSetEntity();
                        } else {
                            setEntity = null;
                            showNoItems.set(true);
                        }
                    }
                });
    }

    LiveData<ExerciseSetRepRelation> getSetRepsRelation() {
        return observableSetReps;
    }

    void saveRep(String weight, String reps) {
        double oneRepMax = EquationsHelper.getOneRepMax(weight, reps);
        JournalRepEntity journalRepEntity = new JournalRepEntity();
        journalRepEntity.setId(UUID.randomUUID().toString());
        journalRepEntity.setTimestamp(dateId);
        journalRepEntity.setPosition(EquationsHelper.getRepPosition(observableSetReps.getValue()));
        journalRepEntity.setLiftName(liftEntity.getName());
        journalRepEntity.setReps(reps);
        journalRepEntity.setWeight(weight);
        journalRepEntity.setJournalSetId(setEntity.getId());
        journalRepEntity.setExerciseId(liftEntity.getId());
        journalRepEntity.setOneRepMax(oneRepMax);
        if (ormEntity == null) {
            ExerciseOrmEntity exerciseOrmEntity = new ExerciseOrmEntity();
            exerciseOrmEntity.setId(UUID.randomUUID().toString());
            exerciseOrmEntity.setName(liftEntity.getName());
            exerciseOrmEntity.setOneRepMax(oneRepMax);
            exerciseOrmEntity.setWeight(weight);
            exerciseOrmEntity.setReps(reps);
            exerciseOrmEntity.setTimestamp(dateId);
            exerciseOrmEntity.setExerciseId(liftEntity.getId());
            exerciseOrmEntity.setRepId(journalRepEntity.getId());
            repository.insertRepAndOrmTransaction(journalRepEntity, exerciseOrmEntity);
        } else {
            if (oneRepMax >= ormEntity.getOneRepMax()) {
                ormEntity.setRepId(journalRepEntity.getId());
                ormEntity.setOneRepMax(oneRepMax);
                ormEntity.setWeight(weight);
                ormEntity.setReps(reps);
                ormEntity.setTimestamp(dateId);
                repository.insertRepAndUpdateOrmTransaction(journalRepEntity, ormEntity);
            } else {
                repository.insertReps(journalRepEntity);
            }
        }
    }

    void deleteRep(JournalRepEntity repEntity, List<JournalRepEntity> repEntityListUpdated) {
        for (int i = 0; i < repEntityListUpdated.size(); i++) {
            repEntityListUpdated.get(i).setPosition(i + 1);
        }
        repository.deleteRepAndUpdateOrm(repEntity, repEntityListUpdated, ormEntity);
    }

    void updateRep(JournalRepEntity repEntity) {
        repository.updateRepNew(repEntity, ormEntity);
    }

    /**
     * Init Primary data
     * <p>
     * If they do not exist create them
     * If the user leaves without adding reps, delete them
     */
    private Observer<ExerciseLiftEntity> exerciseLiftObserver = new Observer<ExerciseLiftEntity>() {
        @Override
        public void onChanged(@Nullable ExerciseLiftEntity exerciseLiftEntity) {
            if (exerciseLiftEntity != null) {
                liftEntity = exerciseLiftEntity;
                toolbarTitle.set(exerciseLiftEntity.getName());
                if (liftEntityLiveData.hasActiveObservers()) {
                    liftEntityLiveData.removeObserver(exerciseLiftObserver);
                }
                ormEntityLiveData.observeForever(ormObserver);
                dateEntityLiveData.observeForever(journalDateEntityObserver);
                setEntityLiveData.observeForever(journalSetEntityObserver);
            }
        }
    };

    private Observer<ExerciseOrmEntity> ormObserver = new Observer<ExerciseOrmEntity>() {
        @Override
        public void onChanged(@Nullable ExerciseOrmEntity exerciseOrmEntity) {
            ormEntity = exerciseOrmEntity;
        }
    };

    private Observer<JournalDateEntity> journalDateEntityObserver = new Observer<JournalDateEntity>() {
        @Override
        public void onChanged(@Nullable JournalDateEntity journalDateEntity) {
            if (journalDateEntity == null) {
                JournalDateEntity newDateEntity = new JournalDateEntity();
                newDateEntity.setId(dateId);
                newDateEntity.setTimestamp(dateId);
                repository.insertDate(newDateEntity);
            } else {
                if (dateEntityLiveData.hasActiveObservers()) {
                    dateEntityLiveData.removeObserver(journalDateEntityObserver);
                }
            }
        }
    };

    private Observer<JournalSetEntity> journalSetEntityObserver = new Observer<JournalSetEntity>() {
        @Override
        public void onChanged(@Nullable JournalSetEntity journalSetEntity) {
            setEntity = journalSetEntity;
            if (journalSetEntity == null) {
                JournalSetEntity newSetEntity = new JournalSetEntity();
                newSetEntity.setId(UUID.randomUUID().toString());
                newSetEntity.setName(liftEntity.getName());
                newSetEntity.setTimestamp(dateId);
                newSetEntity.setExerciseId(liftEntity.getId());
                newSetEntity.setDateId(dateId);
                repository.insertSet(newSetEntity);
            } else {
                dataLoaded.set(true);
                if (setEntityLiveData.hasActiveObservers()) {
                    setEntityLiveData.removeObserver(journalSetEntityObserver);
                }
            }
        }
    };

    @Override
    protected void onCleared() {
        super.onCleared();
        removeObservers();
    }

    private void removeObservers() {
        dataLoaded.set(false);
        if (dateEntityLiveData.hasActiveObservers()) {
            dateEntityLiveData.removeObserver(journalDateEntityObserver);
        }
        if (liftEntityLiveData.hasActiveObservers()) {
            liftEntityLiveData.removeObserver(exerciseLiftObserver);
        }
        if (dateEntityLiveData.hasActiveObservers()) {
            dateEntityLiveData.removeObserver(journalDateEntityObserver);
        }
        if (setEntityLiveData.hasActiveObservers()) {
            setEntityLiveData.removeObserver(journalSetEntityObserver);
        }
    }
}
