package eh.workout.journal.com.workoutjournal.ui.entry;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class EntryParentPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager fm;
    private String[] titleList = {"Add Set", "History"};
    private Fragment[] fragments;

    EntryParentPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
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
                fragments[0] = EntryListFragment.newInstance();
            case 1:
                fragments[1] = EntryHistoryFragment.newInstance();
        }
        return fragments[position];
    }

    Fragment[] getFragments() {
        if (fragments == null) {
            // Force creating the fragments
            int count = getCount();
            for (int i = 0; i < count; i++) {
                getItem(i);
            }
        }
        return fragments;
    }

    void setRetainedFragmentsTags(String[] tags) {
        if (tags != null && tags.length > 0) {
            fragments = new Fragment[tags.length];
            for (int i = 0; i < tags.length; i++) {
                Fragment fragment = fm.findFragmentByTag(tags[i]);
                this.fragments[i] = fragment;
                if (fragment == null) {
                    getItem(i);
                }
            }
        }
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


    EntryHistoryFragment getHistoryFragment() {
        if (getFragments() != null) {
            if (getFragments()[1] != null) {
                return (EntryHistoryFragment) getFragments()[1];
            }
        }
        return null;
    }
}
