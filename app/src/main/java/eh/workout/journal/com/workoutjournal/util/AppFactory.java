package eh.workout.journal.com.workoutjournal.util;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.ui.entry.EntryHistoryViewModel;
import eh.workout.journal.com.workoutjournal.ui.entry.EntryViewModelNew;
import eh.workout.journal.com.workoutjournal.ui.exercises.ExercisePlanViewModel;
import eh.workout.journal.com.workoutjournal.ui.journal.JournalChildViewModel;
import eh.workout.journal.com.workoutjournal.ui.plan.AddPlanViewModel;
import eh.workout.journal.com.workoutjournal.ui.plan.edit.EditPlanViewModel;


public class AppFactory extends ViewModelProvider.NewInstanceFactory {
    @NonNull
    private final Application application;
    private final String liftId;
    private final Long timestamp;
    private final String planId;

    public AppFactory(@NonNull Application application) {
        this.application = application;
        this.timestamp = null;
        this.liftId = null;
        this.planId = null;
    }

    public AppFactory(@NonNull Application application, Long timestamp) {
        this.application = application;
        this.timestamp = timestamp;
        this.liftId = null;
        this.planId = null;
    }

    public AppFactory(@NonNull Application application, String liftId, Long timestamp) {
        this.application = application;
        this.liftId = liftId;
        this.timestamp = timestamp;
        this.planId = null;
    }

    public AppFactory(@NonNull Application application, String planId) {
        this.application = application;
        this.timestamp = null;
        this.liftId = null;
        this.planId = planId;
    }

    @NonNull
    @Override
    @SuppressWarnings("all")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ExercisePlanViewModel.class)) {
            return (T) new ExercisePlanViewModel(application, timestamp);
        } else if (modelClass.isAssignableFrom(JournalChildViewModel.class)) {
            return (T) new JournalChildViewModel(application, timestamp);
        } else if (modelClass.isAssignableFrom(EditPlanViewModel.class)) {
            return (T) new EditPlanViewModel((JournalApplication) application, planId);
        } else if (modelClass.isAssignableFrom(EntryViewModelNew.class)) {
            return (T) new EntryViewModelNew((JournalApplication) application, liftId, timestamp);
        } else if (modelClass.isAssignableFrom(EntryHistoryViewModel.class)) {
            return (T) new EntryHistoryViewModel((JournalApplication) application, liftId, timestamp);
        } else if (modelClass.isAssignableFrom(AddPlanViewModel.class)) {
            return (T) new AddPlanViewModel((JournalApplication) application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}