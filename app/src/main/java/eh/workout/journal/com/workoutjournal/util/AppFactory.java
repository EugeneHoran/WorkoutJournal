package eh.workout.journal.com.workoutjournal.util;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.ui.entry.EntryHistoryViewModel;
import eh.workout.journal.com.workoutjournal.ui.entry.EntryViewModel;
import eh.workout.journal.com.workoutjournal.ui.exercises.ExerciseRoutineViewModel;
import eh.workout.journal.com.workoutjournal.ui.plan.edit.PlanDayEditViewModel;
import eh.workout.journal.com.workoutjournal.ui.routine_new.edit.EditRoutineViewModel;


public class AppFactory extends ViewModelProvider.NewInstanceFactory {
    @NonNull
    private final Application application;
    private final String liftId;
    private final Long timestamp;
    private final String planId;

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
        if (modelClass.isAssignableFrom(ExerciseRoutineViewModel.class)) {
            return (T) new ExerciseRoutineViewModel(application, timestamp);
        } else if (modelClass.isAssignableFrom(EditRoutineViewModel.class)) {
            return (T) new EditRoutineViewModel((JournalApplication) application, planId);
        } else if (modelClass.isAssignableFrom(PlanDayEditViewModel.class)) {
            return (T) new PlanDayEditViewModel(application, planId);
        } else if (modelClass.isAssignableFrom(EntryViewModel.class)) {
            return (T) new EntryViewModel((JournalApplication) application, liftId, timestamp);
        } else if (modelClass.isAssignableFrom(EntryHistoryViewModel.class)) {
            return (T) new EntryHistoryViewModel((JournalApplication) application, liftId, timestamp);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}