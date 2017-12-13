package eh.workout.journal.com.workoutjournal.ui.journal;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentJournalChildBinding;
import eh.workout.journal.com.workoutjournal.db.relations.DateSetRepRelation;

public class JournalChildFragment extends Fragment implements JournalChildRecyclerAdapter.JournalRecyclerInterface {
    private static final String ARG_DATE_TIMESTAMP = "arg_date_timestamp";
    private Long timestamp;
    private FragmentJournalChildBinding binding;
    public ObservableField<Boolean> noItems = new ObservableField<>(true);

    public JournalChildFragment() {
    }

    public static JournalChildFragment newInstance(Long timestamp) {
        JournalChildFragment fragment = new JournalChildFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_DATE_TIMESTAMP, timestamp);
        fragment.setArguments(args);
        return fragment;
    }

    private JournalChildViewModel model;
    private JournalChildRecyclerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            timestamp = getArguments().getLong(ARG_DATE_TIMESTAMP);
        }
        ViewModelProvider.Factory journalFactory = new JournalChildViewModel.JournalViewModelFactory((JournalApplication) getParentFragment().getActivity().getApplicationContext(), timestamp);
        model = ViewModelProviders.of(this, journalFactory).get(JournalChildViewModel.class);
        adapter = new JournalChildRecyclerAdapter();
        adapter.setListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_journal_child, container, false);
        binding.setFragment(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recycler.setAdapter(adapter);
        observeSetsAndReps(model);
    }

    private void observeSetsAndReps(JournalChildViewModel model) {
        model.observeDatesAndTimes().observe(this, new Observer<List<DateSetRepRelation>>() {
            @Override
            public void onChanged(@Nullable List<DateSetRepRelation> dateSetRepRelations) {
                if (dateSetRepRelations != null) {
                    adapter.setItems(dateSetRepRelations);
                    noItems.set(dateSetRepRelations.size() == 0);
                }
            }
        });
    }

    @Override
    public void onWorkoutClicked(String setId) {
        if (getParentFragment() != null) {
            JournalParentFragment journalParentFragment = (JournalParentFragment) getParentFragment();
            journalParentFragment.onExerciseClicked(setId);
        }
    }
}
