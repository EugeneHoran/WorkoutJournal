package eh.workout.journal.com.workoutjournal.util.views;

import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.TextView;

import eh.workout.journal.com.workoutjournal.R;

public class CustomSnackbar extends BaseTransientBottomBar<CustomSnackbar> {
    public CustomSnackbar(
            CoordinatorLayout parent,
            View content,
            BaseTransientBottomBar.ContentViewCallback contentViewCallback) {
        super(parent, content, contentViewCallback);
    }

    public CustomSnackbar setTitle(String title) {
        TextView titleView = getView().findViewById(R.id.custom_snackbar_title);
        titleView.setText(title);
        return this;
    }

    public CustomSnackbar setSubtitle(String subtitle) {
        TextView subtitleView = getView().findViewById(R.id.custom_snackbar_subtitle);
        subtitleView.setText(subtitle);
        return this;
    }
}