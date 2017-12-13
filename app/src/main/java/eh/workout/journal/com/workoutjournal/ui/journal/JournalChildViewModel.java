package eh.workout.journal.com.workoutjournal.ui.journal;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.List;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.relations.DateSetRepRelation;
import eh.workout.journal.com.workoutjournal.util.DateHelper;


class JournalChildViewModel extends AndroidViewModel {
    private LiveData<List<DateSetRepRelation>> observeSetAndReps;
    private JournalRepository repository;

    JournalChildViewModel(@NonNull Application application, Long timestamp) {
        super(application);
        JournalApplication journalApplication = (JournalApplication) application;
        repository = journalApplication.getRepository();
        observeSetAndReps = repository.loadSetAndRepsByDate(DateHelper.getStartAndEndTimestamp(timestamp));
    }


    LiveData<List<DateSetRepRelation>> observeDatesAndTimes() {
        return observeSetAndReps;
    }

    static class JournalViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application application;
        private final Long timestamp;

        JournalViewModelFactory(@NonNull Application application, Long timestamp) {
            this.application = application;
            this.timestamp = timestamp;
        }

        @NonNull
        @Override
        @SuppressWarnings("all")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new JournalChildViewModel(application, timestamp);
        }
    }
}
