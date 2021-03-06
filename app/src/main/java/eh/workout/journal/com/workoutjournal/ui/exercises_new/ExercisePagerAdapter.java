package eh.workout.journal.com.workoutjournal.ui.exercises_new;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ExercisePagerAdapter extends FragmentPagerAdapter {

    private static final int FRAG_EXERCISES = 0;
    private static final int FRAG_GROUPS = 1;
    private static final int FRAG_PLAN = 2;
    private static final int FRAG_ROUTINE = 3;

    private Long timestamp;
    private Fragment[] fragments;
    private String[] titleList = {
            "Exercises",
            "Groups",
            "Plans",
            "Routines"};

    public ExercisePagerAdapter(FragmentManager fm, Long timestamp) {
        super(fm);
        this.timestamp = timestamp;
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments != null && fragments.length > position && fragments[position] != null) {
            return fragments[position];
        }
        if (fragments == null) {
            fragments = new Fragment[getCount()];
        }
        switch (position) {
            case FRAG_EXERCISES:
                fragments[FRAG_EXERCISES] = ExerciseListFragment.newInstance();
            case FRAG_GROUPS:
                fragments[FRAG_GROUPS] = ExerciseListFragment.newInstance();
            case FRAG_PLAN:
                fragments[FRAG_PLAN] = ExerciseListFragment.newInstance();
            case FRAG_ROUTINE:
                fragments[FRAG_ROUTINE] = ExerciseListFragment.newInstance();
        }
//        fragments[FRAG_EXERCISES] = ExerciseListFragment.newInstance();
//        fragments[FRAG_EXERCISES] = ExerciseListFragment.newInstance();
//        if (Constants.SETTINGS_SHOW_ROUTINE_PLAN) {
//            switch (position) {
//                case FRAG_EXERCISES:
//                    fragments[FRAG_EXERCISES] = ExerciseSelectorFragment.newInstance();
//                case FRAG_GROUPS:
//                    fragments[FRAG_GROUPS] = ExerciseGroupFragment.newInstance();
//                case FRAG_PLAN:
//                    fragments[FRAG_PLAN] = ExercisePlanFragment.newInstance(timestamp);
//                case FRAG_ROUTINE:
//                    fragments[FRAG_ROUTINE] = ExerciseRoutineFragment.newInstance(timestamp);
//            }
//        } else {
//            switch (position) {
//                case FRAG_EXERCISES:
//                    fragments[FRAG_EXERCISES] = ExerciseSelectorFragment.newInstance();
//                case FRAG_GROUPS:
//                    fragments[FRAG_GROUPS] = ExerciseGroupFragment.newInstance();
//            }
//        }
        return fragments[position];
    }

    @Override
    public int getCount() {
//        if (Constants.SETTINGS_SHOW_ROUTINE_PLAN) {
//            return titleList.length;
//        }
        return titleList.length;
    }

//    ExerciseGroupFragment getGroupFragment() {
//        return (ExerciseGroupFragment) fragments[FRAG_GROUPS];
//    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList[position];
    }

}
