package eh.workout.journal.com.workoutjournal;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

import eh.workout.journal.com.workoutjournal.db.JournalDatabase;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.AppExecutors;
import eh.workout.journal.com.workoutjournal.util.Constants;


public class JournalApplication extends Application {
    private AppExecutors appExecutors;
    private SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        initSettings();
        appExecutors = new AppExecutors();
    }

    public JournalDatabase getDatabase() {
        return JournalDatabase.getInstance(this, appExecutors);
    }

    public JournalRepository getRepository() {
        return JournalRepository.getInstance(appExecutors, getDatabase());
    }

    public void initSettings() {
        if (sp == null) {
            sp = PreferenceManager.getDefaultSharedPreferences(this);
        }
        Constants.SETTINGS_UNIT_MEASURE = sp.getString(Constants.KEY_UNIT_MEASURES, null);
        Constants.SETTINGS_SHOW_TIMER = sp.getBoolean(Constants.KEY_ENABLED_TIMER, false);
        Constants.SETTINGS_SHOW_ROUTINE_PLAN = sp.getBoolean(Constants.KEY_ROUTINE_PLAN, true);
        Constants.SETTINGS_SHOW_SUGGESTIONS = sp.getBoolean(Constants.KEY_SHOW_SUGGESTIONS, true);
    }
}
