package eh.workout.journal.com.workoutjournal.ui.exercises;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class ExerciseParentPagerAdapter extends FragmentPagerAdapter {
    private String[] titleList = {"Routines", "Exercises", "Groups"};
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
                fragments[0] = ExercisePlanFragment.newInstance(timestamp);
            case 1:
                fragments[1] = ExerciseSelectorFragment.newInstance();
            case 2:
                fragments[2] = ExerciseGroupFragment.newInstance();
        }
        return fragments[position];
    }

    public ExerciseGroupFragment getGroupFragment() {
        return (ExerciseGroupFragment) fragments[2];
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
