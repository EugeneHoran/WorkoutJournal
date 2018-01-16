package eh.workout.journal.com.workoutjournal.ui.exercises;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentExerciseParentBinding;
import eh.workout.journal.com.workoutjournal.ui.BaseFragment;
import eh.workout.journal.com.workoutjournal.ui.exercises_new.ExerciseViewModel;
import eh.workout.journal.com.workoutjournal.util.AppFactory;
import eh.workout.journal.com.workoutjournal.util.Constants;

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
        postponeEnterTransition();
        selectorModel = ViewModelProviders.of(this).get(ExerciseSelectorViewModel.class);
        ViewModelProviders.of(this).get(ExerciseGroupViewModel.class);
        if (getArguments() != null) {
            timestamp = getArguments().getLong(ARG_DATE_TIMESTAMP);
            page = getArguments().getInt(ARG_PAGE);
        }
        if (getActivity() != null && getActivity().getApplicationContext() != null) {
            ViewModelProviders.of(this, new AppFactory((JournalApplication) getActivity().getApplicationContext(), timestamp)).get(ExerciseRoutineViewModel.class);
        }
        adapter = new ExerciseParentPagerAdapter(getChildFragmentManager(), timestamp);
    }

    private FragmentExerciseParentBinding binding;
    private ExerciseSelectorViewModel selectorModel;
    private ExerciseParentPagerAdapter adapter;
    private Long timestamp;
    public int page;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_exercise_parent, container, false);
        binding.setFragment(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.viewToolbar.toolbar.inflateMenu(R.menu.menu_exercise_selector);
        binding.pager.setOffscreenPageLimit(4);
        binding.pager.setAdapter(adapter);
        binding.pager.setCurrentItem(0, false);
        binding.viewToolbar.tabs.setupWithViewPager(binding.pager);
        binding.setShowTabs(true);

        ExerciseSelectorFragment selectorFragment = (ExerciseSelectorFragment) adapter.getItem(0);
        if (selectorFragment != null) {
            selectorFragment.setListener(new ExerciseSelectorFragment.ExerciseSelectorInterface() {

                @Override
                public void liftSelected(String exerciseId, int inputType) {
                    navToAddEntryFragment(binding.viewToolbar.appBar, null, exerciseId, inputType, timestamp);
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

        ExerciseViewModel exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        if (savedInstanceState == null) {
            exerciseViewModel.initData();
        }
        exerciseViewModel.observeRelationList().observe(this, new Observer<List<Object>>() {
            @Override
            public void onChanged(@Nullable List<Object> objects) {
                startPostponedEnterTransition();
            }
        });
    }

//    @Override
//    public void onEnterAnimationComplete() {
//        super.onEnterAnimationComplete();
//
//        //your code
//    }

    private ExerciseGroupFragment.GroupInterface groupInterface = new ExerciseGroupFragment.GroupInterface() {
        @Override
        public void liftSelected(String exerciseId, int inputType) {
            navToAddEntryFragment(binding.viewToolbar.appBar, null, exerciseId, inputType, timestamp);
        }
    };

    public Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.action_add_exercise:
                    if (binding.pager.getCurrentItem() == 2) {
                        navToAddPlanActivity(page, Constants.ADD_EDIT_PLAN_EXERCISE);
                    } else if (binding.pager.getCurrentItem() == 3) {
                        navToAddRoutineActivity(page, Constants.ADD_EDIT_PLAN_EXERCISE, null);
                    } else {
                        binding.pager.setCurrentItem(0);
                        if (adapter.getGroupFragment() != null) {
                            adapter.getGroupFragment().returnToGroup();
                        }
                        dialogNewExercise();
                    }
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
