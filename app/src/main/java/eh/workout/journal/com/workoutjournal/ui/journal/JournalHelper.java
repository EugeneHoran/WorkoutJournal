package eh.workout.journal.com.workoutjournal.ui.journal;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;
import eh.workout.journal.com.workoutjournal.db.relations.PlanDaySetRelation;
import eh.workout.journal.com.workoutjournal.db.relations.RoutineSetRelation;

class JournalHelper {

    List<RoutineSetRelation> getPlanSetsCompleteList(List<ExerciseSetRepRelation> setRepRelations, List<RoutineSetRelation> routineSetRelations) {
        if (setRepRelations != null) {
            if (setRepRelations.size() > 0 && routineSetRelations != null) {
                for (int i = 0; i < routineSetRelations.size(); i++) {
                    for (int j = 0; j < routineSetRelations.get(i).getPlanSetEntityList().size(); j++) {
                        for (ExerciseSetRepRelation exerciseSetRepRelation : setRepRelations) {
                            if (routineSetRelations.get(i).getPlanSetEntityList().get(j).getExerciseId().equalsIgnoreCase(exerciseSetRepRelation.getJournalSetEntity().getExerciseId())) {
                                routineSetRelations.get(i).getPlanSetEntityList().get(j).setSetCompleted(true);
                            }
                        }
                    }
                }
            }
        }
        return routineSetRelations;
    }


    List<Object> getCompleteList(List<ExerciseSetRepRelation> sets, List<RoutineSetRelation> routineSetRelations, List<PlanDaySetRelation> planDaySetRelations) {
        List<Object> items = new ArrayList<>();


        if (sets != null && sets.size() > 0) {
            if (routineSetRelations != null) {
                for (int i = 0; i < routineSetRelations.size(); i++) {
                    for (int j = 0; j < routineSetRelations.get(i).getPlanSetEntityList().size(); j++) {
                        for (ExerciseSetRepRelation exerciseSetRepRelation : sets) {
                            if (routineSetRelations.get(i).getPlanSetEntityList().get(j).getExerciseId().equalsIgnoreCase(exerciseSetRepRelation.getJournalSetEntity().getExerciseId())) {
                                routineSetRelations.get(i).getPlanSetEntityList().get(j).setSetCompleted(true);
                            }
                        }
                    }
                }
            }
            if (planDaySetRelations != null) {
                for (int i = 0; i < planDaySetRelations.size(); i++) {
                    for (int j = 0; j < planDaySetRelations.get(i).getPlanDaySetEntityList().size(); j++) {
                        for (ExerciseSetRepRelation exerciseSetRepRelation : sets) {
                            if (planDaySetRelations.get(i).getPlanDaySetEntityList().get(j).getExerciseId().equalsIgnoreCase(exerciseSetRepRelation.getJournalSetEntity().getExerciseId())) {
                                planDaySetRelations.get(i).getPlanDaySetEntityList().get(j).setSetCompleted(true);
                            }
                        }
                    }
                }
            }
        }
        if (routineSetRelations != null) {
            items.addAll(routineSetRelations);
        }
        if (planDaySetRelations != null) {
            items.addAll(planDaySetRelations);
        }
        return items;
    }
}
