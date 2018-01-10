package eh.workout.journal.com.workoutjournal.ui.journal;


import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentJournalParentBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanDayEntity;
import eh.workout.journal.com.workoutjournal.ui.BaseFragment;
import eh.workout.journal.com.workoutjournal.ui.settings.SettingsActivity;
import eh.workout.journal.com.workoutjournal.util.Constants;
import eh.workout.journal.com.workoutjournal.util.views.LayoutUtil;

public class JournalParentFragment extends BaseFragment implements View.OnClickListener, JournalRoutinePlanRecyclerAdapter.PlanChildInterface {
    private static final String ARG_DATE_PAGE = "arg_date_page";
    private static final String ARG_SHOW_ROUTINE_PLAN = "arg_show_routine_plan";
    public ObservableField<String> toolbarTitle = new ObservableField<>("Today");
    public ObservableField<String> toolbarSubTitle = new ObservableField<>();
    private MutableLiveData<Integer> journalPage;
    private int datePage;
    private boolean showRoutinePlan = false;

    public JournalParentFragment() {
    }

    public static JournalParentFragment newInstance(int page, boolean showRoutinePlan) {
        JournalParentFragment fragment = new JournalParentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DATE_PAGE, page);
        args.putBoolean(ARG_SHOW_ROUTINE_PLAN, showRoutinePlan);
        fragment.setArguments(args);
        return fragment;
    }

    private JournalParentViewModel model;
    private FragmentJournalParentBinding binding;
    private BottomSheetBehavior planBottomSheetBehavior;
    private JournalParentPagerAdapter dayPagerAdapter;
    private JournalRoutinePlanRecyclerAdapter routinePlanRecyclerAdapter;
    private MenuItem todayToolbarMenuItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            datePage = getArguments().getInt(ARG_DATE_PAGE, Constants.JOURNAL_PAGE_TODAY);
        }
        if (savedInstanceState == null) {
            showRoutinePlan = getArguments().getBoolean(ARG_SHOW_ROUTINE_PLAN);
        }
        model = ViewModelProviders.of(this).get(JournalParentViewModel.class);
        dayPagerAdapter = new JournalParentPagerAdapter(getChildFragmentManager());
        routinePlanRecyclerAdapter = new JournalRoutinePlanRecyclerAdapter(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_journal_parent, container, false);
        planBottomSheetBehavior = BottomSheetBehavior.from(binding.bottom.bottomSheet);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.viewToolbar.toolbar.inflateMenu(R.menu.menu_journal_parent);
        initMenu(binding.viewToolbar.toolbar.getMenu());
        binding.bottom.recyclerPlan.setNestedScrollingEnabled(false);
        if (getActivity() != null) {
            binding.bottom.recyclerPlan.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        }
        binding.pager.setAdapter(dayPagerAdapter);
        binding.pager.setCurrentItem(datePage, false);
        binding.pager.addOnPageChangeListener(pageChangeListener);
        updateToolbarDateChange(datePage);
        binding.setFragment(this);
        binding.bottom.recyclerPlan.setAdapter(routinePlanRecyclerAdapter);
        planBottomSheetBehavior.setBottomSheetCallback(bottomSheetCallback);
        binding.bottom.addRoutine.setOnClickListener(this);
        binding.bottom.addPlan.setOnClickListener(this);
        binding.bottom.viewClicker2.setOnClickListener(this);
        getJournalPage().setValue(datePage);
        if (savedInstanceState == null) {
            model.initJournalData(dayPagerAdapter.getTimestamp(datePage));
        }
        observeRoutinePlan(model);
        observeRoutinePlansComplete(model);
    }

    public MutableLiveData<Integer> getJournalPage() {
        if (journalPage == null) {
            journalPage = new MutableLiveData<>();
        }
        return journalPage;
    }

    ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            observeRoutinePlan(model);
            model.initJournalData(dayPagerAdapter.getTimestamp(position));
            getJournalPage().setValue(position);
            updateToolbarDateChange(position);
        }
    };

    @Override
    public void onClick(View view) {
        if (view == binding.bottom.addRoutine) {
            navToAddRoutineActivity(getPage(), Constants.ADD_EDIT_PLAN_JOURNAL);
        } else if (view == binding.bottom.addPlan) {
            navToAddPlanActivity(getPage(), Constants.ADD_EDIT_PLAN_JOURNAL);
        } else if (view == binding.bottom.viewClicker2) {
            planBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @SuppressWarnings("unused")
    public void onAddNewLiftClicked(View view) {
        navToSelectExerciseFragment(getAppBar(), dayPagerAdapter.getTimestamp(getPage()), getPage());
    }

/*__________________Bottom Sheet Imp___________________________*/

    /**
     * Bottom Sheet Plan
     */
    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            binding.bottom.viewClicker.setVisibility(newState == BottomSheetBehavior.STATE_COLLAPSED ? View.GONE : View.VISIBLE);
            binding.bottom.viewClicker.setOnTouchListener(newState == BottomSheetBehavior.STATE_COLLAPSED ? null : touchListener);
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            float animAppBar = slideOffset * getAppBar().getHeight();
            getAppBar().setTranslationY(-animAppBar);
            binding.pager.setTranslationY(-animAppBar);
            binding.bottom.rotateImage.setRotation(slideOffset * 180);
            binding.bottom.viewClicker.setAlpha(slideOffset);
        }
    };

    private void observeRoutinePlansComplete(JournalParentViewModel model) {
        model.getRoutinePlanListComplete().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null) {
                    if (aBoolean) {
                        binding.bottom.hasRoutinePlanIndicator.setImageDrawable(new LayoutUtil().getDrawableMutate(getActivity(), R.drawable.ic_check_circle, R.color.colorAccent));
                    } else {
                        binding.bottom.hasRoutinePlanIndicator.setImageResource(R.drawable.circle_blue);
                    }
                }
            }
        });
    }

    private void observeRoutinePlan(JournalParentViewModel model) {
        model.getRoutinePlanList().observe(this, new Observer<List<Object>>() {
            @Override
            public void onChanged(@Nullable List<Object> routineSetRelations) {
                binding.bottom.hasRoutinePlanIndicator.setVisibility(routineSetRelations != null && routineSetRelations.size() > 0 ? View.VISIBLE : View.GONE);
                routinePlanRecyclerAdapter.setItems(routineSetRelations);
                showRoutinePlan = routineSetRelations != null && showRoutinePlan && routineSetRelations.size() > 0;
                planBottomSheetBehavior.setState(showRoutinePlan ? BottomSheetBehavior.STATE_EXPANDED : BottomSheetBehavior.STATE_COLLAPSED);
                showRoutinePlan = false;
            }
        });
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            planBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            binding.bottom.viewClicker.setOnTouchListener(null);
            return true;
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

    @Override
    public void onEditPlanClicked(String planId) {
        navToEditPlanActivity(getPage(), Constants.ADD_EDIT_PLAN_JOURNAL, planId);
    }

    @Override
    public void onExerciseClicked(String setId, int inputType) {
        navToAddExerciseFragment(getAppBar(), binding.fab, setId, inputType, dayPagerAdapter.getTimestamp(getPage()));
    }

    @Override
    public void onEditRoutineClicked(String planId) {
        navToEditRoutineActivity(getPage(), planId, Constants.ADD_EDIT_PLAN_JOURNAL);
    }

    @Override
    public void onDeleteRoutine(PlanDayEntity planDayEntity) {
        showRoutinePlan = true;
        model.deletePlanDayEntity(planDayEntity);
    }
    /*__________________Toolbar Menu Imp___________________________*/

    public Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.action_today:
                    binding.pager.setCurrentItem(Constants.JOURNAL_PAGE_TODAY, false);
                    break;
                case R.id.action_calendar:

                    break;
                case R.id.action_orm:
                    navToOneRepMaxFragment(getAppBar(), Constants.ORM_ONE_REP_MAX);
                    break;
                case R.id.action_percentage:
                    navToOneRepMaxFragment(getAppBar(), Constants.ORM_PERCENTAGES);
                    break;
                case R.id.action_settings:
                    if (getActivity() != null) {
                        Intent intentSettings = new Intent(getActivity(), SettingsActivity.class);
                        intentSettings.putExtra(Constants.JOURNAL_PAGE_RESULT_CODE_SETTINGS, getPage());
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

    private int getPage() {
        return binding.pager.getCurrentItem();
    }

    private AppBarLayout getAppBar() {
        return binding.viewToolbar.appBar;
    }
}
