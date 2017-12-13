package eh.workout.journal.com.workoutjournal.util;


import android.text.Html;
import android.text.Spanned;

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
}
