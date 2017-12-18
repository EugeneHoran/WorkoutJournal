package eh.workout.journal.com.workoutjournal.ui.calendar;

import com.roomorama.caldroid.CaldroidGridAdapter;


public class CalendarBottomSheetFragment extends CaldroidBottomSheetFragment {

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        return new CalendarCellAdapter(getActivity(), month, year, getCaldroidData(), extraData);
    }
}
