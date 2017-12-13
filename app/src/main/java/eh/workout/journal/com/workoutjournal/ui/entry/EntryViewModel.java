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

import java.util.Date;
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

public class EntryViewModel extends AndroidViewModel {
    public ObservableField<String> toolbarTitle = new ObservableField<>("Workout");
    public ObservableField<Boolean> showNoItems = new ObservableField<>(false);
    private JournalRepository repository;
    private String exerciseId;
    private Long[] startEndTime;

    private ExerciseLiftEntity liftEntity;
    private ExerciseOrmEntity ormEntity;

    private boolean isDataLoaded = false;
    private String dateId = null;
    private boolean dateExist = false;
    private String setId = null;
    private boolean setExist = false;
    private final MediatorLiveData<ExerciseSetRepRelation> observableSetReps;
    private LiveData<List<ExerciseSetRepRelation>> observeHistory;

    void onResume() {
        new ExerciseLiftTask().execute();
    }

    void onPauseEmpty() {
        repository.deleteSet(setId);
    }

    EntryViewModel(@NonNull JournalApplication application, String exerciseId, Long timestamp) {
        super(application);
        repository = application.getRepository();
        this.exerciseId = exerciseId;
        startEndTime = DateHelper.getStartAndEndTimestamp(timestamp);
        observeHistory = repository.loadExerciseSetRepsHistory(exerciseId, startEndTime[0]);
        observableSetReps = new MediatorLiveData<>();
        observableSetReps.addSource(
                repository.loadExerciseSetReps(exerciseId, startEndTime[0], startEndTime[1]),
                new Observer<ExerciseSetRepRelation>() {
                    @Override
                    public void onChanged(@Nullable ExerciseSetRepRelation exerciseSetRepRelation) {
                        if (exerciseSetRepRelation != null) {
                            showNoItems.set(exerciseSetRepRelation.getJournalRepEntityList().size() == 0);
                            observableSetReps.setValue(exerciseSetRepRelation);
                        } else {
                            showNoItems.set(true);
                        }
                    }
                });
    }

    LiveData<List<ExerciseSetRepRelation>> getHistory() {
        return observeHistory;
    }

    /**
     * Observe data changes
     *
     * @return ExerciseSetRepRelation
     */
    LiveData<ExerciseSetRepRelation> getObservableSetReps() {
        return observableSetReps;
    }

    void saveSet(String weight, String reps) throws Exception {
        double oneRepMax = EquationsHelper.getOneRepMax(weight, reps);
        if (liftEntity != null && isDataLoaded) {
            ExerciseLiftEntity exercise = liftEntity;
            JournalRepEntity journalRepEntity = new JournalRepEntity();
            journalRepEntity.setId(UUID.randomUUID().toString());
            journalRepEntity.setTimestamp(startEndTime[0]);
            journalRepEntity.setPosition(EquationsHelper.getRepPosition(observableSetReps.getValue()));
            journalRepEntity.setLiftName(exercise.getName());
            journalRepEntity.setReps(reps);
            journalRepEntity.setWeight(weight);
            journalRepEntity.setJournalSetId(setId);
            journalRepEntity.setOneRepMax(oneRepMax);
            if (ormEntity == null) {
                createOrm(oneRepMax, journalRepEntity.getId());
            } else {
                if (oneRepMax > ormEntity.getOneRepMax()) {
                    ormEntity.setOneRepMax(oneRepMax);
                    ormEntity.setRepId(journalRepEntity.getId());
                    repository.updateExerciseOrm(ormEntity);
                }
            }
            if (!dateExist && !setExist) {
                repository.insertDateSetRep(generateDateEntity(), generateSetEntity(exercise), journalRepEntity);
                dateExist = true;
                setExist = true;
            } else if (dateExist && !setExist) {
                repository.insertSetRep(generateSetEntity(exercise), journalRepEntity);
                dateExist = true;
                setExist = true;
            } else {
                repository.insertReps(journalRepEntity);
                dateExist = true;
                setExist = true;
            }
        }
    }

    void updateRepEntity(JournalRepEntity repEntity) {
        double oneRepMax = EquationsHelper.getOneRepMax(repEntity.getWeight(), repEntity.getReps());
        if (ormEntity == null) {
            createOrm(oneRepMax, repEntity.getId());
        } else {
            if (oneRepMax > ormEntity.getOneRepMax()) {
                ormEntity.setOneRepMax(oneRepMax);
                ormEntity.setRepId(repEntity.getId());
                repository.updateExerciseOrm(ormEntity);
            }
        }
        repEntity.setOneRepMax(oneRepMax);
        repository.updateRep(repEntity);
    }


    void deleteRepAndFilter(JournalRepEntity repEntity, List<JournalRepEntity> repEntityList) {
        for (int i = 0; i < repEntityList.size(); i++) {
            repEntityList.get(i).setPosition(i + 1);
        }
        repository.deleteRepAndUpdate(repEntity, repEntityList);
    }

    /**
     * Date Generators
     */

    @SuppressWarnings("ConstantConditions")
    private void createOrm(double orm, String repId) {
        ExerciseOrmEntity newOrmEntity = new ExerciseOrmEntity();
        newOrmEntity.setId(UUID.randomUUID().toString());
        newOrmEntity.setName(liftEntity.getName());
        newOrmEntity.setOneRepMax(orm);
        newOrmEntity.setTimestamp(DateHelper.getTimestamp(new Date()));
        newOrmEntity.setExerciseId(exerciseId);
        newOrmEntity.setRepId(repId);
        repository.insertOrmEntity(newOrmEntity);
        ormEntity = newOrmEntity;
    }

    private JournalDateEntity generateDateEntity() {
        JournalDateEntity journalDateEntity = new JournalDateEntity();
        journalDateEntity.setId(dateId);
        journalDateEntity.setTimestamp(startEndTime[0]);
        return journalDateEntity;
    }

    private JournalSetEntity generateSetEntity(ExerciseLiftEntity exercise) {
        JournalSetEntity journalSetEntity = new JournalSetEntity();
        journalSetEntity.setId(setId);
        journalSetEntity.setName(exercise.getName());
        journalSetEntity.setTimestamp(startEndTime[0]);
        journalSetEntity.setExerciseId(exercise.getId());
        journalSetEntity.setJournalDateId(dateId);
        return journalSetEntity;
    }

    /**
     * AsyncTasks
     * <p>
     * Load Initial Data
     */

    @SuppressLint("StaticFieldLeak")
    class ExerciseLiftTask extends AsyncTask<Void, Void, ExerciseLiftEntity> {
        @Override
        protected ExerciseLiftEntity doInBackground(Void... voids) {
            return repository.loadExerciseById(exerciseId);
        }

        @Override
        protected void onPostExecute(ExerciseLiftEntity exerciseLiftEntity) {
            super.onPostExecute(exerciseLiftEntity);
            toolbarTitle.set(exerciseLiftEntity.getName());
            liftEntity = exerciseLiftEntity;
            new OneRepMaxTask().execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class OneRepMaxTask extends AsyncTask<Void, Void, ExerciseOrmEntity> {
        @Override
        protected ExerciseOrmEntity doInBackground(Void... voids) {
            return repository.loadExerciseOrm(exerciseId);
        }

        @Override
        protected void onPostExecute(ExerciseOrmEntity exerciseOrmEntity) {
            super.onPostExecute(exerciseOrmEntity);
            ormEntity = exerciseOrmEntity;
            new DateTask().execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class DateTask extends AsyncTask<Void, Void, JournalDateEntity> {
        @Override
        protected JournalDateEntity doInBackground(Void... voids) {
            return repository.getDateRun(startEndTime[0], startEndTime[1]);
        }

        @Override
        protected void onPostExecute(JournalDateEntity dateEntity) {
            super.onPostExecute(dateEntity);
            if (dateEntity != null) {
                dateId = dateEntity.getId();
                dateExist = true;
                new SetTask().execute();
            } else {
                dateId = UUID.randomUUID().toString();
                dateExist = false;
                setId = UUID.randomUUID().toString();
                setExist = false;
                isDataLoaded = true;
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class SetTask extends AsyncTask<Void, Void, JournalSetEntity> {
        @Override
        protected JournalSetEntity doInBackground(Void... voids) {
            return repository.getSet(exerciseId, dateId);
        }

        @Override
        protected void onPostExecute(JournalSetEntity setEntity) {
            super.onPostExecute(setEntity);
            if (setEntity != null) {
                setId = setEntity.getId();
                setExist = true;
            } else {
                setId = UUID.randomUUID().toString();
                setExist = false;
            }
            isDataLoaded = true;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
