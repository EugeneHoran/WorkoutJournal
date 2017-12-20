package eh.workout.journal.com.workoutjournal.ui.journal;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentJournalParentBinding;
import eh.workout.journal.com.workoutjournal.db.relations.PlanSetRelation;
import eh.workout.journal.com.workoutjournal.ui.BaseFragment;
import eh.workout.journal.com.workoutjournal.ui.calendar.CalendarBottomSheetFragment;
import eh.workout.journal.com.workoutjournal.ui.plan.AddPlanActivity;
import eh.workout.journal.com.workoutjournal.ui.settings.SettingsActivity;
import eh.workout.journal.com.workoutjournal.util.Constants;
import eh.workout.journal.com.workoutjournal.util.DateHelper;

public class JournalParentFragment extends BaseFragment implements JournalPlanRecyclerAdapter.PlanChildInterface {
    private static final String ARG_DATE_PAGE = "arg_date_page";
    private int datePage = Constants.JOURNAL_PAGE_TODAY;

    public ObservableField<String> toolbarTitle = new ObservableField<>("Today");
    public ObservableField<String> toolbarSubTitle = new ObservableField<>();
    private CalendarBottomSheetFragment caldroidFragment;

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

    public JournalParentPagerAdapter pagerAdapter;
    private JournalPlanRecyclerAdapter planAdapter;
    private MenuItem menuItemToday;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            datePage = getArguments().getInt(ARG_DATE_PAGE, Constants.JOURNAL_PAGE_TODAY);
        }
        pagerAdapter = new JournalParentPagerAdapter(getChildFragmentManager());
        planAdapter = new JournalPlanRecyclerAdapter(this);
    }

    private FragmentJournalParentBinding binding;
    BottomSheetBehavior bottomSheetBehavior;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_journal_parent, container, false);
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.viewToolbar.toolbar.inflateMenu(R.menu.menu_journal_parent);
        initMenu(binding.viewToolbar.toolbar.getMenu());
        binding.recyclerPlan.setAdapter(planAdapter);
        binding.pager.setAdapter(pagerAdapter);
        binding.pager.setCurrentItem(datePage, false);
        binding.pager.addOnPageChangeListener(pageListener);
        binding.setFragment(this);
    }

    @Override
    public void onExerciseClicked(String setId, int inputType) {
        navToAddExerciseFragment(binding.viewToolbar.appBar, setId, inputType, pagerAdapter.getTimestamp(binding.pager.getCurrentItem()));
    }

    @SuppressWarnings("unused")
    public void onAddNewLiftClicked(View view) {
        navToSelectExerciseFragment(binding.viewToolbar.appBar, pagerAdapter.getTimestamp(binding.pager.getCurrentItem()));
    }

    ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffset == 0) {
                observeData(pagerAdapter.getItem(position).getPlanSetRelation());
            } else {
                bottomSheetBehavior.setHideable(true);
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            String[] titleSub = pagerAdapter.getTitleAndSubTitle(getActivity(), position);
            toolbarTitle.set(titleSub[0]);
            toolbarSubTitle.set(titleSub[1]);
            menuItemToday.setVisible(position != Constants.JOURNAL_PAGE_TODAY);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private void observeData(MutableLiveData<List<PlanSetRelation>> mutableLiveData) {
        if (mutableLiveData.hasActiveObservers()) {
            mutableLiveData.removeObserver(observer);
        }
        mutableLiveData.observe(this, observer);
    }

    private Observer<List<PlanSetRelation>> observer = new Observer<List<PlanSetRelation>>() {
        @Override
        public void onChanged(@Nullable List<PlanSetRelation> planSetRelations) {
            if (planSetRelations != null) {
                if (planSetRelations.size() > 0) {
                    bottomSheetBehavior.setHideable(false);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    planAdapter.setItems(planSetRelations);
                }
            }
        }
    };

    public Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.action_today:
                    binding.pager.setCurrentItem(Constants.JOURNAL_PAGE_TODAY, false);
                    break;
                case R.id.action_calendar:
                    caldroidFragment = new CalendarBottomSheetFragment();
                    caldroidFragment.setCaldroidListener(calListener);
                    showCalendarBottomSheet(caldroidFragment, pagerAdapter.getAdapterDate(binding.pager.getCurrentItem()), null);
                    break;
                case R.id.action_plan:
                    if (getActivity() != null) {
                        Intent intentPlan = new Intent(getActivity(), AddPlanActivity.class);
                        intentPlan.putExtra(Constants.JOURNAL_PAGE_RESULT_CODE_PLAN, binding.pager.getCurrentItem());
                        getActivity().startActivityForResult(intentPlan, Constants.REQUEST_CODE_PLAN);
                    }
                    break;
                case R.id.action_orm:
                    navToOneRepMaxFragment(binding.viewToolbar.appBar, Constants.ORM_ONE_REP_MAX);
                    break;
                case R.id.action_percentage:
                    navToOneRepMaxFragment(binding.viewToolbar.appBar, Constants.ORM_PERCENTAGES);
                    break;
                case R.id.action_settings:
                    if (getActivity() != null) {
                        Intent intentSettings = new Intent(getActivity(), SettingsActivity.class);
                        intentSettings.putExtra(Constants.JOURNAL_PAGE_RESULT_CODE_SETTINGS, binding.pager.getCurrentItem());
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
            binding.pager.setCurrentItem((Constants.JOURNAL_PAGE_TODAY - DateHelper.findDaysDiff(date.getTime(), new Date().getTime())), true);
        }
    };

    public View.OnClickListener onBottomSheetHeaderListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            bottomSheetBehavior.setState(
                    bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED ?
                            BottomSheetBehavior.STATE_COLLAPSED : BottomSheetBehavior.STATE_EXPANDED);
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
