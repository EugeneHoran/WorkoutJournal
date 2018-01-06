package eh.workout.journal.com.workoutjournal.db;


import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

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
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;
import eh.workout.journal.com.workoutjournal.db.relations.PlanDaySetRelation;
import eh.workout.journal.com.workoutjournal.db.relations.PlanSetRelation;
import eh.workout.journal.com.workoutjournal.db.relations.RoutineSetRelation;

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
     * Plans
     */

    public PlanDaySetRelation getPlanDaySetRelation(String planId) {
        return database.getPlanDao().getPlanDaySetRelation(planId);
    }

    public void updatePlanDaySetRelation(final PlanDayEntity planDayEntity, final List<PlanDaySetEntity> deleteSets, final List<PlanDaySetEntity> updateSets) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getPlanDao().deletePlanDaySetEntities(deleteSets);
            }
        });
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getPlanDao().updatePlanDaySets(planDayEntity, updateSets);
            }
        });
    }

    public void insertPlanSets(final PlanEntity planEntity, final List<PlanSetEntity> planSetEntities) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getPlanDao().insertPlanSets(planEntity, planSetEntities);
            }
        });
    }

    public void insertPlanDaySets(final PlanDayEntity planDayEntity, final List<PlanDaySetEntity> planDaySetEntityList) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getPlanDao().insertPlanDaySets(planDayEntity, planDaySetEntityList);
            }
        });
    }

    public void deletePlanSets(final PlanEntity planEntity) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getPlanDao().deletePlanEntity(planEntity);
            }
        });
    }

    public void deletePlanDayEntity(final PlanDayEntity planDayEntity) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getPlanDao().deletePlanDayEntity(planDayEntity);
            }
        });
    }

    public LiveData<List<PlanSetRelation>> getPlanSetRelationListLive() {
        return database.getPlanDao().getPlanSetRelationListLive();
    }

    public LiveData<List<PlanDaySetRelation>> getPlanSetDayRelationListLive(long timestamp) {
        return database.getPlanDao().getPlanSetDayRelationListLive(timestamp);
    }

    public List<PlanDaySetRelation> getPlanSetDayRelationList(long timestamp) {
        return database.getPlanDao().getPlanSetDayRelationList(timestamp);
    }

    /**
     * Routines
     */

    public LiveData<List<RoutineSetRelation>> getPlanSetRelationListLive(Integer day) {
        return database.getRoutineDao().getRoutineSetRelationListLive("%" + String.valueOf(day) + "%");
    }

    public List<RoutineSetRelation> getAllRoutinesWithSets() {
        return database.getRoutineDao().getAllRoutinesWithSets();
    }

    public List<RoutineSetRelation> getRoutineSetRelationList(Integer day) {
        return database.getRoutineDao().getRoutineSetRelationList("%" + String.valueOf(day) + "%");
    }

    public RoutineSetRelation getRoutineSetRelation(String routineId) {
        return database.getRoutineDao().getRoutineSetRelation(routineId);
    }

    public void insertRoutine(final RoutineEntity routineEntity, final List<RoutineSetEntity> planSetEntityList) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getRoutineDao().insertNewRoutine(routineEntity, planSetEntityList);
            }
        });
    }

    public void deleteRoutine(final RoutineEntity deleteRoutineEntity) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getRoutineDao().deleteRoutineEntity(deleteRoutineEntity);
            }
        });
    }

    public void deleteAndInsertRoutine(final RoutineEntity deleteRoutineEntity, final RoutineEntity newRoutineEntity, final List<RoutineSetEntity> newPlanSetEntityList) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getRoutineDao().deleteRoutineEntity(deleteRoutineEntity);
            }
        });
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getRoutineDao().insertNewRoutine(newRoutineEntity, newPlanSetEntityList);
            }
        });
    }


    /**
     * Journal Data
     */
    public LiveData<List<JournalDateEntity>> getDateListLimitLive(int limit) {
        return database.getJournalDao().getDateListLimitLive(limit);
    }

    public List<JournalDateEntity> getDateList() {
        return database.getJournalDao().getDateList();
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

    public ExerciseLiftEntity getExerciseById(String id) {
        return database.getJournalDao().getExerciseById(id);
    }

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
                Log.e("Testing", "Deleted");
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

    public List<ExerciseLiftEntity> getAllExercisesList() {
        return database.getExerciseLiftDao().getAllExercisesList();
    }

    public LiveData<List<ExerciseLiftEntity>> getAllExercisesLive() {
        return database.getExerciseLiftDao().getAllExercisesLive();
    }

    public List<ExerciseGroupEntity> getAllExercisesGroupsList() {
        return database.getExerciseLiftDao().getAllExercisesGroupsList();
    }

    public LiveData<List<ExerciseGroupEntity>> getAllExercisesGroupsLive() {
        return database.getExerciseLiftDao().getAllExercisesGroupsLive();
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
