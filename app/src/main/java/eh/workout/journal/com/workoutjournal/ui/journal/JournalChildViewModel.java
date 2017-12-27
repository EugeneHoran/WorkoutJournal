package eh.workout.journal.com.workoutjournal.ui.journal;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
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
    private JournalRepository repository;
    private MediatorLiveData<List<ExerciseSetRepRelation>> observeSetAndReps;
    private MutableLiveData<List<PlanSetRelation>> observePlans;
    private Long timestamp;

    public JournalChildViewModel(@NonNull Application application, final Long timestamp) {
        super(application);
        this.timestamp = timestamp;
        JournalApplication journalApplication = (JournalApplication) application;
        repository = journalApplication.getRepository();
        observeSetAndReps = new MediatorLiveData<>();
        observeSetAndReps.addSource(repository.getExerciseSetRepRelationLive(DateHelper.getStartAndEndTimestamp(timestamp)), new Observer<List<ExerciseSetRepRelation>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseSetRepRelation> exerciseSetRepRelations) {
                if (deletingSet == null) {
                    observeSetAndReps.setValue(exerciseSetRepRelations);
                }
                new PlanTask().execute(timestamp);
            }
        });
    }

    public void resetTasks() {
        new PlanTask().execute(timestamp);
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

    private JournalSetEntity deletingSet;


    void cancelDeleteSet() {
        deleteTimer.cancel();
        deletingSet = null;
    }

    void deleteSet(final JournalSetEntity setEntity) {
        deleteTimer.cancel();
        if (deletingSet != null) {
            repository.deleteSet(deletingSet);
            deletingSet = null;
        }
        deletingSet = setEntity;
        deleteTimer.start();
    }


    private CountDownTimer deleteTimer = new CountDownTimer(2500, 1000) {
        @Override
        public void onTick(long l) {
        }

        @Override
        public void onFinish() {
            repository.deleteSet(deletingSet);
            deletingSet = null;
        }
    };

    @SuppressLint("StaticFieldLeak")
    class PlanTask extends AsyncTask<Long, Void, List<PlanSetRelation>> {
        @Override
        protected List<PlanSetRelation> doInBackground(Long... integers) {
            List<PlanSetRelation> planSetRelations = repository.getPlanSetRelationList(DateHelper.getDayOfWeek(integers[0]));
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
            getPlans().setValue(planSetRelations);
        }
    }


}
