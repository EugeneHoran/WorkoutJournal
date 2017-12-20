package eh.workout.journal.com.workoutjournal.util.views;

import android.annotation.TargetApi;
import android.databinding.BindingAdapter;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import eh.workout.journal.com.workoutjournal.R;


public class MyBindingAdapter {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @BindingAdapter("workoutDrawableRight")
    public static void setShowWorkoutDrawableRight(TextView textView, boolean showWorkoutPlan) {
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(textView.getContext(), showWorkoutPlan ? R.drawable.ic_expand_less : R.drawable.ic_expand_more), null);
    }
}
