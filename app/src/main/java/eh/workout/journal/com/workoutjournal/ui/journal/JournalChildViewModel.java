package eh.workout.journal.com.workoutjournal.ui.journal;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;
import eh.workout.journal.com.workoutjournal.util.DateHelper;


public class JournalChildViewModel extends AndroidViewModel {
    private LiveData<List<ExerciseSetRepRelation>> observeSetAndReps;
    private JournalRepository repository;

    public JournalChildViewModel(@NonNull Application application, Long timestamp) {
        super(application);
        JournalApplication journalApplication = (JournalApplication) application;
        repository = journalApplication.getRepository();
        observeSetAndReps = repository.getExerciseSetRepRelationLive(DateHelper.getStartAndEndTimestamp(timestamp));
    }

    LiveData<List<ExerciseSetRepRelation>> getSetAndReps() {
        return observeSetAndReps;
    }

    void deleteSet(JournalSetEntity setEntity) {
        repository.deleteSet(setEntity);
    }
}
