package eh.workout.journal.com.workoutjournal.ui.exercises;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;
import eh.workout.journal.com.workoutjournal.util.DataGenerator;

public class ExerciseSelectorViewModel extends AndroidViewModel {
    private JournalRepository repository;
    private LiveData<List<ExerciseLiftEntity>> allExercises;
    private final MediatorLiveData<List<Object>> observeExercisesObjects;

    public ExerciseSelectorViewModel(@NonNull Application application) {
        super(application);
        JournalApplication journalApplication = getApplication();
        repository = journalApplication.getRepository();
        allExercises = repository.getAllExercises();
        observeExercisesObjects = new MediatorLiveData<>();
        observeExercisesObjects.addSource(allExercises, new Observer<List<ExerciseLiftEntity>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseLiftEntity> exerciseLiftEntities) {
                observeExercisesObjects.setValue(DataGenerator.getFormattedExerciseList(exerciseLiftEntities));
            }
        });
    }

    void queryExercises(String query) {
        if (TextUtils.isEmpty(query)) {
            observeExercisesObjects.setValue(DataGenerator.getFormattedExerciseList(allExercises.getValue()));
        } else {
            List<ExerciseLiftEntity> exerciseLiftEntities = allExercises.getValue();
            if (exerciseLiftEntities != null) {
                List<Object> temp = new ArrayList<>();
                for (Object d : exerciseLiftEntities) {
                    if (d instanceof ExerciseLiftEntity) {
                        ExerciseLiftEntity lift = (ExerciseLiftEntity) d;
                        if (lift.getName().toLowerCase().contains(query.toLowerCase())) {
                            temp.add(d);
                        }
                    }
                }
                observeExercisesObjects.setValue(temp);
            } else {
                observeExercisesObjects.setValue(DataGenerator.getFormattedExerciseList(allExercises.getValue()));
            }
        }
    }

    LiveData<List<Object>> observeExerciseList() {
        return observeExercisesObjects;
    }

    void addExerciseToRecent(ExerciseLiftEntity exerciseLiftEntity) {
        if (exerciseLiftEntity.getIsShownInRecent()) {
            return;
        }
        exerciseLiftEntity.setRecent(true);
        repository.updateExercises(exerciseLiftEntity);
    }

    void removeExerciseFromRecent(ExerciseLiftEntity exerciseLiftEntity) {
        exerciseLiftEntity.setRecent(false);
        repository.updateExercises(exerciseLiftEntity);
    }

    void addExercise(ExerciseLiftEntity exerciseLiftEntity) {
        repository.insertExercises(exerciseLiftEntity);
    }
}
