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
import eh.workout.journal.com.workoutjournal.db.entinty.PlanDayEntity;
import eh.workout.journal.com.workoutjournal.ui.BaseFragment;
import eh.workout.journal.com.workoutjournal.ui.settings.SettingsActivity;
import eh.workout.journal.com.workoutjournal.util.Constants;
import eh.workout.journal.com.workoutjournal.util.views.LayoutUtil;

public class JournalParentFragment extends BaseFragment implements View.OnClickListener {
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
    private BottomSheetBehavior bsSheetBehavior;
    private JournalParentPagerAdapter journalPagerAdapter;
    private JournalRoutinePlanRecyclerAdapter routinePlanRecyclerAdapter;


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
        journalPagerAdapter = new JournalParentPagerAdapter(getChildFragmentManager());
        routinePlanRecyclerAdapter = new JournalRoutinePlanRecyclerAdapter(routineInterface);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_journal_parent, container, false);
        bsSheetBehavior = BottomSheetBehavior.from(binding.bottom.bsHolder);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.viewToolbar.toolbar.inflateMenu(R.menu.menu_journal_parent);
        initMenu(binding.viewToolbar.toolbar.getMenu());
        binding.bottom.bsRecyclerRoutinePlan.setNestedScrollingEnabled(false);
        if (getActivity() != null) {
            binding.bottom.bsRecyclerRoutinePlan.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        }
        binding.pager.setAdapter(journalPagerAdapter);
        binding.pager.setCurrentItem(datePage, false);
        binding.pager.addOnPageChangeListener(pageChangeListener);
        updateToolbarDateChange(datePage);
        binding.setFragment(this);
        binding.bottom.bsRecyclerRoutinePlan.setAdapter(routinePlanRecyclerAdapter);
        bsSheetBehavior.setBottomSheetCallback(bottomSheetCallback);
        binding.bottom.bsBtnAddRoutine.setOnClickListener(this);
        binding.bottom.bsBtnAddPlan.setOnClickListener(this);
        binding.bottom.bsFabSeparator.setOnClickListener(this);
        binding.bottom.bsOpenClose.setOnClickListener(this);
        binding.fab.setOnClickListener(this);
        getJournalPage().setValue(datePage);
        if (savedInstanceState == null) {
            model.initJournalData(journalPagerAdapter.getTimestamp(datePage));
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
            model.initJournalData(journalPagerAdapter.getTimestamp(position));
            getJournalPage().setValue(position);
            updateToolbarDateChange(position);
        }
    };

    @Override
    public void onClick(View view) {
        if (view == binding.fab) {
            navToSelectExerciseFragment(getAppBar(), journalPagerAdapter.getTimestamp(getPage()), getPage());
        } else if (view == binding.bottom.bsOpenClose) {
            bsSheetBehavior.setState(
                    bsSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED ?
                            BottomSheetBehavior.STATE_COLLAPSED : BottomSheetBehavior.STATE_EXPANDED);
        } else if (view == binding.bottom.bsFabSeparator) {
            bsSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (view == binding.bottom.bsBtnAddRoutine) {
            navToAddRoutineActivity(getPage(), Constants.ADD_EDIT_PLAN_JOURNAL, binding.fab);
        } else if (view == binding.bottom.bsBtnAddPlan) {
            navToAddPlanActivity(getPage(), Constants.ADD_EDIT_PLAN_JOURNAL);
        }
    }

/*__________________Bottom Sheet Imp___________________________*/

    /**
     * Bottom Sheet Plan
     */
    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            binding.bottom.bsTransDismissView.setVisibility(newState == BottomSheetBehavior.STATE_COLLAPSED ? View.GONE : View.VISIBLE);
            binding.bottom.bsTransDismissView.setOnTouchListener(newState == BottomSheetBehavior.STATE_COLLAPSED ? null : getTouchListener());
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            float animAppBar = slideOffset * getAppBar().getHeight();
            getAppBar().setTranslationY(-animAppBar);
            binding.pager.setTranslationY(-animAppBar);
            binding.bottom.bsOpenCloseIndicator.setRotation(slideOffset * 180);
            binding.bottom.bsTransDismissView.setAlpha(slideOffset);
        }
    };

    private void observeRoutinePlansComplete(JournalParentViewModel model) {
        model.getRoutinePlanListComplete().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null) {
                    if (aBoolean) {
                        binding.bottom.bsRoutinePlanIndicator.setImageDrawable(new LayoutUtil().getDrawableMutate(getActivity(), R.drawable.ic_check_circle, R.color.colorAccent));
                    } else {
                        binding.bottom.bsRoutinePlanIndicator.setImageResource(R.drawable.circle_blue);
                    }
                }
            }
        });
    }

    private void observeRoutinePlan(JournalParentViewModel model) {
        model.getRoutinePlanList().observe(this, new Observer<List<Object>>() {
            @Override
            public void onChanged(@Nullable List<Object> routineSetRelations) {
                binding.bottom.bsRoutinePlanIndicator.setVisibility(routineSetRelations != null && routineSetRelations.size() > 0 ? View.VISIBLE : View.GONE);
                routinePlanRecyclerAdapter.setItems(routineSetRelations);
                showRoutinePlan = routineSetRelations != null && showRoutinePlan && routineSetRelations.size() > 0;
                bsSheetBehavior.setState(showRoutinePlan ? BottomSheetBehavior.STATE_EXPANDED : BottomSheetBehavior.STATE_COLLAPSED);
                showRoutinePlan = false;
            }
        });
    }

    private View.OnTouchListener getTouchListener() {
        return new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                bsSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                binding.bottom.bsTransDismissView.setOnTouchListener(null);
                return true;
            }
        };
    }

    public JournalRoutinePlanRecyclerAdapter.RoutinePlanCallbacks routineInterface = new JournalRoutinePlanRecyclerAdapter.RoutinePlanCallbacks() {
        @Override
        public void onExerciseClicked(String setId, int inputType) {
            navToAddExerciseFragment(getAppBar(), binding.fab, setId, inputType, journalPagerAdapter.getTimestamp(getPage()));
        }

        @Override
        public void editRoutine(String planId) {
            navToEditRoutineActivity(getPage(), planId, Constants.ADD_EDIT_PLAN_JOURNAL);
        }

        @Override
        public void deletePlan(PlanDayEntity planDayEntity) {
            showRoutinePlan = true;
            model.deletePlanDayEntity(planDayEntity);
        }

        @Override
        public void editPlan(String planId) {
            navToEditPlanActivity(getPage(), Constants.ADD_EDIT_PLAN_JOURNAL, planId);
        }
    };

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
        String[] titleSub = journalPagerAdapter.getTitleAndSubTitle(getActivity(), position);
        toolbarTitle.set(titleSub[0]);
        toolbarSubTitle.set(titleSub[1]);
        binding.viewToolbar.toolbar.getMenu().findItem(R.id.action_today).setVisible(position != Constants.JOURNAL_PAGE_TODAY);
    }

    private void initMenu(final Menu menu) {
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
