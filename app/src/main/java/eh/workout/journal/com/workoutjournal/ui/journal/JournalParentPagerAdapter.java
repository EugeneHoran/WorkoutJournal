package eh.workout.journal.com.workoutjournal.ui.journal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
        JournalChildFragment journalFragment = JournalChildFragment.newInstance(DateHelper.getAdapterTimestamp(position));
        if (mFragments == null) {
            mFragments = new JournalChildFragment[getCount()];
        }
        mFragments[position] = journalFragment;
        return journalFragment;
    }

    Date getAdapterDate(int dayPosition) {
        int dayDiff = dayPosition - Constants.JOURNAL_PAGE_TODAY;
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Calendar mCalendar = new GregorianCalendar(year, month, day);
        mCalendar.add(Calendar.DATE, dayDiff);
        return mCalendar.getTime();
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

    Long getTimestamp(int page) {
        return DateHelper.getAdapterTimestamp(page);
    }

    String[] getTitleAndSubTitle(Context context, int page) {
        String[] titleSubTitle = new String[2];
        if (page == Constants.JOURNAL_PAGE_TODAY) {
            titleSubTitle[0] = "Today";
            titleSubTitle[1] = String.valueOf(new SimpleDateFormat("MMM dd", Locale.getDefault()).format(new Date()));
        } else {
            titleSubTitle[0] = String.valueOf(DateUtils.getRelativeTimeSpanString(context, DateHelper.getAdapterTimestamp(page)));
            titleSubTitle[1] = null;
        }
        return titleSubTitle;
    }
}
