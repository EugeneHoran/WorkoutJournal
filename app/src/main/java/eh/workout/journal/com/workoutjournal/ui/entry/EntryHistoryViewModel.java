package eh.workout.journal.com.workoutjournal.ui.entry;


import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;
import eh.workout.journal.com.workoutjournal.util.DateHelper;

public class EntryHistoryViewModel extends AndroidViewModel {
    private LiveData<List<ExerciseSetRepRelation>> observeHistory;

    public EntryHistoryViewModel(@NonNull JournalApplication application, String exerciseId, Long timestamp) {
        super(application);
        JournalRepository repository = application.getRepository();
        Long[] startEndTime = DateHelper.getStartAndEndTimestamp(timestamp);
        observeHistory = repository.getExerciseSetRepRelationHistoryLive(exerciseId, startEndTime[0]);
    }

    LiveData<List<ExerciseSetRepRelation>> getHistory() {
        return observeHistory;
    }
}
