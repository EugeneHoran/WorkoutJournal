package eh.workout.journal.com.workoutjournal.db;


import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.os.Handler;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseOrmEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalDateEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.DateSetRepRelation;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;

public class JournalRepository {
    private static JournalRepository instance;
    private final JournalDatabase database;
    private final AppExecutors appExecutors;

    private JournalRepository(AppExecutors appExecutors, final JournalDatabase database) {
        this.appExecutors = appExecutors;
        this.database = database;
    }

    public static JournalRepository getInstance(final AppExecutors appExecutors, final JournalDatabase database) {
        if (instance == null) {
            synchronized (JournalRepository.class) {
                if (instance == null) {
                    instance = new JournalRepository(appExecutors, database);
                }
            }
        }
        return instance;
    }

    /**
     * One Rep Max
     */
    public void insertOrmEntity(final ExerciseOrmEntity... exerciseOrmEntities) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                database.getOrmDao().insertOrms(exerciseOrmEntities);
            }
        });
    }

    public ExerciseOrmEntity loadExerciseOrm(String exerciseId) {
        return database.getOrmDao().getSet(exerciseId);
    }

    public void updateExerciseOrm(final ExerciseOrmEntity exerciseOrmEntity) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                database.getOrmDao().updateOrms(exerciseOrmEntity);
            }
        });
    }

    /**
     * Async Imports
     */
    public void insertDateSetRep(final JournalDateEntity journalDateEntity, final JournalSetEntity journalSetEntity, final JournalRepEntity journalRepEntity) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                database.getJournalDateDao().insertDates(journalDateEntity);
            }
        });
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                database.getJournalSetDao().insertSets(journalSetEntity);
            }
        });
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                database.getJournalRepDao().insertReps(journalRepEntity);
            }
        });
    }

    public void insertSetRep(final JournalSetEntity journalSetEntity, final JournalRepEntity journalRepEntity) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                database.getJournalSetDao().insertSets(journalSetEntity);
            }
        });
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                database.getJournalRepDao().insertReps(journalRepEntity);
            }
        });
    }

    public void deleteRepAndUpdate(final JournalRepEntity journalSetEntity, final List<JournalRepEntity> repEntityList) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                database.getJournalRepDao().deleteReps(journalSetEntity);
            }
        });
        // TODO failed trying to create a single transaction with both. But delete has its own transaction
        // Delaying to help with view animations
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        database.getJournalRepDao().updateRepList(repEntityList);
                    }
                });
            }
        }, 300);
    }

    /**
     * Relations
     */
    public LiveData<List<DateSetRepRelation>> loadSetAndRepsByDate(Long... times) {
        return database.getRelationDao().getDateSetRepList(times[0], times[1]);
    }

    public LiveData<ExerciseSetRepRelation> loadExerciseSetReps(String exerciseId, Long... times) {
        return database.getRelationDao().getExerciseSetRep(exerciseId, times[0], times[1]);
    }

    /**
     * Exercises
     */
    public LiveData<List<ExerciseLiftEntity>> loadAllExercises() {
        return database.getExerciseLiftDao().loadAllExercises();
    }

    public void insertExercises(final ExerciseLiftEntity... exerciseLiftEntities) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getExerciseLiftDao().insertExercises(exerciseLiftEntities);
            }
        });
    }

    public void updateExercises(final ExerciseLiftEntity... exerciseLiftEntities) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getExerciseLiftDao().updateExercises(exerciseLiftEntities);
            }
        });
    }

    public ExerciseLiftEntity loadExerciseById(String id) {
        return database.getExerciseLiftDao().loadExercise(id);
    }

    /**
     * Journal Dates
     */
    public JournalDateEntity getDateRun(final long... times) {
        return database.getJournalDateDao().getDateRun(times[0], times[1]);
    }

    /**
     * Journal Sets
     */
    public void insertSets(final JournalSetEntity... setEntities) {
        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getJournalSetDao().insertSets(setEntities);
            }
        });
    }

    public void deleteSets(final JournalSetEntity... setEntities) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getJournalSetDao().deleteSets(setEntities);
            }
        });
    }

    public JournalSetEntity getSet(String exerciseId, String dateId) {
        return database.getJournalSetDao().getSet(exerciseId, dateId);
    }

    /**
     * Journal Reps
     */
    public void insertReps(final JournalRepEntity... repEntities) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getJournalRepDao().insertReps(repEntities);
            }
        });
    }

    public void deleteReps(final JournalRepEntity... repEntities) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getJournalRepDao().deleteReps(repEntities);
            }
        });
    }
}
