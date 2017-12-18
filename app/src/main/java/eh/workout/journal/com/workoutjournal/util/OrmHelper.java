package eh.workout.journal.com.workoutjournal.util;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;

public class OrmHelper {


    public static double getOneRepMax(String w, String r) {
        double weight = Double.valueOf(w);
        double reps = Double.valueOf(r);
        return weight / (1.0278 - (0.0278 * reps));
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


    public static String getOrm(Integer w, int r) {
        return String.valueOf((int) Math.rint((double) w / (1.0278 - (0.0278 * r))));
    }

    public static List<String> ormList(Integer w, int r) {
        if (w == null) {
            return null;
        }
        List<String> ormList = new ArrayList<>();
        List<Double> percentages = getBrzyckiPrecentages(w, r);
        for (int i = 0; i < percentages.size(); i++) {
            ormList.add(String.valueOf((int) Math.rint(percentages.get(i))));
        }
        return ormList;
    }

    public static List<String> ormEmptyList() {
        List<String> ormList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            ormList.add("0");
        }
        return ormList;
    }

    private static double[] percentagesBrzycki = {
            1, .95, .90, .88, .86, .83,
            .80, .78, .76, .75, .72, .70};

    private static List<Double> getBrzyckiPrecentages(int w, int r) {
        double orm = (double) w / (1.0278 - (0.0278 * r));
        List<Double> reps = new ArrayList<>();
        for (double aPercentagesBrzycki : percentagesBrzycki) {
            reps.add(orm * aPercentagesBrzycki);
        }
        return reps;
    }


    /**
     * Percentages
     */
    private static double[] percentages = {
            1.25, 1.2, 1.15, 1.1, 1, .95, .9, .85, .80, .75, .7, .65,
            .6, .55, .5, .45, .4, .35, .3, .25, .2, .15, .1, .05
    };

    public static int getMaxPercentages(int pos) {
        return (int) Math.rint(percentages[pos] * 100.0);
    }

    public static List<String> getPercentageList(Integer w, int r, Double oneRepmax) {
        if (w == null && oneRepmax == null) {
            return null;
        }
        List<String> percentageList = new ArrayList<>();
        double orm = oneRepmax == null ? (double) w / (1.0278 - (0.0278 * r)) : oneRepmax;
        for (double percentage : percentages) {
            percentageList.add(String.valueOf((int) Math.rint(percentage * orm)));
        }
        return percentageList;
    }

    public static List<String> percentageEmptyList() {
        List<String> ormList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            ormList.add("0.0");
        }
        return ormList;
    }
}
