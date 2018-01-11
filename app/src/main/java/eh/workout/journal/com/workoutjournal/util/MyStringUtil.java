package eh.workout.journal.com.workoutjournal.util;


import android.text.Html;
import android.text.Spanned;

import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseOrmEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;

public class MyStringUtil {

    public static Spanned formatRepSet(Integer pos) {
        return Html.fromHtml(String.valueOf(pos) + "<small> set</small>");
    }


    //    "<font color='black'>" + OrmHelper.getOneRepMaxInt(ormEntity.getOneRepMax()) + "</font>"
    public static Spanned formatRepWeight(String weight) {
        return Html.fromHtml(weight + "<small> " + Constants.SETTINGS_UNIT_MEASURE + "</small>");
    }

    public static Spanned formatRepWeightBlack(String weight) {
        return Html.fromHtml("<font color='black'>" + weight + "</font>" + "<small> " + Constants.SETTINGS_UNIT_MEASURE + "</small>");
    }

    public static Spanned formatRepReps(String reps) {
        return Html.fromHtml(reps + "<small> reps</small>");
    }

    public static Spanned formatRepRepsBlack(String reps) {
        return Html.fromHtml("<font color='black'>" + reps + "</font>" + "<small> reps</small>");
    }

    public static Spanned formatOneRepMaxWeight(ExerciseOrmEntity ormEntity) {
        String test = ormEntity.getWeight() + "<small> " + Constants.SETTINGS_UNIT_MEASURE + "</small>" + " for " + ormEntity.getReps() + "<small> reps</small> = " + OrmHelper.getOneRepMaxInt(ormEntity.getOneRepMax()) + "<small> " + Constants.SETTINGS_UNIT_MEASURE + " (1RM)" + "</small>";
        return Html.fromHtml(test);
    }


    public static Spanned formatOneRepMaxWeightFromRep(JournalRepEntity ormEntity) {
        String test = ormEntity.getWeight() + "<small> " + Constants.SETTINGS_UNIT_MEASURE + "</small>" + " for " + ormEntity.getReps() + "<small> reps</small> = " + OrmHelper.getOneRepMaxInt(ormEntity.getOneRepMax()) + "<small> " + Constants.SETTINGS_UNIT_MEASURE + " (1RM)" + "</small>";
        return Html.fromHtml(test);
    }

    public static Spanned formatOneRepMaxBlack(JournalRepEntity ormEntity) {
        String test = "<font color='black'>" + OrmHelper.getOneRepMaxInt(ormEntity.getOneRepMax()) + "</font>" + "<small>" + " 1RM" + "</small>";
        return Html.fromHtml(test);
    }

    public static Spanned formatOneRepMaxReps(ExerciseOrmEntity ormEntity) {
        String test = ormEntity.getReps() + "<small> reps</small>";
        return Html.fromHtml(test);
    }

    public static String showWorkoutPlan(boolean showWorkoutPlan) {
        return showWorkoutPlan ? "Hide workout plan" : "Show workout plan";
    }

}
