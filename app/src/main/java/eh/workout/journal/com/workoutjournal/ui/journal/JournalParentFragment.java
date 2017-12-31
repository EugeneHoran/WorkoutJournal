package eh.workout.journal.com.workoutjournal.ui.journal;


import android.annotation.SuppressLint;
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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import eh.workout.journal.com.workoutjournal.db.relations.RoutineSetRelation;
import eh.workout.journal.com.workoutjournal.ui.BaseFragment;
import eh.workout.journal.com.workoutjournal.ui.calendar.CalendarBottomSheetFragment;
import eh.workout.journal.com.workoutjournal.ui.settings.SettingsActivity;
import eh.workout.journal.com.workoutjournal.util.Constants;
import eh.workout.journal.com.workoutjournal.util.DateHelper;

public class JournalParentFragment extends BaseFragment implements JournalPlanRecyclerAdapter.PlanChildInterface, View.OnClickListener {
    private static final String ARG_DATE_PAGE = "arg_date_page";

    public ObservableField<String> toolbarTitle = new ObservableField<>("Today");
    public ObservableField<String> toolbarSubTitle = new ObservableField<>();

    public JournalParentFragment() {
    }

    public static JournalParentFragment newInstance(int page) {
        JournalParentFragment fragment = new JournalParentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DATE_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    private int datePage = Constants.JOURNAL_PAGE_TODAY;
    private int intPagerLastPage = Constants.JOURNAL_PAGE_TODAY;
    private int intPeekingHeight = 0;
    private Integer initiatedPage = null;

    private FragmentJournalParentBinding binding;
    private BottomSheetBehavior planBottomSheetBehavior;
    private JournalParentPagerAdapter dayPagerAdapter;
    private JournalPlanRecyclerAdapter planRecyclerAdapter;
    private MutableLiveData<List<RoutineSetRelation>> journalChildPlanLiveData;
    private CalendarBottomSheetFragment calendarBottomSheetFragment;
    private MenuItem todayToolbarMenuItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            datePage = getArguments().getInt(ARG_DATE_PAGE, Constants.JOURNAL_PAGE_TODAY);
        }
        dayPagerAdapter = new JournalParentPagerAdapter(getChildFragmentManager());
        planRecyclerAdapter = new JournalPlanRecyclerAdapter(this);
        intPeekingHeight = (int) getResources().getDimension(R.dimen.bottom_sheet_default_peeking_height);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_journal_parent, container, false);
        planBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);
        planBottomSheetBehavior.setPeekHeight(intPeekingHeight);
        planBottomSheetBehavior.setHideable(false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.viewToolbar.toolbar.inflateMenu(R.menu.menu_journal_parent);
        initMenu(binding.viewToolbar.toolbar.getMenu());
        binding.recyclerPlan.setNestedScrollingEnabled(false);
        binding.pager.setAdapter(dayPagerAdapter);
        binding.pager.setCurrentItem(datePage, false);
        binding.pager.addOnPageChangeListener(pageChangeListener);
        if (getActivity() != null) {
            binding.recyclerPlan.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        }
        binding.recyclerPlan.setAdapter(planRecyclerAdapter);
        updateToolbarDateChange(datePage);
        binding.addRoutine.setOnClickListener(this);
        binding.setFragment(this);
        planBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.viewClicker.setVisibility(View.GONE);
                } else {
                    binding.viewClicker.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.viewClicker.setAlpha(slideOffset);
            }
        });
        binding.viewClicker.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                planBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == binding.addRoutine) {
            navToAddRoutineActivity(binding.pager.getCurrentItem(), Constants.ADD_EDIT_PLAN_JOURNAL);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (initiatedPage == null) {
            initBottomSheet(datePage);
        }
    }


    @Override
    public void onExerciseClicked(String setId, int inputType) {
        navToAddExerciseFragment(binding.viewToolbar.appBar, setId, inputType, dayPagerAdapter.getTimestamp(binding.pager.getCurrentItem()));
    }

    @Override
    public void onEditPlanClicked(String planId) {
        navToEditPlanActivity(binding.pager.getCurrentItem(), planId, Constants.ADD_EDIT_PLAN_JOURNAL);
    }

    @SuppressWarnings("unused")
    public void onAddNewLiftClicked(View view) {
        navToSelectExerciseFragment(binding.viewToolbar.appBar, dayPagerAdapter.getTimestamp(binding.pager.getCurrentItem()), binding.pager.getCurrentItem());
    }

    ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            updateToolbarDateChange(position);
            initBottomSheet(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                planBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
    };

    /**
     * Bottom Sheet Plan
     */
    private void initBottomSheet(int position) {
        if (journalChildPlanLiveData == null) {
            journalChildPlanLiveData = dayPagerAdapter.getItem(position).getPlanSetRelation();
            journalChildPlanLiveData.observe(this, planObserver);
        } else {
            journalChildPlanLiveData = dayPagerAdapter.getItem(intPagerLastPage).getPlanSetRelation();
            if (journalChildPlanLiveData.hasActiveObservers()) {
                journalChildPlanLiveData.removeObserver(planObserver);
            }
            journalChildPlanLiveData = dayPagerAdapter.getItem(position).getPlanSetRelation();
            journalChildPlanLiveData.observe(this, planObserver);
        }
        initiatedPage = position;
        intPagerLastPage = position;
    }

    private Observer<List<RoutineSetRelation>> planObserver = new Observer<List<RoutineSetRelation>>() {
        @Override
        public void onChanged(@Nullable List<RoutineSetRelation> routineSetRelations) {
            if (routineSetRelations != null) {
                if (routineSetRelations.size() > 0) {
                    planBottomSheetBehavior.setPeekHeight(intPeekingHeight);
                    planBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    planRecyclerAdapter.setItems(routineSetRelations);
                } else {
                    planRecyclerAdapter.setItems(null);
                    planBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        }
    };

    public View.OnClickListener onBottomSheetHeaderListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            planBottomSheetBehavior.setState(
                    planBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED ?
                            BottomSheetBehavior.STATE_COLLAPSED : BottomSheetBehavior.STATE_EXPANDED);
        }
    };

    /**
     * Toolbar
     */
    public Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.action_today:
                    binding.pager.setCurrentItem(Constants.JOURNAL_PAGE_TODAY, false);
                    break;
                case R.id.action_calendar:
                    calendarBottomSheetFragment = new CalendarBottomSheetFragment();
                    calendarBottomSheetFragment.setCaldroidListener(new CaldroidListener() {
                        @Override
                        public void onSelectDate(Date date, View view) {
                            calendarBottomSheetFragment.dismiss();
                            binding.pager.setCurrentItem((Constants.JOURNAL_PAGE_TODAY - DateHelper.findDaysDiff(date.getTime(), new Date().getTime())), true);
                        }
                    });
                    showCalendarBottomSheet(calendarBottomSheetFragment, dayPagerAdapter.getAdapterDate(binding.pager.getCurrentItem()), null);
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

    private void updateToolbarDateChange(int position) {
        String[] titleSub = dayPagerAdapter.getTitleAndSubTitle(getActivity(), position);
        toolbarTitle.set(titleSub[0]);
        toolbarSubTitle.set(titleSub[1]);
        todayToolbarMenuItem.setVisible(position != Constants.JOURNAL_PAGE_TODAY);
    }

    private void initMenu(final Menu menu) {
        todayToolbarMenuItem = menu.findItem(R.id.action_today);
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
