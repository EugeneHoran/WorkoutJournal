package eh.workout.journal.com.workoutjournal.ui.calendar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.ui.journal.JournalParentPagerAdapter;
import eh.workout.journal.com.workoutjournal.util.DateHelper;

public class CalendarBottomSheetFragment extends BottomSheetDialogFragment {
    private static final String ARG_DATE_TIMESTAMP = "arg_date_timestamp";
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

    public CalendarBottomSheetFragment() {
    }

    public static CalendarBottomSheetFragment newInstance(long timestamp) {
        CalendarBottomSheetFragment fragment = new CalendarBottomSheetFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_DATE_TIMESTAMP, timestamp);
        fragment.setArguments(args);
        return fragment;
    }

    private CalendarCallbacks listener;

    public void setListener(CalendarCallbacks listener) {
        this.listener = listener;
    }

    public interface CalendarCallbacks {
        void onDateSelected(int page);
    }

    private long timestamp;
    private TextView txtDate;
    private CompactCalendarView compactCalendarView;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        if (getArguments() != null) {
            timestamp = getArguments().getLong(ARG_DATE_TIMESTAMP);
        }
        View v = View.inflate(getContext(), R.layout.fragment_calendar, null);
        if (getActivity() != null) {
            CalendarViewModel model = ViewModelProviders.of(getActivity()).get(CalendarViewModel.class);
            model.initData();
            initDateData(model);
        }
        txtDate = v.findViewById(R.id.date);
        compactCalendarView = v.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setCurrentDate(new Date(timestamp));
        compactCalendarView.setUseThreeLetterAbbreviation(false);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.displayOtherMonthDays(false);
        compactCalendarView.setCurrentDayTextColor(getResources().getColor(R.color.white));
        compactCalendarView.setCurrentDayBackgroundColor(getResources().getColor(R.color.colorAccent));
        if (DateUtils.isToday(timestamp)) {
            compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.colorGrayNormal));
        }
        compactCalendarView.setUseThreeLetterAbbreviation(false);
        compactCalendarView.shouldDrawIndicatorsBelowSelectedDays(true);
        txtDate.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                if (listener != null) {
                    listener.onDateSelected(5000 - DateHelper.findDaysDiff(dateClicked.getTime(), JournalParentPagerAdapter.getTimestampStatic(5000)));
                }
                listener = null;
                getDialog().dismiss();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                txtDate.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });
        dialog.setContentView(v);
    }

    private void initDateData(CalendarViewModel model) {
        model.getSetDates().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                compactCalendarView.removeAllEvents();
                compactCalendarView.addEvents(events);
                compactCalendarView.invalidate();
            }
        });
    }
}
