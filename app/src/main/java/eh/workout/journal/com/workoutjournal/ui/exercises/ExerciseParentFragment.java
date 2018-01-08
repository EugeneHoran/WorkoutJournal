package eh.workout.journal.com.workoutjournal.ui.exercises;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentExerciseParentBinding;
import eh.workout.journal.com.workoutjournal.ui.BaseFragment;
import eh.workout.journal.com.workoutjournal.util.AppFactory;
import eh.workout.journal.com.workoutjournal.util.Constants;
import eh.workout.journal.com.workoutjournal.util.views.CustomSearchView;

public class ExerciseParentFragment extends BaseFragment {
    private static final String ARG_DATE_TIMESTAMP = "arg_date_timestamp";
    private static final String ARG_PAGE = "arg_page";

    public ExerciseParentFragment() {
    }

    public static ExerciseParentFragment newInstance(Long timestamp, int page) {
        ExerciseParentFragment fragment = new ExerciseParentFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_DATE_TIMESTAMP, timestamp);
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectorModel = ViewModelProviders.of(this).get(ExerciseSelectorViewModel.class);
        ViewModelProviders.of(this).get(ExerciseGroupViewModel.class);
        if (getArguments() != null) {
            timestamp = getArguments().getLong(ARG_DATE_TIMESTAMP);
            page = getArguments().getInt(ARG_PAGE);
        }
        if (getActivity() != null && getActivity().getApplicationContext() != null) {
            planViewModel = ViewModelProviders.of(this, new AppFactory((JournalApplication) getActivity().getApplicationContext(), timestamp)).get(ExerciseRoutineViewModel.class);
        }
        adapter = new ExerciseParentPagerAdapter(getChildFragmentManager(), timestamp);
    }

    private FragmentExerciseParentBinding binding;
    private ExerciseSelectorViewModel selectorModel;
    private ExerciseParentPagerAdapter adapter;
    private CustomSearchView customSearchView;
    private Long timestamp;
    public int page;
    private ExerciseRoutineViewModel planViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_exercise_parent, container, false);
        customSearchView = new CustomSearchView(binding.viewToolbar != null ? binding.viewToolbar.toolbar : null);
        customSearchView.setListener(new CustomSearchView.SearchInterface() {
            @Override
            public void onSearchQuery(String query) {
                selectorModel.queryExercises(query);
            }
        });
        binding.setFragment(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.pager.setOffscreenPageLimit(4);
        binding.pager.setAdapter(adapter);
        binding.pager.setCurrentItem(0, false);
        binding.viewToolbar.tabs.setupWithViewPager(binding.pager);
        if (savedInstanceState != null) {
            if (selectorModel.searchVisible) {
                customSearchView.show();
                binding.setShowTabs(false);
                binding.viewToolbar.tabs.setVisibility(View.GONE);
                binding.pager.enableSwiping(false);
            } else {
                binding.setShowTabs(true);
            }
        } else {
            binding.setShowTabs(true);
        }

        ExerciseSelectorFragment selectorFragment = (ExerciseSelectorFragment) adapter.getItem(0);
        if (selectorFragment != null) {
            selectorFragment.setListener(new ExerciseSelectorFragment.ExerciseSelectorInterface() {
                @Override
                public void liftSelected(String exerciseId, int inputType) {
                    navToAddExerciseFragment(binding.viewToolbar.appBar, null, exerciseId, inputType, timestamp);
                }
            });
        }

        binding.pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 1) {
                    if (adapter.getGroupFragment() != null) {
                        adapter.getGroupFragment().setListener(groupInterface);
                    }
                }
            }
        });
    }

    private ExerciseGroupFragment.GroupInterface groupInterface = new ExerciseGroupFragment.GroupInterface() {
        @Override
        public void liftSelected(String exerciseId, int inputType) {
            navToAddExerciseFragment(binding.viewToolbar.appBar, null, exerciseId, inputType, timestamp);
        }
    };

    public boolean searchVisible() {
        return binding.viewToolbar.searchHolder.getVisibility() == View.VISIBLE;
    }

    public void hideSearch() {
        binding.pager.enableSwiping(true);
        selectorModel.searchVisible = false;
        customSearchView.hide();
        binding.viewToolbar.tabs.setVisibility(View.VISIBLE);
    }

    public void resetPlanFragment() {
        planViewModel.resetRoutine();
    }

    public Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.action_add_exercise:
                    if (binding.pager.getCurrentItem() == 2) {
                        navToAddPlanActivity(page, Constants.ADD_EDIT_PLAN_EXERCISE);
                    } else if (binding.pager.getCurrentItem() == 3) {
                        navToAddRoutineActivity(page, Constants.ADD_EDIT_PLAN_EXERCISE);
                    } else {
                        dialogNewExercise();
                    }
                    break;
                case R.id.action_search_exercise:
                    binding.pager.setCurrentItem(0, false);
                    binding.viewToolbar.tabs.setVisibility(View.GONE);
                    selectorModel.searchVisible = true;
                    binding.pager.enableSwiping(false);
                    customSearchView.show();
                    break;
                default:
                    return false;
            }
            return false;
        }
    };

    public View.OnClickListener navListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        }
    };

    public void onEditPlanClicked(String planId) {
        navToEditRoutineActivity(page, planId, Constants.ADD_EDIT_PLAN_EXERCISE);
    }
}
