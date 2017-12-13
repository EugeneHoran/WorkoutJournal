package eh.workout.journal.com.workoutjournal.base;

import android.os.Build;
import android.support.transition.Fade;
import android.support.v4.app.Fragment;
import android.view.View;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.ui.MainActivity;
import eh.workout.journal.com.workoutjournal.ui.add.exercise.AddExerciseParentFragment;
import eh.workout.journal.com.workoutjournal.ui.exercises.ExerciseSelectorAddExerciseDialogFragment;
import eh.workout.journal.com.workoutjournal.ui.exercises.ExerciseSelectorFragment;
import eh.workout.journal.com.workoutjournal.util.DetailsTransition;


public class BaseFragment extends Fragment {
    private static final String TAG_ADD_LIFT_DIALOG_FRAGMENT = "tag_add_lift_dialog_fragment";

    public void dialogNewExercise(ExerciseSelectorFragment fragment) {
        ExerciseSelectorAddExerciseDialogFragment addExerciseDialogFragment = ExerciseSelectorAddExerciseDialogFragment.newInstance();
        addExerciseDialogFragment.setListener(fragment);
        addExerciseDialogFragment.show(getChildFragmentManager(), TAG_ADD_LIFT_DIALOG_FRAGMENT);
    }

    /**
     * Navigation
     */

    public void navToSelectExerciseFragment(View view, Long timestamp) {
        ExerciseSelectorFragment exerciseSelectorFragment = ExerciseSelectorFragment.newInstance(timestamp);
        initTransition(exerciseSelectorFragment);
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addSharedElement(view, "app_bar")
                    .replace(R.id.container, exerciseSelectorFragment, MainActivity.TAG_FRAG_EXERCISE_SELECTOR)
                    .addToBackStack(MainActivity.TAG_FRAG_EXERCISE_SELECTOR).commit();
        }
    }


    @SuppressWarnings("ConstantConditions")
    public void navToAddExerciseFragment(View view, String id, Long timestamp) {
        AddExerciseParentFragment fragment = AddExerciseParentFragment.newInstance(id, timestamp);
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
        initTransition(fragment);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(view, "app_bar")
                .replace(R.id.container,
                        fragment,
                        MainActivity.TAG_FRAG_ADD_EXERCISE)
                .addToBackStack(MainActivity.TAG_FRAG_ADD_EXERCISE)
                .commit();
    }

    private void initTransition(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementEnterTransition(new DetailsTransition());
            fragment.setEnterTransition(new Fade());
            fragment.setSharedElementReturnTransition(new DetailsTransition());
        }
    }
}
