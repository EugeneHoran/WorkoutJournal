package eh.workout.journal.com.workoutjournal.util.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

public class LayoutUtil {
    public static Drawable getDrawableMutate(Context context, int resDrawable, int resColor) {
        Drawable drawableChange = ContextCompat.getDrawable(context, resDrawable);
        if (drawableChange != null) {
            drawableChange.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, resColor), PorterDuff.Mode.MULTIPLY));
            drawableChange.mutate();
        }
        return drawableChange;
    }

    public static Drawable getDrawableMutate(Context context, Drawable resDrawable, int resColor) {
        resDrawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, resColor), PorterDuff.Mode.MULTIPLY));
        resDrawable.mutate();
        return resDrawable;
    }
}
