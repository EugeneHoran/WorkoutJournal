package eh.workout.journal.com.workoutjournal.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.transition.Explode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.ActivityMainBinding;
import eh.workout.journal.com.workoutjournal.ui.entry.EntryParentFragment;
import eh.workout.journal.com.workoutjournal.ui.exercises.ExerciseParentFragment;
import eh.workout.journal.com.workoutjournal.ui.journal.JournalParentFragment;
import eh.workout.journal.com.workoutjournal.util.AnimationTransition;
import eh.workout.journal.com.workoutjournal.util.Constants;

public class MainActivity extends AppCompatActivity {
    public static final String TAG_FRAG_JOURNAL = "tag_frag_journal";
    public static final String TAG_FRAG_EXERCISE_SELECTOR = "tag_frag_exercise_selector";
    public static final String TAG_FRAG_ADD_EXERCISE = "tag_frag_add_exercise";
    public static final String TAG_FRAG_ORM = "tag_frag_one_rep_max";
    private SharedPreferences sp;
    private FragmentManager fm;

    public ObservableBoolean showTimer = new ObservableBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        Constants.SETTINGS_UNIT_MEASURE = sp.getString(Constants.KEY_UNIT_MEASURES, null);
        Constants.SETTINGS_SHOW_ROUTINE_PLAN = sp.getBoolean(Constants.KEY_ROUTINE_PLAN, true);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);
        fm = getSupportFragmentManager();
        if (savedInstanceState == null) {
            initJournalFragment(Constants.JOURNAL_PAGE_TODAY, false);
        }
        handleTimer();
    }

    private void initJournalFragment(int page, boolean showPlanRoutine) {
        if (fm.findFragmentByTag(TAG_FRAG_JOURNAL) != null) {
            ((JournalParentFragment) fm.findFragmentByTag(TAG_FRAG_JOURNAL)).initJournalData();
            return;
        }
        JournalParentFragment journalParentFragment = JournalParentFragment.newInstance(page, false);
        initTransitionN(journalParentFragment);
        fm.beginTransaction()
                .replace(R.id.container, journalParentFragment, TAG_FRAG_JOURNAL)
                .commit();
    }

    private void resetJournal(int page) {
        JournalParentFragment journalParentFragment = JournalParentFragment.newInstance(page, false);
        initTransitionN(journalParentFragment);
        fm.beginTransaction()
                .replace(R.id.container, journalParentFragment, TAG_FRAG_JOURNAL)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_SETTINGS) {
            Constants.SETTINGS_SHOW_ROUTINE_PLAN = sp.getBoolean(Constants.KEY_ROUTINE_PLAN, true);
            resetJournal(resultCode);
            handleTimer();
        } else if (requestCode == Constants.ADD_EDIT_PLAN_JOURNAL) {
            if (data != null) {
                if (data.getExtras() != null) {
                    int page = data.getExtras().getInt(Constants.JOURNAL_PAGE_RESULT_CODE_PLAN, Constants.JOURNAL_PAGE_TODAY);
                    fm.popBackStack();
                    initJournalFragment(page, resultCode == RESULT_OK);
                }
            }
        } else if (requestCode == Constants.ADD_EDIT_PLAN_EXERCISE) {
            ExerciseParentFragment exerciseParentFragment = (ExerciseParentFragment) fm.findFragmentByTag(TAG_FRAG_EXERCISE_SELECTOR);
            if (exerciseParentFragment != null) {
                int page = exerciseParentFragment.page;
                fm.popBackStack();
                initJournalFragment(page, resultCode == RESULT_OK);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (fm.findFragmentById(R.id.container) instanceof ExerciseParentFragment) {
            if (getExerciseFragment() != null) {
                if (getExerciseFragment().searchVisible()) {
                    getExerciseFragment().hideSearch();
                    return;
                }
            }
        }
        if (fm.findFragmentById(R.id.container) instanceof EntryParentFragment) {
            if (fm.getBackStackEntryCount() == 2 && getExerciseFragment() != null) {
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                return;
            }
        }
        super.onBackPressed();
    }

    private void handleTimer() {
        showTimer.set(sp.getBoolean(Constants.KEY_ENABLED_TIMER, false));
    }

    private ExerciseParentFragment getExerciseFragment() {
        return fm.findFragmentByTag(TAG_FRAG_EXERCISE_SELECTOR) != null ?
                (ExerciseParentFragment) fm.findFragmentByTag(TAG_FRAG_EXERCISE_SELECTOR) :
                null;
    }

    private void initTransitionN(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementReturnTransition(new AnimationTransition());
            fragment.setExitTransition(new Explode());
        }
    }
}
