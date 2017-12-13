package eh.workout.journal.com.workoutjournal.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.base.BaseActivity;
import eh.workout.journal.com.workoutjournal.ui.exercises.ExerciseSelectorFragment;
import eh.workout.journal.com.workoutjournal.ui.journal.JournalParentFragment;
import eh.workout.journal.com.workoutjournal.util.Constants;

public class MainActivity extends BaseActivity {
    public static final String TAG_FRAG_JOURNAL = "tag_frag_journal";
    public static final String TAG_FRAG_EXERCISE_SELECTOR = "tag_frag_exercise_selector";
    public static final String TAG_FRAG_ADD_EXERCISE = "tag_frag_add_exercise";
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        Constants.UNIT = PreferenceManager
                .getDefaultSharedPreferences(this)
                .getString("key_unit_measure", null);
        DataBindingUtil.setContentView(this, R.layout.activity_main);
        fm = getSupportFragmentManager();
        if (savedInstanceState == null) {
            fm.beginTransaction()
                    .replace(R.id.container, JournalParentFragment.newInstance(), TAG_FRAG_JOURNAL)
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_SETTINGS) {
            refreshJournalAfterSettings(resultCode);
        }
    }

    public void refreshJournalAfterSettings(int page) {
        fm.beginTransaction()
                .replace(R.id.container, JournalParentFragment.newInstance(page), TAG_FRAG_JOURNAL)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getExerciseFragment() != null) {
            if (getExerciseFragment().searchVisible()) {
                getExerciseFragment().hideSearch();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    private ExerciseSelectorFragment getExerciseFragment() {
        return fm.findFragmentByTag(TAG_FRAG_EXERCISE_SELECTOR) != null ?
                (ExerciseSelectorFragment) fm.findFragmentByTag(TAG_FRAG_EXERCISE_SELECTOR) :
                null;
    }
}
