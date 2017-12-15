package eh.workout.journal.com.workoutjournal.db;


import android.arch.lifecycle.LiveData;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseOrmEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalDateEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;
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
     * Journal Data
     */
    public LiveData<List<JournalDateEntity>> getDateListLimitLive(int limit) {
        return database.getJournalDao().getDateListLimitLive(limit);
    }

    public LiveData<List<ExerciseSetRepRelation>> getExerciseSetRepRelationLive(Long... times) {
        return database.getJournalDao().getExerciseSetRepRelationLive(times[0], times[1]);
    }

    public LiveData<List<JournalSetEntity>> getSetListByIdLive(Long... times) {
        return database.getJournalDao().getSetListByIdLive(times[0], times[1]);
    }

    private JournalDateEntity getDateById(Long dateId) {
        return database.getJournalDao().getDateById(dateId);
    }

    private void deleteDate(JournalDateEntity dateEntity) {
        database.getJournalDao().deleteDates(dateEntity);
    }

    public void deleteDateSetsNull(final Long dateId) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                JournalDateEntity dateEntity = getDateById(dateId);
                if (dateEntity == null) {
                    return;
                }
                deleteDate(dateEntity);
            }
        });
    }

    /**
     * Entry Data
     */
    public LiveData<ExerciseLiftEntity> getExerciseByIdLive(String id) {
        return database.getJournalDao().getExerciseByIdLive(id);
    }

    public LiveData<JournalDateEntity> getDateByTimestampLive(final Long... times) {
        return database.getJournalDao().getDateByTimestampLive(times[0], times[1]);
    }

    public LiveData<JournalSetEntity> getSetByExerciseIdAndDateId(String exerciseId, Long dateId) {
        return database.getJournalDao().getSetByExerciseIdAndDateIdLive(exerciseId, dateId);
    }

    public LiveData<ExerciseOrmEntity> getOneRepMaxByExerciseId(String exerciseId) {
        return database.getJournalDao().getOneRepMaxByExerciseIdLive(exerciseId);
    }

    public LiveData<ExerciseSetRepRelation> getEntrySetRepsAndOrm(String exerciseId, Long... times) {
        return database.getJournalDao().getEntrySetRepsAndOrmLive(exerciseId, times[0], times[1]);
    }

    public LiveData<List<ExerciseSetRepRelation>> getExerciseSetRepRelationHistoryLive(String exerciseId, Long... times) {
        return database.getJournalDao().getExerciseSetRepRelationHistoryLive(exerciseId, times[0]);
    }

    public void insertDate(final JournalDateEntity journalDateEntity) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getJournalDao().insertDates(journalDateEntity);
            }
        });
    }

    public void deleteSet(final JournalSetEntity journalSetEntity) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getJournalDao().deleteSets(journalSetEntity);
            }
        });
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final JournalRepEntity journalRepEntity = database.getJournalDao().getRepWithLargestOrm(journalSetEntity.getExerciseId());
                final ExerciseOrmEntity ormEntity = database.getJournalDao().getOneRepMaxByExerciseId(journalSetEntity.getExerciseId());
                if (journalRepEntity == null) {
                    if (ormEntity != null) {
                        database.getJournalDao().deleteOrms(ormEntity);
                    }
                } else {
                    ormEntity.setRepId(journalRepEntity.getId());
                    ormEntity.setOneRepMax(journalRepEntity.getOneRepMax());
                    ormEntity.setWeight(journalRepEntity.getWeight());
                    ormEntity.setReps(journalRepEntity.getReps());
                    ormEntity.setTimestamp(journalRepEntity.getTimestamp());
                    database.getJournalDao().updateOrms(ormEntity);
                }
            }
        });
    }

    public void insertSet(final JournalSetEntity setEntity) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getJournalDao().insertSets(setEntity);
            }
        });
    }

    public void insertRepAndOrmTransaction(final JournalRepEntity repEntity, final ExerciseOrmEntity ormEntity) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getJournalDao().insertSetAndOrmTransaction(repEntity, ormEntity);
            }
        });
    }

    public void insertRepAndUpdateOrmTransaction(final JournalRepEntity repEntity, final ExerciseOrmEntity ormEntity) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getJournalDao().insertSetAndUpdateOrmTransaction(repEntity, ormEntity);
            }
        });
    }

    public void insertReps(final JournalRepEntity... repEntities) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getJournalDao().insertReps(repEntities);
            }
        });
    }

    public void deleteRepAndUpdateOrm(final JournalRepEntity repEntity, final List<JournalRepEntity> repEntityList, final ExerciseOrmEntity ormEntity) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getJournalDao().deleteRepAndUpdateListPositions(repEntity, repEntityList);
            }
        });
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final JournalRepEntity journalRepEntity = database.getJournalDao().getRepWithLargestOrm(repEntity.getExerciseId());
                if (journalRepEntity == null) {
                    database.getJournalDao().deleteOrms(ormEntity);
                } else {
                    ormEntity.setRepId(journalRepEntity.getId());
                    ormEntity.setOneRepMax(journalRepEntity.getOneRepMax());
                    ormEntity.setWeight(journalRepEntity.getWeight());
                    ormEntity.setReps(journalRepEntity.getReps());
                    ormEntity.setTimestamp(journalRepEntity.getTimestamp());
                    database.getJournalDao().updateOrms(ormEntity);
                }
            }
        });
    }

    public void updateRepNew(final JournalRepEntity repEntity, final ExerciseOrmEntity ormEntity) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getJournalDao().updateReps(repEntity);
            }
        });
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                JournalRepEntity journalRepEntity = database.getJournalDao().getRepWithLargestOrm(repEntity.getExerciseId());
                ormEntity.setRepId(journalRepEntity.getId());
                ormEntity.setOneRepMax(journalRepEntity.getOneRepMax());
                ormEntity.setWeight(journalRepEntity.getWeight());
                ormEntity.setReps(journalRepEntity.getReps());
                ormEntity.setTimestamp(journalRepEntity.getTimestamp());
                database.getJournalDao().updateOrms(ormEntity);
            }
        });
    }


    /**
     * Exercises
     */
    public LiveData<List<ExerciseLiftEntity>> getAllExercises() {
        return database.getExerciseLiftDao().getAllExercises();
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
}
