package eh.workout.journal.com.workoutjournal.ui.add.exercise;

import android.annotation.SuppressLint;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
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

public class AddExerciseEntryViewModel extends AndroidViewModel {
    public ObservableField<String> toolbarTitle = new ObservableField<>("Workout");
    public ObservableField<Boolean> showRepEntry = new ObservableField<>(false);

    private JournalRepository repository;
    private String exerciseId;
    private Long[] startEndTime;
    private MutableLiveData<Boolean> dataLoaded = new MutableLiveData<>();
    private MutableLiveData<ExerciseLiftEntity> exerciseLiftEntityMutableLiveData = new MutableLiveData<>();
    private ExerciseOrmEntity ormEntity;
    private String dateId = null;
    private boolean dateExist = false;
    private String setId = null;
    private boolean setExist = false;
    private final MediatorLiveData<ExerciseSetRepRelation> observableSetReps;

    AddExerciseEntryViewModel(@NonNull JournalApplication application, String exerciseId, Long timestamp) {
        super(application);
        repository = application.getRepository();
        this.exerciseId = exerciseId;
        startEndTime = DateHelper.getStartAndEndTimestamp(timestamp);
        new ExerciseLiftTask().execute();
        observableSetReps = new MediatorLiveData<>();
        observableSetReps.addSource(
                repository.loadExerciseSetReps(exerciseId, startEndTime[0], startEndTime[1]),
                new Observer<ExerciseSetRepRelation>() {
                    @Override
                    public void onChanged(@Nullable ExerciseSetRepRelation exerciseSetRepRelation) {
                        observableSetReps.setValue(exerciseSetRepRelation);
                    }
                });
    }

    LiveData<ExerciseSetRepRelation> getObservableSetReps() {
        return observableSetReps;
    }

    private LiveData<ExerciseLiftEntity> getExerciseLiftEntity() {
        return exerciseLiftEntityMutableLiveData;
    }

    private LiveData<Boolean> getDataLoaded() {
        return dataLoaded;
    }

    void saveSet(String weight, String reps) throws Exception {
        double oneRepMax = EquationsHelper.getOneRepMax(weight, reps);
        if (getExerciseLiftEntity().getValue() != null && getDataLoaded().getValue() != null) {
            ExerciseLiftEntity exercise = getExerciseLiftEntity().getValue();
            JournalRepEntity journalRepEntity = new JournalRepEntity();
            journalRepEntity.setId(UUID.randomUUID().toString());
            journalRepEntity.setTimestamp(startEndTime[0]);
            journalRepEntity.setPosition(repPosition());
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

    void deleteRep(JournalRepEntity repEntity) {
        repository.deleteReps(repEntity);
    }

    void deleteRepAndFilter(JournalRepEntity repEntity, List<JournalRepEntity> repEntityList) {
        for (int i = 0; i < repEntityList.size(); i++) {
            repEntityList.get(i).setPosition(i + 1);
        }
        repository.deleteRepAndUpdate(repEntity, repEntityList);
    }

    @SuppressWarnings("ConstantConditions")
    private void createOrm(double orm, String repId) {
        ExerciseOrmEntity newOrmEntity = new ExerciseOrmEntity();
        newOrmEntity.setId(UUID.randomUUID().toString());
        newOrmEntity.setName(exerciseLiftEntityMutableLiveData.getValue().getName());
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

    private void setDataLoaded() {
        showRepEntry.set(true);
        dataLoaded.setValue(true);
    }

    private int repPosition() {
        int pos = 0;
        if (observableSetReps.getValue() != null) {
            if (observableSetReps.getValue().getJournalRepEntityList() != null) {
                pos = observableSetReps.getValue().getJournalRepEntityList().size();
            }
        }
        return pos + 1;
    }

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
            exerciseLiftEntityMutableLiveData.setValue(exerciseLiftEntity);
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
                setDataLoaded();
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
            setDataLoaded();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.e("Testing", "Cleared");
    }
}
