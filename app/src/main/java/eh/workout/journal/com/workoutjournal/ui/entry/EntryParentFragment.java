package eh.workout.journal.com.workoutjournal.ui.entry;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentEntryParentBinding;
import eh.workout.journal.com.workoutjournal.ui.BaseFragment;
import eh.workout.journal.com.workoutjournal.util.AppFactory;
import eh.workout.journal.com.workoutjournal.util.Constants;


public class EntryParentFragment extends BaseFragment {
    private static final String FRAGMENT_TAGS = "pager_fragment_tags";


    private static final String ARG_LIFT_ID = "param_lift_id";
    public static final String ARG_LIFT_INPUT_TYPE = "param_lift_input_type";
    private static final String ARG_LIFT_TIMESTAMP = "param_lift_timestamp";

    public EntryParentFragment() {
    }

    public static EntryParentFragment newInstance(String exerciseId, int inputType, Long timestamp) {
        EntryParentFragment fragment = new EntryParentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LIFT_ID, exerciseId);
        args.putInt(ARG_LIFT_INPUT_TYPE, inputType);
        args.putLong(ARG_LIFT_TIMESTAMP, timestamp);
        fragment.setArguments(args);
        return fragment;
    }

    private EntryViewModel model;
    private EntryParentPagerAdapter adapter;
    private FragmentEntryParentBinding binding;
    private Long timestamp;
    private int inputType = 0;
    private String liftId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            timestamp = getArguments().getLong(ARG_LIFT_TIMESTAMP);
            inputType = getArguments().getInt(ARG_LIFT_INPUT_TYPE);
            liftId = getArguments().getString(ARG_LIFT_ID);
        }
        if (getActivity() != null) {
            ViewModelProvider.Factory appFactory = new AppFactory((JournalApplication) getActivity().getApplicationContext(), liftId, timestamp);
            model = ViewModelProviders.of(this, appFactory).get(EntryViewModel.class);
            ViewModelProviders.of(this, appFactory).get(EntryHistoryViewModel.class);
        }
        adapter = new EntryParentPagerAdapter(getChildFragmentManager());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_entry_parent, container, false);
        binding.viewToolbar.toolbar.inflateMenu(R.menu.menu_entry);
        binding.setModel(model);
        binding.pager.setAdapter(adapter);
        getChildFragmentManager().beginTransaction().replace(R.id.entryHolder, EntryInputFragment.newInstance(inputType)).commit();
        binding.setFragment(this);
        return binding.getRoot();
    }

    private Integer entryViewHeight;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] pagerFragmentTags = null;
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(FRAGMENT_TAGS)) {
                pagerFragmentTags = savedInstanceState.getStringArray(FRAGMENT_TAGS);
            }
        }
        adapter.setRetainedFragmentsTags(pagerFragmentTags);
        entryViewHeight = binding.entryHolder.getHeight();
        binding.viewToolbar.tabs.setupWithViewPager(binding.pager);
        binding.pager.addOnPageChangeListener(pageChangeListener);
    }

    @Override
    public void onResume() {
        binding.pager.setCurrentItem(0, false);
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        if (adapter != null && adapter.getFragments() != null) {
            Fragment[] singleDayFragments = adapter.getFragments();
            String[] tags = new String[singleDayFragments.length];
            for (int i = 0; i < tags.length; i++) {
                tags[i] = singleDayFragments[i].getTag();
            }
            outState.putStringArray(FRAGMENT_TAGS, tags);
        }
    }

    public void onExerciseClicked(String setId, int inputType, long itemTimestamp) {
        navToAddEntryFragment(binding.viewToolbar.appBar, binding.fab, setId, inputType, itemTimestamp);
    }

    public Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.action_orm:
                    navToOneRepMaxFragment(binding.viewToolbar.appBar, Constants.ORM_ONE_REP_MAX);
                    break;
                case R.id.action_percentage:
                    navToOneRepMaxFragment(binding.viewToolbar.appBar, Constants.ORM_PERCENTAGES);
                    break;
                default:
                    return false;
            }
            return false;
        }
    };

    ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (entryViewHeight == null || entryViewHeight == 0) {
                entryViewHeight = binding.entryHolder.getHeight();
            }
            binding.entryHolder.setTranslationY(-(entryViewHeight * (position + positionOffset)));
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            if (position == 1) {
                if (adapter.getHistoryFragment() != null) {
                    adapter.getHistoryFragment().initData();
                }
            }
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
}
