package eh.workout.journal.com.workoutjournal.ui;

import android.content.Intent;
import android.os.Build;
import android.support.transition.Explode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.ui.entry.EntryParentFragment;
import eh.workout.journal.com.workoutjournal.ui.exercises.ExerciseParentFragment;
import eh.workout.journal.com.workoutjournal.ui.exercises.ExerciseSelectorAddExerciseDialogFragment;
import eh.workout.journal.com.workoutjournal.ui.exercises_new.ExerciseFragment;
import eh.workout.journal.com.workoutjournal.ui.orm.OneRepMaxFragment;
import eh.workout.journal.com.workoutjournal.ui.plan.PlanAddActivity;
import eh.workout.journal.com.workoutjournal.ui.plan.edit.PlanDayEditActivity;
import eh.workout.journal.com.workoutjournal.ui.routine_new.RoutineActivity;
import eh.workout.journal.com.workoutjournal.ui.routine_new.edit.EditRoutineActivity;
import eh.workout.journal.com.workoutjournal.util.AnimationTransition;
import eh.workout.journal.com.workoutjournal.util.Constants;

@SuppressWarnings("ConstantConditions")
public class BaseFragment extends Fragment {
    private static final String TAG_ADD_LIFT_DIALOG_FRAGMENT = "tag_add_lift_dialog_fragment";

    private void initTransitionN(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementReturnTransition(new AnimationTransition());
            fragment.setExitTransition(new Explode());
            fragment.setSharedElementEnterTransition(new AnimationTransition());
            fragment.setEnterTransition(new Explode());
        }
    }

    public void dialogNewExercise() {
        ExerciseSelectorAddExerciseDialogFragment addExerciseDialogFragment = ExerciseSelectorAddExerciseDialogFragment.newInstance();
        addExerciseDialogFragment.show(getChildFragmentManager(), TAG_ADD_LIFT_DIALOG_FRAGMENT);
    }

    public void navToAddEntryFragment(View view, View fab, String id, int inputType, Long timestamp) {
        EntryParentFragment fragment = EntryParentFragment.newInstance(id, inputType, timestamp);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        initTransitionN(fragment);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            transaction.addSharedElement(view, "app_bar");
            if (fab != null) {
                try {
                    transaction.addSharedElement(fab, ViewCompat.getTransitionName(fab));
                } catch (Exception e) {
                    Log.e("Testing", e.toString());
                }
            }
        }
        transaction.replace(R.id.container,
                fragment,
                MainActivity.TAG_FRAG_ADD_EXERCISE)
                .addToBackStack(MainActivity.TAG_FRAG_ADD_EXERCISE)
                .commit();
    }

    public void navToSelectExerciseFragment(View view, Long timestamp, int page) {
        ExerciseParentFragment exerciseSelectorFragment = ExerciseParentFragment.newInstance(timestamp, page);
        initTransitionN(exerciseSelectorFragment);
        if (getActivity() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addSharedElement(view, "app_bar")
                        .replace(R.id.container, exerciseSelectorFragment, MainActivity.TAG_FRAG_EXERCISE_SELECTOR)
                        .addToBackStack(MainActivity.TAG_FRAG_EXERCISE_SELECTOR).commit();
            } else {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, exerciseSelectorFragment, MainActivity.TAG_FRAG_EXERCISE_SELECTOR)
                        .addToBackStack(MainActivity.TAG_FRAG_EXERCISE_SELECTOR).commit();
            }
        }
    }

    public void navToOneRepMaxFragment(View view, int which) {
        OneRepMaxFragment oneRepMaxFragment = OneRepMaxFragment.newInstance(which);
        initTransitionN(oneRepMaxFragment);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .addSharedElement(view, "app_bar")
                    .replace(R.id.container, oneRepMaxFragment, MainActivity.TAG_FRAG_ORM)
                    .addToBackStack(MainActivity.TAG_FRAG_ORM)
                    .commit();
        } else {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, oneRepMaxFragment, MainActivity.TAG_FRAG_ORM)
                    .addToBackStack(MainActivity.TAG_FRAG_ORM)
                    .commit();
        }
    }


    /**
     * Routines And Plans
     */

    public void navToAddRoutineActivity(int page, int requestCode, View view) {
        if (getActivity() != null) {
            Intent intentPlan = new Intent(getActivity(), RoutineActivity.class);
            intentPlan.putExtra(Constants.JOURNAL_PAGE_RESULT_CODE_PLAN, page);
            getActivity().startActivityForResult(intentPlan, requestCode);
        }
    }

    public void navToAddPlanActivity(int page, int requestCode) {
        if (getActivity() != null) {
            Intent intentPlan = new Intent(getActivity(), PlanAddActivity.class);
            intentPlan.putExtra(Constants.JOURNAL_PAGE_RESULT_CODE_PLAN, page);
            getActivity().startActivityForResult(intentPlan, requestCode);
        }
    }

    public void navToEditPlanActivity(int page, int requestCode, String planId) {
        if (getActivity() != null) {
            Intent intentPlan = new Intent(getActivity(), PlanDayEditActivity.class);
            intentPlan.putExtra(Constants.JOURNAL_PAGE_RESULT_CODE_PLAN, page);
            intentPlan.putExtra(Constants.EDIT_PLAN_ID, planId);
            getActivity().startActivityForResult(intentPlan, requestCode);
        }
    }

    public void navToEditRoutineActivity(int page, String planId, int requestCode) {
        if (getActivity() != null) {
            Intent editPlanIntent = new Intent(getActivity(), EditRoutineActivity.class);
            editPlanIntent.putExtra(Constants.JOURNAL_PAGE_RESULT_CODE_PLAN, page);
            editPlanIntent.putExtra(Constants.EDIT_PLAN_ID, planId);
            getActivity().startActivityForResult(editPlanIntent, requestCode);
        }
    }

}
