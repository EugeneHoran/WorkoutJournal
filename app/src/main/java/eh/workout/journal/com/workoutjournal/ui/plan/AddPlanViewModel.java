package eh.workout.journal.com.workoutjournal.ui.plan;

import android.annotation.SuppressLint;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;

public class AddPlanViewModel extends AndroidViewModel {
    private JournalRepository repository;
    private List<ExerciseLiftEntity> liftEntities = new ArrayList<>();
    private MutableLiveData<List<ExerciseLiftEntity>> listLiveData;

    public AddPlanViewModel(@NonNull JournalApplication application) {
        super(application);
        repository = application.getRepository();
    }

    void initLifts() {
        new TaskLifts().execute();
    }

    LiveData<List<ExerciseLiftEntity>> getLiftsLiveData() {
        if (listLiveData == null) {
            listLiveData = new MutableLiveData<>();
        }
        return listLiveData;
    }

    private void dataLoaded() {
        listLiveData.setValue(liftEntities);
    }

    @SuppressLint("StaticFieldLeak")
    class TaskLifts extends AsyncTask<Void, Void, List<ExerciseLiftEntity>> {
        @Override
        protected List<ExerciseLiftEntity> doInBackground(Void... voids) {
            return repository.getAllExercisesList();
        }

        @Override
        protected void onPostExecute(List<ExerciseLiftEntity> exerciseLiftEntities) {
            super.onPostExecute(exerciseLiftEntities);
            liftEntities.addAll(exerciseLiftEntities);
            dataLoaded();
        }
    }
}
