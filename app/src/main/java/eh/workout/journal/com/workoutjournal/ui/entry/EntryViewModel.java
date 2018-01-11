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
import android.util.Log;

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

public class EntryViewModel extends AndroidViewModel {
    public ObservableField<String> toolbarTitle = new ObservableField<>("Workout");
    public ObservableField<Boolean> showNoItems = new ObservableField<>(false);
    public ObservableField<Boolean> dataLoaded = new ObservableField<>(false);

    private JournalRepository repository;
    private String exerciseId;
    private Long dateId;
    private ExerciseLiftEntity liftEntity;
    private final MediatorLiveData<ExerciseSetRepRelation> observableSetReps;
    private final MediatorLiveData<ExerciseOrmEntity> observableOrm;
    private LiveData<ExerciseSetRepRelation> exerciseSetRepRelationLiveData;
    private ExerciseSetRepRelation setRepRelation;


    public EntryViewModel(@NonNull JournalApplication application, String exerciseId, Long timestamp) {
        super(application);
        Long[] startEndTime = DateHelper.getStartAndEndTimestamp(timestamp);
        this.repository = application.getRepository();
        this.exerciseId = exerciseId;
        this.dateId = startEndTime[0];
        observableSetReps = new MediatorLiveData<>();
        observableOrm = new MediatorLiveData<>();
        exerciseSetRepRelationLiveData = repository.getEntrySetRepsAndOrm(exerciseId, startEndTime[0], startEndTime[1]);
        observableOrm.addSource(repository.getOneRepMaxByExerciseId(exerciseId), new Observer<ExerciseOrmEntity>() {
            @Override
            public void onChanged(@Nullable ExerciseOrmEntity ormEntityReturned) {
                observableOrm.setValue(ormEntityReturned);
            }
        });
        new LiftTask().execute(exerciseId);
    }

    private void initPrimaryObserver() {
        observableSetReps.addSource(exerciseSetRepRelationLiveData, new Observer<ExerciseSetRepRelation>() {
            @Override
            public void onChanged(@Nullable ExerciseSetRepRelation exerciseSetRepRelation) {
                setRepRelation = exerciseSetRepRelation;
                if (exerciseSetRepRelation != null) {
                    showNoItems.set(exerciseSetRepRelation.getJournalRepEntityList().size() == 0);
                    for (int i = 0; i < exerciseSetRepRelation.getJournalRepEntityList().size(); i++) {
                        exerciseSetRepRelation.getJournalRepEntityList().get(i).setTempPosition(i + 1);
                    }
                    observableSetReps.setValue(exerciseSetRepRelation);
                } else {
                    showNoItems.set(true);
                    observableSetReps.setValue(null);
                }
            }
        });
    }

    LiveData<ExerciseSetRepRelation> getSetRepsRelation() {
        return observableSetReps;
    }

    MediatorLiveData<ExerciseOrmEntity> getOrmEntityLiveData() {
        return observableOrm;
    }

    @SuppressWarnings("ConstantConditions")
    void deleteRep(JournalRepEntity repEntity, List<JournalRepEntity> repEntityListUpdated) {
        if (observableSetReps.getValue().getJournalRepEntityList().size() == 1) {
            repository.deleteSet(setRepRelation.getJournalSetEntity());
            return;
        }
        for (int i = 0; i < repEntityListUpdated.size(); i++) {
            repEntityListUpdated.get(i).setPosition(i + 1);
        }
        repository.deleteRepAndUpdateOrm(repEntity, repEntityListUpdated, getOrmEntityLiveData().getValue());
    }

    void updateRep(JournalRepEntity repEntity) {
        repository.updateRepNew(repEntity, getOrmEntityLiveData().getValue());
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

    synchronized void saveRep(String weight, String reps, double oneRepMax) {
        String setId;
        if (setRepRelation == null) {
            setId = UUID.randomUUID().toString();
            JournalSetEntity newSetEntity = new JournalSetEntity();
            newSetEntity.setId(setId);
            newSetEntity.setName(liftEntity.getName());
            newSetEntity.setTimestamp(dateId);
            newSetEntity.setExerciseId(liftEntity.getId());
            newSetEntity.setDateId(dateId);
            newSetEntity.setExerciseInputType(liftEntity.getExerciseInputType());
            repository.insertSet(newSetEntity);
        } else {
            setId = setRepRelation.getJournalSetEntity().getId();
        }

        JournalRepEntity journalRepEntity = new JournalRepEntity();
        journalRepEntity.setId(UUID.randomUUID().toString());
        journalRepEntity.setTimestamp(dateId);
        journalRepEntity.setPosition(OrmHelper.getRepPosition(observableSetReps.getValue()));
        journalRepEntity.setLiftName(liftEntity.getName());
        journalRepEntity.setReps(reps);
        journalRepEntity.setWeight(weight);
        journalRepEntity.setJournalSetId(setId);
        journalRepEntity.setExerciseId(liftEntity.getId());
        journalRepEntity.setExerciseInputType(liftEntity.getExerciseInputType());
        journalRepEntity.setOneRepMax(oneRepMax);
        if (getOrmEntityLiveData().getValue() == null) {
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
            if (oneRepMax >= getOrmEntityLiveData().getValue().getOneRepMax()) {
                getOrmEntityLiveData().getValue().setRepId(journalRepEntity.getId());
                getOrmEntityLiveData().getValue().setOneRepMax(oneRepMax);
                getOrmEntityLiveData().getValue().setWeight(weight);
                getOrmEntityLiveData().getValue().setReps(reps);
                getOrmEntityLiveData().getValue().setTimestamp(dateId);
                repository.insertRepAndUpdateOrmTransaction(journalRepEntity, getOrmEntityLiveData().getValue());
            } else {
                repository.insertReps(journalRepEntity);
            }
        }
    }

}
