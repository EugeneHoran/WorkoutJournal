package eh.workout.journal.com.workoutjournal.ui.journal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import eh.workout.journal.com.workoutjournal.util.DateHelper;


public class JournalParentPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager fragmentManager;
    private JournalChildFragment[] mFragments;

    public JournalParentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
    }

    @Override
    public Fragment getItem(int position) {
        if (mFragments != null && mFragments.length > position && mFragments[position] != null) {
            return mFragments[position];
        }
        JournalChildFragment journalFragment = JournalChildFragment.newInstance(DateHelper.getAdapterTimestamp(position));
        if (mFragments == null) {
            mFragments = new JournalChildFragment[getCount()];
        }
        mFragments[position] = journalFragment;
        return journalFragment;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return FragmentStatePagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return 10000;
    }

    public Long getTimestamp(int page) {
        return DateHelper.getAdapterTimestamp(page);
    }

    public String[] getTitleAndSubTitle(Context context, int page) {
        String[] titleSubTitle = new String[2];
        if (page == 5000) {
            titleSubTitle[0] = "Today";
            titleSubTitle[1] = String.valueOf(new SimpleDateFormat("MMM dd", Locale.getDefault()).format(new Date()));
        } else {
            titleSubTitle[0] = getDayFormatted(context, page);
            titleSubTitle[1] = null;
        }
        return titleSubTitle;
    }

    private String getDayFormatted(Context context, int page) {
        CharSequence charSequence = DateUtils.getRelativeTimeSpanString(context, DateHelper.getAdapterTimestamp(page));
        StringBuilder sb = new StringBuilder(charSequence.length());
        sb.append(charSequence);
        return sb.toString();
    }
}
