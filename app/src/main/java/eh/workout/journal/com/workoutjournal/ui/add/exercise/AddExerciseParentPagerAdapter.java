package eh.workout.journal.com.workoutjournal.ui.add.exercise;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AddExerciseParentPagerAdapter extends FragmentPagerAdapter {
    private String[] titleList = {"Add Set", "History"};
    private Fragment[] fragments;
    private FragmentManager fragmentManager;

    AddExerciseParentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentManager = fm;
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
                fragments[0] = AddExerciseEntryFragment.newInstance();
            case 1:
                fragments[1] = AddExerciseHistoryFragment.newInstance();
        }
        return fragments[position];
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
