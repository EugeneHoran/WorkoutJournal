package eh.workout.journal.com.workoutjournal.ui.factory;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.ui.entry.EntryHistoryViewModel;
import eh.workout.journal.com.workoutjournal.ui.entry.EntryViewModelNew;
import eh.workout.journal.com.workoutjournal.ui.journal.JournalChildViewModel;


public class AppFactory extends ViewModelProvider.NewInstanceFactory {
    @NonNull
    private final Application application;
    private final String liftId;
    private final Long timestamp;

    public AppFactory(@NonNull Application application, Long timestamp) {
        this.application = application;
        this.timestamp = timestamp;
        liftId = null;
    }

    public AppFactory(@NonNull Application application, String liftId, Long timestamp) {
        this.application = application;
        this.liftId = liftId;
        this.timestamp = timestamp;
    }

    @NonNull
    @Override
    @SuppressWarnings("all")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(JournalChildViewModel.class)) {
            return (T) new JournalChildViewModel(application, timestamp);
        } else if (modelClass.isAssignableFrom(EntryViewModelNew.class)) {
            return (T) new EntryViewModelNew((JournalApplication) application, liftId, timestamp);
        } else if (modelClass.isAssignableFrom(EntryHistoryViewModel.class)) {
            return (T) new EntryHistoryViewModel((JournalApplication) application, liftId, timestamp);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}