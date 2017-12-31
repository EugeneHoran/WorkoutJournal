package eh.workout.journal.com.workoutjournal.ui.journal;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;
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
}
