package eh.workout.journal.com.workoutjournal.ui.journal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import eh.workout.journal.com.workoutjournal.util.Constants;
import eh.workout.journal.com.workoutjournal.util.DateHelper;


public class JournalParentPagerAdapter extends FragmentPagerAdapter {
    private JournalChildFragment[] mFragments;

    JournalParentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public JournalChildFragment getItem(int position) {
        if (mFragments != null && mFragments.length > position && mFragments[position] != null) {
            return mFragments[position];
        }
        JournalChildFragment journalFragment = JournalChildFragment.newInstance(DateHelper.getAdapterTimestamp(position), position);
        if (mFragments == null) {
            mFragments = new JournalChildFragment[getCount()];
        }
        mFragments[position] = journalFragment;
        return journalFragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return FragmentStatePagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return Constants.JOURNAL_TOTAL_PAGES_DATES;
    }

    public static Long getTimestampStatic(int page) {
        return DateHelper.getAdapterTimestamp(page);
    }

    Date getJournalDate(int page) {
        Date date = new Date();
        date.setTime(getTimestamp(page));
        return date;
    }

    Long getTimestamp(int page) {
        return DateHelper.getAdapterTimestamp(page);
    }

    String[] getTitleAndSubTitle(Context context, int page) {
        String[] titleSubTitle = new String[2];
        if (page == Constants.JOURNAL_PAGE_TODAY) {
            titleSubTitle[0] = "Today";
            titleSubTitle[1] = String.valueOf(new SimpleDateFormat("MMM dd", Locale.getDefault()).format(new Date()));
        } else if (page == Constants.JOURNAL_PAGE_TODAY - 1) {
            titleSubTitle[0] = "Yesterday";
            titleSubTitle[1] = String.valueOf(new SimpleDateFormat("MMM dd", Locale.getDefault()).format(new Date()));
        } else if (page == Constants.JOURNAL_PAGE_TODAY + 1) {
            titleSubTitle[0] = "Tomorrow";
            titleSubTitle[1] = String.valueOf(new SimpleDateFormat("MMM dd", Locale.getDefault()).format(new Date()));
        } else {
            titleSubTitle[0] = String.valueOf(DateUtils.getRelativeTimeSpanString(context, DateHelper.getAdapterTimestamp(page)));
            titleSubTitle[1] = null;
        }
        return titleSubTitle;
    }
}
