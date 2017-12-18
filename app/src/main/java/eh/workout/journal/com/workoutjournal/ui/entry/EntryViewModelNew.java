package eh.workout.journal.com.workoutjournal.ui.entry;


import android.annotation.SuppressLint;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.databinding.ObservableField;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.UUID;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseOrmEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;
import eh.workout.journal.com.workoutjournal.util.DateHelper;
import eh.workout.journal.com.workoutjournal.util.OrmHelper;

public class EntryViewModelNew extends AndroidViewModel {
    public ObservableField<String> toolbarTitle = new ObservableField<>("Workout");
    public ObservableField<Boolean> showNoItems = new ObservableField<>(false);
    public ObservableField<Boolean> dataLoaded = new ObservableField<>(false);
    private final MediatorLiveData<ExerciseSetRepRelation> observableSetReps;

    private JournalRepository repository;
    private String exerciseId;
    private Long dateId;

    private ExerciseLiftEntity liftEntity;
    private LiveData<ExerciseSetRepRelation> relationLiveData;
    private ExerciseSetRepRelation setRepRelation;
    private LiveData<ExerciseOrmEntity> ormEntityLiveData;
    private ExerciseOrmEntity ormEntity;

    public EntryViewModelNew(@NonNull JournalApplication application, String exerciseId, Long timestamp) {
        super(application);
        this.repository = application.getRepository();
        this.exerciseId = exerciseId;
        Long[] startEndTime = DateHelper.getStartAndEndTimestamp(timestamp);
        this.dateId = startEndTime[0];
        ormEntityLiveData = repository.getOneRepMaxByExerciseId(exerciseId);
        relationLiveData = repository.getEntrySetRepsAndOrm(exerciseId, startEndTime[0], startEndTime[1]);
        observableSetReps = new MediatorLiveData<>();
    }

    void onResume() {
        new LiftTask().execute(exerciseId);
    }

    void onPauseDelete() {
        observableSetReps.removeSource(relationLiveData);
        if (setRepRelation != null) {
            dataLoaded.set(false);
            ormEntity = null;
            if (setRepRelation.getJournalRepEntityList().size() == 0) {
                if (setRepRelation.getJournalSetEntity() != null) {
                    repository.deleteSet(setRepRelation.getJournalSetEntity());
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class LiftTask extends AsyncTask<String, Void, ExerciseLiftEntity> {
        @Override
        protected ExerciseLiftEntity doInBackground(String... strings) {
            return repository.getExerciseById(strings[0]);
        }

        @Override
        protected void onPostExecute(ExerciseLiftEntity exerciseLiftEntity) {
            super.onPostExecute(exerciseLiftEntity);
            liftEntity = exerciseLiftEntity;
            toolbarTitle.set(liftEntity.getName());
            initPrimaryObserver();
            dataLoaded.set(true);
        }
    }

    private void initPrimaryObserver() {
        observableSetReps.addSource(
                relationLiveData,
                new Observer<ExerciseSetRepRelation>() {
                    @Override
                    public void onChanged(@Nullable ExerciseSetRepRelation exerciseSetRepRelation) {
                        if (exerciseSetRepRelation != null) {
                            setRepRelation = exerciseSetRepRelation;
                            if (exerciseSetRepRelation.getExerciseOrmEntity().size() == 1) {
                                ormEntity = exerciseSetRepRelation.getExerciseOrmEntity().get(0);
                            }
                            showNoItems.set(exerciseSetRepRelation.getJournalRepEntityList().size() == 0);
                            for (int i = 0; i < exerciseSetRepRelation.getJournalRepEntityList().size(); i++) {
                                exerciseSetRepRelation.getJournalRepEntityList().get(i).setTempPosition(i + 1);
                            }
                            observableSetReps.setValue(exerciseSetRepRelation);
                        } else {
                            showNoItems.set(true);
                            JournalSetEntity newSetEntity = new JournalSetEntity();
                            newSetEntity.setId(UUID.randomUUID().toString());
                            newSetEntity.setName(liftEntity.getName());
                            newSetEntity.setTimestamp(dateId);
                            newSetEntity.setExerciseId(liftEntity.getId());
                            newSetEntity.setDateId(dateId);
                            newSetEntity.setExerciseInputType(liftEntity.getExerciseInputType());
                            repository.insertSet(newSetEntity);
                        }
                    }
                });
    }

    LiveData<ExerciseSetRepRelation> getSetRepsRelation() {
        return observableSetReps;
    }

    LiveData<ExerciseOrmEntity> getOrmEntityLiveData() {
        return ormEntityLiveData;
    }

    void saveRep(String weight, String reps, double oneRepMax) {
        JournalRepEntity journalRepEntity = new JournalRepEntity();
        journalRepEntity.setId(UUID.randomUUID().toString());
        journalRepEntity.setTimestamp(dateId);
        journalRepEntity.setPosition(OrmHelper.getRepPosition(observableSetReps.getValue()));
        journalRepEntity.setLiftName(liftEntity.getName());
        journalRepEntity.setReps(reps);
        journalRepEntity.setWeight(weight);
        journalRepEntity.setJournalSetId(setRepRelation.getJournalSetEntity().getId());
        journalRepEntity.setExerciseId(liftEntity.getId());
        journalRepEntity.setExerciseInputType(liftEntity.getExerciseInputType());
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
            exerciseOrmEntity.setInputType(liftEntity.getExerciseInputType());
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

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
