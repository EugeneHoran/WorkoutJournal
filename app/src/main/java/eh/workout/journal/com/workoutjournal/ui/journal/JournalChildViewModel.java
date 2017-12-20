package eh.workout.journal.com.workoutjournal.ui.journal;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.databinding.ObservableField;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;
import eh.workout.journal.com.workoutjournal.db.relations.PlanSetRelation;
import eh.workout.journal.com.workoutjournal.util.DateHelper;


public class JournalChildViewModel extends AndroidViewModel {
    public ObservableField<Boolean> showWorkoutPlan = new ObservableField<>(false);
    public ObservableField<Boolean> expandWorkoutPlan = new ObservableField<>(false);

    private JournalRepository repository;
    private MediatorLiveData<List<ExerciseSetRepRelation>> observeSetAndReps;
    private MutableLiveData<List<PlanSetRelation>> observePlans;


    boolean deleteSet = true;

    public JournalChildViewModel(@NonNull Application application, final Long timestamp) {
        super(application);
        JournalApplication journalApplication = (JournalApplication) application;
        repository = journalApplication.getRepository();
        observeSetAndReps = new MediatorLiveData<>();
        observeSetAndReps.addSource(repository.getExerciseSetRepRelationLive(DateHelper.getStartAndEndTimestamp(timestamp)), new Observer<List<ExerciseSetRepRelation>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseSetRepRelation> exerciseSetRepRelations) {
                observeSetAndReps.setValue(exerciseSetRepRelations);
                new PlanTask().execute(DateHelper.getDayOfWeek(timestamp));
            }
        });
    }

    LiveData<List<ExerciseSetRepRelation>> getSetAndReps() {
        return observeSetAndReps;
    }

    MutableLiveData<List<PlanSetRelation>> getPlans() {
        if (observePlans == null) {
            observePlans = new MutableLiveData<>();
        }
        return observePlans;
    }

    void deleteSet(final JournalSetEntity setEntity) {
        new CountDownTimer(2500, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                if (deleteSet) {
                    repository.deleteSet(setEntity);
                }
                deleteSet = true;
            }
        }.start();
    }

    @SuppressLint("StaticFieldLeak")
    class PlanTask extends AsyncTask<Integer, Void, List<PlanSetRelation>> {
        @Override
        protected List<PlanSetRelation> doInBackground(Integer... integers) {
            List<PlanSetRelation> planSetRelations = repository.getPlanSetRelationList(integers[0]);
            if (getSetAndReps().getValue() != null) {
                if (getSetAndReps().getValue().size() > 0 && planSetRelations != null) {
                    for (int i = 0; i < planSetRelations.size(); i++) {
                        for (int j = 0; j < planSetRelations.get(i).getPlanSetEntityList().size(); j++) {
                            for (ExerciseSetRepRelation exerciseSetRepRelation : getSetAndReps().getValue()) {
                                if (planSetRelations.get(i).getPlanSetEntityList().get(j).getExerciseId().equalsIgnoreCase(exerciseSetRepRelation.getJournalSetEntity().getExerciseId())) {
                                    planSetRelations.get(i).getPlanSetEntityList().get(j).setSetCompleted(true);
                                }
                            }

                        }
                    }
                }
            }
            return planSetRelations;
        }

        @Override
        protected void onPostExecute(List<PlanSetRelation> planSetRelations) {
            super.onPostExecute(planSetRelations);
            if (planSetRelations != null) {
                showWorkoutPlan.set(planSetRelations.size() > 0);
            } else {
                showWorkoutPlan.set(false);
            }
            getPlans().setValue(planSetRelations);
        }
    }
}
