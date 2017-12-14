package eh.workout.journal.com.workoutjournal.ui.entry;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentEntryParentBinding;
import eh.workout.journal.com.workoutjournal.ui.factory.AppFactory;


public class EntryParentFragment extends Fragment {
    private static final String ARG_LIFT_ID = "param_lift_id";
    private static final String ARG_LIFT_TIMESTAMP = "param_lift_timestamp";

    public EntryParentFragment() {
    }

    public static EntryParentFragment newInstance(String exerciseId, Long timestamp) {
        EntryParentFragment fragment = new EntryParentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LIFT_ID, exerciseId);
        args.putLong(ARG_LIFT_TIMESTAMP, timestamp);
        fragment.setArguments(args);
        return fragment;
    }

    private EntryViewModelNew model;
    private FragmentEntryParentBinding binding;
    private Long timestamp;
    private String liftId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            timestamp = getArguments().getLong(ARG_LIFT_TIMESTAMP);
            liftId = getArguments().getString(ARG_LIFT_ID);
        }
        if (getActivity() != null) {
            ViewModelProvider.Factory appFactory = new AppFactory((JournalApplication) getActivity().getApplicationContext(), liftId, timestamp);
            model = ViewModelProviders.of(this, appFactory).get(EntryViewModelNew.class);
            ViewModelProviders.of(this, appFactory).get(EntryHistoryViewModel.class);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_entry_parent, container, false);
        getChildFragmentManager().beginTransaction().replace(R.id.entryHolder, EntryInputFragment.newInstance()).commit();
        binding.setModel(model);
        binding.setFragment(this);
        return binding.getRoot();
    }

    private Integer entryViewHeight;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.pager.setAdapter(new EntryParentPagerAdapter(getChildFragmentManager()));
        binding.viewToolbar.tabs.setupWithViewPager(binding.pager);
        entryViewHeight = binding.entryHolder.getHeight();
        binding.pager.addOnPageChangeListener(pageChangeListener);
    }


    ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (entryViewHeight == null || entryViewHeight == 0) {
                entryViewHeight = binding.entryHolder.getHeight();
            }
            binding.entryHolder.setTranslationY(-(entryViewHeight * (position + positionOffset)));
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
