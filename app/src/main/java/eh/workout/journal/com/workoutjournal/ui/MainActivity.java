package eh.workout.journal.com.workoutjournal.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.ui.exercises.ExerciseParentFragment;
import eh.workout.journal.com.workoutjournal.ui.journal.JournalParentFragment;
import eh.workout.journal.com.workoutjournal.util.Constants;

public class MainActivity extends AppCompatActivity {
    public static final String TAG_FRAG_JOURNAL = "tag_frag_journal";
    public static final String TAG_FRAG_EXERCISE_SELECTOR = "tag_frag_exercise_selector";
    public static final String TAG_FRAG_ADD_EXERCISE = "tag_frag_add_exercise";
    public static final String TAG_FRAG_ORM = "tag_frag_one_rep_max";
    private SharedPreferences sp;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        Constants.UNIT = sp.getString(Constants.KEY_UNIT_MEASURES, null);
        DataBindingUtil.setContentView(this, R.layout.activity_main);
        fm = getSupportFragmentManager();
        if (savedInstanceState == null) {
            initJournalFragment(Constants.JOURNAL_PAGE_TODAY);
        }
        handleTimer();
    }

    private void initJournalFragment(int page) {
        fm.beginTransaction()
                .replace(R.id.container, JournalParentFragment.newInstance(page), TAG_FRAG_JOURNAL)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_SETTINGS) {
            initJournalFragment(resultCode);
            handleTimer();
        }
    }

//    @Override
//    public void onBackPressed() {
//        if (getExerciseFragment() != null) {
//            if (getExerciseFragment().searchVisible()) {
//                getExerciseFragment().hideSearch();
//            } else {
//                super.onBackPressed();
//            }
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public void onBackPressed() {
        if (getExerciseFragment() != null) {
            if (getExerciseFragment().searchVisible()) {
                getExerciseFragment().hideSearch();
                return;
            }
        }
        super.onBackPressed();
    }

    private void handleTimer() {
        findViewById(R.id.containerTimer).setVisibility(sp.getBoolean("example_switch", false) ? View.VISIBLE : View.GONE);
    }

    private ExerciseParentFragment getExerciseFragment() {
        return fm.findFragmentByTag(TAG_FRAG_EXERCISE_SELECTOR) != null ?
                (ExerciseParentFragment) fm.findFragmentByTag(TAG_FRAG_EXERCISE_SELECTOR) :
                null;
    }
}
