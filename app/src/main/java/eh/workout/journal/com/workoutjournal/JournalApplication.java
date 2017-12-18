package eh.workout.journal.com.workoutjournal;

import android.app.Application;

import eh.workout.journal.com.workoutjournal.db.JournalDatabase;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.AppExecutors;


public class JournalApplication extends Application {
    private AppExecutors appExecutors;

    @Override
    public void onCreate() {
        super.onCreate();
        appExecutors = new AppExecutors();
    }

    public JournalDatabase getDatabase() {
        return JournalDatabase.getInstance(this, appExecutors);
    }

    public JournalRepository getRepository() {
        return JournalRepository.getInstance(appExecutors, getDatabase());
    }
}
