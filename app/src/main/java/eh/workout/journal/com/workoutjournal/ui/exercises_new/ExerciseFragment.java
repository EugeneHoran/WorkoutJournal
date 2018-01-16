package eh.workout.journal.com.workoutjournal.ui.exercises_new;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentExerciseBinding;

public class ExerciseFragment extends Fragment {
    private static final String ARG_DATE_TIMESTAMP = "arg_date_timestamp";
    private static final String ARG_PAGE = "arg_page";

    public ExerciseFragment() {
    }

    public static ExerciseFragment newInstance(Long timestamp, int page) {
        ExerciseFragment fragment = new ExerciseFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_DATE_TIMESTAMP, timestamp);
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    private Long timestamp;
    public int page;
    private ExerciseViewModel model;
    ExercisePagerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            timestamp = getArguments().getLong(ARG_DATE_TIMESTAMP);
            page = getArguments().getInt(ARG_PAGE);
        }
    }

    private FragmentExerciseBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_exercise, container, false);
        binding.setShowTabs(true);
        binding.setFragment(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        adapter = new ExercisePagerAdapter(getChildFragmentManager(), timestamp);
        binding.pager.setAdapter(adapter);
        binding.viewToolbar.tabs.setupWithViewPager(binding.pager);
        observeObjectData(model);
    }

    private void observeObjectData(ExerciseViewModel model) {
        model.observeRelationList().observe(this, new Observer<List<Object>>() {
            @Override
            public void onChanged(@Nullable List<Object> objects) {

            }
        });
    }


    public View.OnClickListener navListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        }
    };

    public Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.action_add_exercise:


                    break;
                default:
                    return false;
            }
            return false;
        }
    };

}