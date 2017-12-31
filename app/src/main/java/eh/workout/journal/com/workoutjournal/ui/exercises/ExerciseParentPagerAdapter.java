package eh.workout.journal.com.workoutjournal.ui.exercises;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ExerciseParentPagerAdapter extends FragmentPagerAdapter {
    private String[] titleList = {"Exercises", "Groups", "Plans", "Routine"};
    private Fragment[] fragments;
    private Long timestamp;

    ExerciseParentPagerAdapter(FragmentManager fm, Long timestamp) {
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
            case 0:
                fragments[0] = ExerciseSelectorFragment.newInstance();
            case 1:
                fragments[1] = ExerciseGroupFragment.newInstance();
            case 2:
                fragments[2] = ExerciseRoutineFragment.newInstance(timestamp);
            case 3:
                fragments[3] = ExerciseRoutineFragment.newInstance(timestamp);
        }
        return fragments[position];
    }

    ExerciseGroupFragment getGroupFragment() {
        return (ExerciseGroupFragment) fragments[1];
    }

    @Override
    public int getCount() {
        return titleList.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList[position];
    }

}
