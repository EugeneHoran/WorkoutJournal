package eh.workout.journal.com.workoutjournal.ui.exercises;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseGroupEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;


public class ExerciseGroupViewModel extends AndroidViewModel {
    private JournalRepository repository;
    private List<ExerciseGroupEntity> groupEntities;
    private List<ExerciseLiftEntity> liftEntities;
    private MutableLiveData<List<Object>> objectList;

    public ExerciseGroupViewModel(@NonNull Application application) {
        super(application);
        JournalApplication journalApplication = getApplication();
        repository = journalApplication.getRepository();
        new ExerciseGroupListTask().execute();
        new ExerciseLiftListTask().execute();
    }

    MutableLiveData<List<Object>> getGroupAndLiftObjectList() {
        if (objectList == null) {
            objectList = new MutableLiveData<>();
        }
        return objectList;
    }

    void groupItemClicked(ExerciseGroupEntity groupEntity) {
        List<Object> liftObjectList = new ArrayList<>();
        String headerText = groupEntity.getName();
        liftObjectList.add(headerText);
        for (int i = 0; i < liftEntities.size(); i++) {
            ExerciseLiftEntity lift = liftEntities.get(i);
            if (lift.getExerciseGroupId() == groupEntity.getId()) {
                liftObjectList.add(lift);
            }
        }
        objectList.setValue(liftObjectList);
    }

    void returnToGroupList() {
        objectList.setValue(new ArrayList<Object>(groupEntities));
    }

    @SuppressLint("StaticFieldLeak")
    class ExerciseGroupListTask extends AsyncTask<Void, Void, List<ExerciseGroupEntity>> {
        @Override
        protected List<ExerciseGroupEntity> doInBackground(Void... voids) {
            return repository.getAllExercisesGroupsList();
        }

        @Override
        protected void onPostExecute(List<ExerciseGroupEntity> exerciseGroupEntities) {
            super.onPostExecute(exerciseGroupEntities);
            groupEntities = exerciseGroupEntities;
            getGroupAndLiftObjectList().setValue(new ArrayList<Object>(groupEntities));
        }
    }

    @SuppressLint("StaticFieldLeak")
    class ExerciseLiftListTask extends AsyncTask<Void, Void, List<ExerciseLiftEntity>> {

        @Override
        protected List<ExerciseLiftEntity> doInBackground(Void... voids) {
            return repository.getAllExercisesList();
        }

        @Override
        protected void onPostExecute(List<ExerciseLiftEntity> exerciseLiftEntities) {
            super.onPostExecute(exerciseLiftEntities);
            liftEntities = exerciseLiftEntities;
        }
    }
}
