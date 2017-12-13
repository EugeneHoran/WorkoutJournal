package eh.workout.journal.com.workoutjournal.util;


import java.util.List;

import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;

public class EquationsHelper {

    public static double getOneRepMax(String w, String r) {
        double weight = Double.valueOf(w);
        double reps = Double.valueOf(r);
        return (double) weight / (1.0278 - (0.0278 * reps));
    }

    public static int getOneRepMaxInt(double orm) {
        return (int) Math.rint(orm);
    }


    public static int getRepPosition(ExerciseSetRepRelation exerciseSetRepRelation) {
        int pos = 0;
        if (exerciseSetRepRelation != null) {
            if (exerciseSetRepRelation.getJournalRepEntityList() != null) {
                pos = exerciseSetRepRelation.getJournalRepEntityList().size();
            }
        }
        return pos + 1;
    }
}
