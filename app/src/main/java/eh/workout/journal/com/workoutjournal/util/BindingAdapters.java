package eh.workout.journal.com.workoutjournal.util;


import android.databinding.BindingAdapter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import eh.workout.journal.com.workoutjournal.R;

public class BindingAdapters {
    @BindingAdapter("bind:animateCard")
    public static void setPaddingLeft(View view, boolean animate) {
        if (!animate) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
            Animation slide = AnimationUtils.loadAnimation(view.getContext(), R.anim.anim_slide_down_show);
            slide.setStartOffset(500);
            view.startAnimation(slide);
        }
    }
}
