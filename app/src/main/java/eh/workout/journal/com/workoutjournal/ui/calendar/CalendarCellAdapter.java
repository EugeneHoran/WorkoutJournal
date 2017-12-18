package eh.workout.journal.com.workoutjournal.ui.calendar;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.Map;

import eh.workout.journal.com.workoutjournal.R;
import hirondelle.date4j.DateTime;

public class CalendarCellAdapter extends CaldroidGridAdapter {
    public CalendarCellAdapter(Context context, int month, int year, Map<String, Object> caldroidData, Map<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;
        if (convertView == null) {
            cellView = inflater.inflate(R.layout.view_calendar_cell, null);
        }
        Resources resources = context.getResources();
        DateTime dateTimeCell = this.datetimeList.get(position);
        int[] padding = getViewPadding(cellView);
        boolean isDateInMonth = dateTimeCell.getMonth() == month;
//        boolean hasLifts = getExtraData().get(String.valueOf(dateTimeCell.getMilliseconds(TimeZone.getDefault()))) != null;

        TextView tv1 = cellView.findViewById(R.id.tv1);
        View viewIndicator = cellView.findViewById(R.id.viewIndicator);
        viewIndicator.setVisibility(View.INVISIBLE);
        // Remove previous selected date
        if (getSelectedDates().size() > 1) {
            getSelectedDates().remove(0);
        }

        // Init Views
        tv1.setText(String.valueOf(dateTimeCell.getDay())); // Num Day of Month
//        viewIndicator.setVisibility(hasLifts ? View.VISIBLE : View.INVISIBLE); // Indicator for existing lifts
        if (dateTimeCell.getMonth() != month) {// Set color of the dates in previous / next month
            tv1.setTextColor(resources.getColor(com.caldroid.R.color.caldroid_lighter_gray));
        }
        if (dateTimeCell.equals(getToday())) {
            if (isDateInMonth) {
                tv1.setBackgroundResource(R.drawable.circle_gray);
                tv1.setTextColor(resources.getColor(R.color.white));
            }
        } else {
            cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
        }
        if (getSelectedDates().size() != 0) {
            DateTime selectedDateTime = getSelectedDates().get(0).getStartOfDay();

            if (selectedDateTime.equals(dateTimeCell.getStartOfDay())) {
                if (!selectedDateTime.equals(getToday().getStartOfDay())) {
                    tv1.setBackgroundResource(R.drawable.circle_gray_transparent);
                }
            } else {
                if (!dateTimeCell.equals(getToday())) {
                    tv1.setBackground(null);
                }
            }
        } else {
            if (!dateTimeCell.equals(getToday())) {
                tv1.setBackground(null);
            }
        }

        cellView.setPadding(padding[0], padding[1], padding[2], padding[3]);
        setCustomResources(dateTimeCell, cellView, tv1);
        return cellView;
    }

    private int[] getViewPadding(View view) {
        int[] padding = new int[4];
        padding[0] = view.getPaddingTop();
        padding[1] = view.getPaddingLeft();
        padding[2] = view.getPaddingBottom();
        padding[3] = view.getPaddingRight();
        return padding;
    }
}

