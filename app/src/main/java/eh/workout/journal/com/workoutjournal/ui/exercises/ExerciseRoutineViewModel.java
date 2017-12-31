package eh.workout.journal.com.workoutjournal.ui.exercises;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.relations.RoutineSetRelation;


public class ExerciseRoutineViewModel extends AndroidViewModel {
    private JournalRepository repository;
    private MutableLiveData<List<RoutineSetRelation>> routineSetRelationList;
    private Long timestamp;

    public ExerciseRoutineViewModel(@NonNull Application application, Long timestamp) {
        super(application);
        this.timestamp = timestamp;
        JournalApplication journalApplication = getApplication();
        repository = journalApplication.getRepository();
        routineSetRelationList = new MutableLiveData<>();
        new TaskRoutine().execute();
    }

    void resetRoutine() {
        new TaskRoutine().execute();
    }

    MutableLiveData<List<RoutineSetRelation>> getRoutineSetRelationList() {
        return routineSetRelationList;
    }


    @SuppressLint("StaticFieldLeak")
    class TaskRoutine extends AsyncTask<Void, Void, List<RoutineSetRelation>> {
        @Override
        protected List<RoutineSetRelation> doInBackground(Void... voids) {
            return repository.getAllRoutinesWithSets();
        }

        @Override
        protected void onPostExecute(List<RoutineSetRelation> routineSetRelations) {
            super.onPostExecute(routineSetRelations);
            getRoutineSetRelationList().setValue(routineSetRelations);
        }
    }
}
