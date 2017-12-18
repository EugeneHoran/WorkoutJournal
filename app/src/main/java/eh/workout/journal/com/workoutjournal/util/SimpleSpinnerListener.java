package eh.workout.journal.com.workoutjournal.util;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;


public class SimpleSpinnerListener {

    private RepsSpinnerInterface callback;

    public interface RepsSpinnerInterface {
        void onRepsChanged();
    }

    public SimpleSpinnerListener setCallback(RepsSpinnerInterface callback) {
        this.callback = callback;
        return this;
    }

    public SimpleSpinnerListener registerSpinner(final Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (callback != null) {
                    callback.onRepsChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        return this;
    }
}
