package eh.workout.journal.com.workoutjournal.util;


public class EquationsHelper {

    public static double getOneRepMax(String w, String r) {
        double weight = Double.valueOf(w);
        double reps = Double.valueOf(r);
        return (double) weight / (1.0278 - (0.0278 * reps));
    }

    public static int getOneRepMaxInt(double orm) {
        return (int) Math.rint(orm);
    }
}
