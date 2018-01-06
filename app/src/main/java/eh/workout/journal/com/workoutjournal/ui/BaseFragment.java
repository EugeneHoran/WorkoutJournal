package eh.workout.journal.com.workoutjournal.ui;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.transition.Fade;
import android.support.v4.app.Fragment;
import android.view.View;

import com.roomorama.caldroid.CaldroidFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.ui.calendar.CaldroidBottomSheetFragment;
import eh.workout.journal.com.workoutjournal.ui.calendar.CalendarBottomSheetFragment;
import eh.workout.journal.com.workoutjournal.ui.entry.EntryParentFragment;
import eh.workout.journal.com.workoutjournal.ui.exercises.ExerciseParentFragment;
import eh.workout.journal.com.workoutjournal.ui.exercises.ExerciseSelectorAddExerciseDialogFragment;
import eh.workout.journal.com.workoutjournal.ui.orm.OneRepMaxFragment;
import eh.workout.journal.com.workoutjournal.ui.plan.PlanAddActivity;
import eh.workout.journal.com.workoutjournal.ui.plan.edit.PlanDayEditActivity;
import eh.workout.journal.com.workoutjournal.ui.routine.RoutineAddActivity;
import eh.workout.journal.com.workoutjournal.ui.routine.edit.EditRoutineActivity;
import eh.workout.journal.com.workoutjournal.util.Constants;
import eh.workout.journal.com.workoutjournal.util.DetailsTransition;

@SuppressWarnings("ConstantConditions")
public class BaseFragment extends Fragment {
    private static final String TAG_FRAG_CALENDAR = "tag_calendar_frag";
    private static final String TAG_ADD_LIFT_DIALOG_FRAGMENT = "tag_add_lift_dialog_fragment";

    public Application getApplicationChild() {
        if (getParentFragment() != null) {
            if (getParentFragment().getActivity() != null) {
                return (JournalApplication) getParentFragment().getActivity().getApplicationContext();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void dialogNewExercise() {
        ExerciseSelectorAddExerciseDialogFragment addExerciseDialogFragment = ExerciseSelectorAddExerciseDialogFragment.newInstance();
        addExerciseDialogFragment.show(getChildFragmentManager(), TAG_ADD_LIFT_DIALOG_FRAGMENT);
    }

    public void navToAddRoutineActivity(int page, int requestCode) {
        if (getActivity() != null) {
            Intent intentPlan = new Intent(getActivity(), RoutineAddActivity.class);
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

    /**
     * Navigation
     */
    public void showCalendarBottomSheet(CalendarBottomSheetFragment caldroidFragment, Date date, HashMap<String, Object> dateList) {
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        caldroidFragment.setSelectedDate(date);
        args.putInt(CaldroidBottomSheetFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidBottomSheetFragment.YEAR, cal.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.THEME_RESOURCE, R.style.CaldroidCustom);
        args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS, false);
        args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, true);
        caldroidFragment.setArguments(args);
        caldroidFragment.show(getChildFragmentManager(), TAG_FRAG_CALENDAR);
    }

    public void navToSelectExerciseFragment(View view, Long timestamp, int page) {
        ExerciseParentFragment exerciseSelectorFragment = ExerciseParentFragment.newInstance(timestamp, page);
        initTransition(exerciseSelectorFragment);
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addSharedElement(view, "app_bar")
                    .replace(R.id.container, exerciseSelectorFragment, MainActivity.TAG_FRAG_EXERCISE_SELECTOR)
                    .addToBackStack(MainActivity.TAG_FRAG_EXERCISE_SELECTOR).commit();
        }
    }


    public void navToAddExerciseFragment(View view, String id, int inputType, Long timestamp) {
        EntryParentFragment fragment = EntryParentFragment.newInstance(id, inputType, timestamp);
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

    public void navToOneRepMaxFragment(View view, int which) {
        OneRepMaxFragment oneRepMaxFragment = OneRepMaxFragment.newInstance(which);
        initTransition(oneRepMaxFragment);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(view, "app_bar")
                .replace(R.id.container, oneRepMaxFragment, MainActivity.TAG_FRAG_ORM)
                .addToBackStack(MainActivity.TAG_FRAG_ORM)
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
