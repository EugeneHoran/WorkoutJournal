package eh.workout.journal.com.workoutjournal.ui.add.exercise;


import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import eh.workout.journal.com.workoutjournal.JournalApplication;

public class AddExerciseEntryVMFactory extends ViewModelProvider.NewInstanceFactory {
    @NonNull
    private final Application application;
    private final String liftId;
    private final Long timestamp;

    AddExerciseEntryVMFactory(@NonNull Application application, String liftId, Long timestamp) {
        this.application = application;
        this.liftId = liftId;
        this.timestamp = timestamp;
    }

    @NonNull
    @Override
    @SuppressWarnings("all")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddExerciseEntryViewModel((JournalApplication) application, liftId, timestamp);
    }
}