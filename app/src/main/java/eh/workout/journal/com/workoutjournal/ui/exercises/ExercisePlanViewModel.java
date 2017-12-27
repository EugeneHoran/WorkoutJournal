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
import eh.workout.journal.com.workoutjournal.db.entinty.PlanEntity;
import eh.workout.journal.com.workoutjournal.db.relations.PlanSetRelation;


public class ExercisePlanViewModel extends AndroidViewModel {
    private JournalRepository repository;
    private MutableLiveData<List<PlanSetRelation>> planSetRelationList;
    private Long timestamp;

    public ExercisePlanViewModel(@NonNull Application application, Long timestamp) {
        super(application);
        this.timestamp = timestamp;
        JournalApplication journalApplication = getApplication();
        repository = journalApplication.getRepository();
        planSetRelationList = new MutableLiveData<>();
        new TaskPlans().execute();
    }

    public void resetPlan() {
        new TaskPlans().execute();
    }

    MutableLiveData<List<PlanSetRelation>> getPlanSetRelationList() {
        return planSetRelationList;
    }


    @SuppressLint("StaticFieldLeak")
    class TaskPlans extends AsyncTask<Void, Void, List<PlanSetRelation>> {
        @Override
        protected List<PlanSetRelation> doInBackground(Void... voids) {
            List<PlanSetRelation> finalList = new ArrayList<>();
            List<PlanSetRelation> planSetRelationsParent = new ArrayList<>();
            List<PlanSetRelation> allPlanSetRelations = repository.getAllPlansWithSets();
            for (int i = 0; i < allPlanSetRelations.size(); i++) {
                PlanEntity planSetEntity = allPlanSetRelations.get(i).getPlanEntity();
                if (planSetEntity.getParentId() == null) {
                    planSetRelationsParent.add(allPlanSetRelations.get(i));
                }
            }
            finalList.addAll(planSetRelationsParent);
            return finalList;
        }

        @Override
        protected void onPostExecute(List<PlanSetRelation> planSetRelations) {
            super.onPostExecute(planSetRelations);
            getPlanSetRelationList().setValue(planSetRelations);
        }
    }
}
