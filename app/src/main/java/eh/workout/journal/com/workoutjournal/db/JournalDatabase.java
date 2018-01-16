package eh.workout.journal.com.workoutjournal.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import eh.workout.journal.com.workoutjournal.BuildConfig;
import eh.workout.journal.com.workoutjournal.db.dao.ExerciseDao;
import eh.workout.journal.com.workoutjournal.db.dao.ExerciseLiftDao;
import eh.workout.journal.com.workoutjournal.db.dao.JournalDao;
import eh.workout.journal.com.workoutjournal.db.dao.PlanDao;
import eh.workout.journal.com.workoutjournal.db.dao.RoutineDao;
import eh.workout.journal.com.workoutjournal.db.entinty.Exercise;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseCategory;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseGroupEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseOrmEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalDateEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanDayEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanDaySetEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanSetEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.RoutineEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.RoutineSetEntity;
import eh.workout.journal.com.workoutjournal.util.DataHelper;
import eh.workout.journal.com.workoutjournal.util.loaders.ExerciseLoaders;


@Database(entities = {
        JournalDateEntity.class,
        JournalSetEntity.class,
        JournalRepEntity.class,
        ExerciseGroupEntity.class,
        ExerciseLiftEntity.class,
        ExerciseOrmEntity.class,
        RoutineEntity.class,
        RoutineSetEntity.class,
        PlanEntity.class,
        PlanSetEntity.class,
        PlanDayEntity.class,
        PlanDaySetEntity.class,
        ExerciseCategory.class,
        Exercise.class},
        version = 1)
@TypeConverters({JournalTypeConverter.class})
public abstract class JournalDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = BuildConfig.DB_NAME;
    private static JournalDatabase instance;

    public abstract JournalDao getJournalDao();

    public abstract ExerciseDao getExerciseDao();

    public abstract ExerciseLiftDao getExerciseLiftDao();

    public abstract RoutineDao getRoutineDao();

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
                final JournalDatabase database = JournalDatabase.getInstance(context, executors);
                executors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<ExerciseLiftEntity> liftEntities = ExerciseLoaders.get().getExerciseLifts(context);
                        database.getExerciseLiftDao().insertGroupAndExercises(new DataHelper().generateExerciseGroups(), liftEntities);
                    }
                });
            }
        }).build();
    }
}
