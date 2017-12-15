package eh.workout.journal.com.workoutjournal.ui.journal;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentJournalParentBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalDateEntity;
import eh.workout.journal.com.workoutjournal.ui.BaseFragment;
import eh.workout.journal.com.workoutjournal.ui.calendar.CalendarBottomSheetFragment;
import eh.workout.journal.com.workoutjournal.ui.settings.SettingsActivity;
import eh.workout.journal.com.workoutjournal.util.Constants;
import eh.workout.journal.com.workoutjournal.util.DateHelper;

public class JournalParentFragment extends BaseFragment {
    private static final String ARG_DATE_PAGE = "arg_date_page";
    private int datePage = Constants.PAGE_TODAY;


    public ObservableField<String> toolbarTitle = new ObservableField<>("Today");
    public ObservableField<String> toolbarSubTitle = new ObservableField<>();
    private CalendarBottomSheetFragment caldroidFragment;
    private List<JournalDateEntity> dateEntities = new ArrayList<>();

    public JournalParentFragment() {
    }

    public static JournalParentFragment newInstance(int page) {
        JournalParentFragment fragment = new JournalParentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DATE_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    public static JournalParentFragment newInstance() {
        return new JournalParentFragment();
    }

    public JournalParentPagerAdapter journalPagerAdapter;
    private MenuItem menuItemToday;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            datePage = getArguments().getInt(ARG_DATE_PAGE, Constants.PAGE_TODAY);
        }
        journalPagerAdapter = new JournalParentPagerAdapter(getChildFragmentManager());
    }

    private FragmentJournalParentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_journal_parent, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.viewToolbar.toolbar.inflateMenu(R.menu.menu_journal_parent);
        initMenu(binding.viewToolbar.toolbar.getMenu());
        binding.pager.setAdapter(journalPagerAdapter);
        binding.pager.addOnPageChangeListener(pageChangeListener);
        binding.pager.setCurrentItem(datePage, false);
        binding.setFragment(this);
    }

    public void onExerciseClicked(String setId) {
        navToAddExerciseFragment(binding.viewToolbar.appBar, setId, journalPagerAdapter.getTimestamp(binding.pager.getCurrentItem()));
    }

    @SuppressWarnings("unused")
    public void onAddNewLiftClicked(View view) {
        navToSelectExerciseFragment(binding.viewToolbar.appBar, journalPagerAdapter.getTimestamp(binding.pager.getCurrentItem()));
    }

    private ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            String[] titleSub = journalPagerAdapter.getTitleAndSubTitle(getActivity(), position);
            toolbarTitle.set(titleSub[0]);
            toolbarSubTitle.set(titleSub[1]);
            menuItemToday.setVisible(position != 5000);
        }
    };

    public Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.action_today:
                    binding.pager.setCurrentItem(5000, true);
                    break;
                case R.id.action_calendar:
                    caldroidFragment = new CalendarBottomSheetFragment();
                    caldroidFragment.setCaldroidListener(calListener);
                    showCalendarBottomSheet(caldroidFragment, journalPagerAdapter.getAdapterDate(binding.pager.getCurrentItem()), null);
                    break;
                case R.id.action_orm:
                    break;
                case R.id.action_settings:
                    if (getActivity() != null) {
                        Intent intentSettings = new Intent(getActivity(), SettingsActivity.class);
                        intentSettings.putExtra(Constants.PAGE_RESULT_CODE_SETTINGS, binding.pager.getCurrentItem());
                        getActivity().startActivityForResult(intentSettings, Constants.REQUEST_CODE_SETTINGS);
                    }
                    break;
                default:
                    return false;
            }
            return false;
        }
    };

    final CaldroidListener calListener = new CaldroidListener() {
        @Override
        public void onSelectDate(Date date, View view) {
            caldroidFragment.dismiss();
            binding.pager.setCurrentItem((5000 - DateHelper.findDaysDiff(date.getTime(), new Date().getTime())), true);
        }
    };

    private void initMenu(final Menu menu) {
        menuItemToday = menu.findItem(R.id.action_today);
        final MenuItem itemCal = menu.findItem(R.id.action_calendar);
        FrameLayout rootView = (FrameLayout) itemCal.getActionView();
        TextView dayOfMonth = rootView.findViewById(R.id.dayOfMonth);
        dayOfMonth.setText(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
        itemCal.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.performIdentifierAction(itemCal.getItemId(), 0);
            }
        });
    }
}
