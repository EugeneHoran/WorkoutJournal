package eh.workout.journal.com.workoutjournal.util;


import android.text.Html;
import android.text.Spanned;

import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseOrmEntity;

public class MyStringUtil {

    public static Spanned formatRepSet(Integer pos) {
        return Html.fromHtml(String.valueOf(pos) + "<small> set</small>");
    }

    public static Spanned formatRepWeight(String weight) {
        return Html.fromHtml(weight + "<small> " + Constants.UNIT + "</small>");
    }

    public static Spanned formatRepReps(String reps) {
        return Html.fromHtml(reps + "<small> reps</small>");
    }

    public static Spanned formatOneRepMaxWeight(ExerciseOrmEntity ormEntity) {
        String test = ormEntity.getWeight() + "<small> " + Constants.UNIT + "</small>" + " for " + ormEntity.getReps() + "<small> reps</small> = " + OrmHelper.getOneRepMaxInt(ormEntity.getOneRepMax()) + "<small> " + Constants.UNIT + " (1RM)" + "</small>";
        return Html.fromHtml(test);
    }

    public static Spanned formatOneRepMaxReps(ExerciseOrmEntity ormEntity) {
        String test = ormEntity.getReps() + "<small> reps</small>";
        return Html.fromHtml(test);
    }
}
