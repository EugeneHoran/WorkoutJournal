package eh.workout.journal.com.workoutjournal.ui.journal;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;


public class JournalChildViewModel extends AndroidViewModel {
    private JournalRepository repository;

    public JournalChildViewModel(@NonNull Application application) {
        super(application);
        JournalApplication journalApplication = (JournalApplication) application;
        repository = journalApplication.getRepository();
    }

    void deleteSet(final ExerciseSetRepRelation dateSetRepRelation) {
        repository.deleteSet(dateSetRepRelation.getJournalSetEntity());
    }

}
