package eh.workout.journal.com.workoutjournal.ui.exercises_new;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.entinty.Exercise;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseCategoryRelation;

public class ExerciseViewModel extends AndroidViewModel {
    private JournalRepository repository;
    private final MediatorLiveData<List<Object>> mediatorExerciseCategoryRelation = new MediatorLiveData<>();
    private MutableLiveData<List<Exercise>> exerciseListLive;

    public ExerciseViewModel(@NonNull Application application) {
        super(application);
        repository = ((JournalApplication) application).getRepository();

    }

    public void initData() {
        final LiveData<List<ExerciseCategoryRelation>> getExerciseCategoryRelations = repository.getExerciseCategoryRelations();
        mediatorExerciseCategoryRelation.addSource(getExerciseCategoryRelations, new Observer<List<ExerciseCategoryRelation>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseCategoryRelation> exerciseCategoryRelations) {
                if (exerciseCategoryRelations != null) {
                    List<Exercise> exerciseList = new ArrayList<>();
                    for (ExerciseCategoryRelation exerciseCategoryRelation : exerciseCategoryRelations) {
                        exerciseList.addAll(exerciseCategoryRelation.getExerciseList());
                    }
                    Collections.sort(exerciseList);
                    observeExerciseList().setValue(exerciseList);
                    mediatorExerciseCategoryRelation.setValue(null);
                }
            }
        });
    }

    public LiveData<List<Object>> observeRelationList() {
        return mediatorExerciseCategoryRelation;
    }

    public MutableLiveData<List<Exercise>> observeExerciseList() {
        if (exerciseListLive == null) {
            exerciseListLive = new MutableLiveData<>();
        }
        return exerciseListLive;
    }
}
