package eh.workout.journal.com.workoutjournal.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import eh.workout.journal.com.workoutjournal.BuildConfig;
import eh.workout.journal.com.workoutjournal.db.dao.ExerciseLiftDao;
import eh.workout.journal.com.workoutjournal.db.dao.JournalDao;
import eh.workout.journal.com.workoutjournal.db.dao.PlanDao;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseGroupEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseOrmEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalDateEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanSetEntity;
import eh.workout.journal.com.workoutjournal.util.ExerciseDataHelper;


@Database(entities = {
        JournalDateEntity.class,
        JournalSetEntity.class,
        JournalRepEntity.class,
        ExerciseGroupEntity.class,
        ExerciseLiftEntity.class,
        ExerciseOrmEntity.class,
        PlanEntity.class,
        PlanSetEntity.class}, version = 1)
public abstract class JournalDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = BuildConfig.DB_NAME;
    private static JournalDatabase instance;

    public abstract JournalDao getJournalDao();

    public abstract ExerciseLiftDao getExerciseLiftDao();

    public abstract PlanDao getPlanDao();

    public static JournalDatabase getInstance(final Context context, final AppExecutors executors) {
        if (instance == null) {
            synchronized (JournalDatabase.class) {
                if (instance == null) {
                    instance = buildDatabase(context.getApplicationContext(), executors);
                }
            }
        }
        return instance;
    }

    private static JournalDatabase buildDatabase(final Context context, final AppExecutors executors) {
        return Room.databaseBuilder(context, JournalDatabase.class, DATABASE_NAME).addCallback(new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                executors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        JournalDatabase database = JournalDatabase.getInstance(context, executors);
                        database.getExerciseLiftDao().insertGroupAndExercises(ExerciseDataHelper.generateExerciseGroups(), ExerciseDataHelper.generateExerciseLifts());
                    }
                });
            }
        }).build();
    }
}
