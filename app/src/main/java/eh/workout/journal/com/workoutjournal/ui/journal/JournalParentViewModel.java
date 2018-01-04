package eh.workout.journal.com.workoutjournal.ui.journal;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanDayEntity;


public class JournalParentViewModel extends AndroidViewModel {
    private JournalRepository repository;

    public JournalParentViewModel(@NonNull Application application) {
        super(application);
        repository = ((JournalApplication) application).getRepository();
    }

    void deletePlanDayEntity(PlanDayEntity planDayEntity) {
        repository.deletePlanDayEntity(planDayEntity);
    }
}
