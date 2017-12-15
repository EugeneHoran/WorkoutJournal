package eh.workout.journal.com.workoutjournal.ui.journal;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentJournalChildBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;
import eh.workout.journal.com.workoutjournal.ui.BaseFragment;
import eh.workout.journal.com.workoutjournal.ui.factory.AppFactory;

public class JournalChildFragment extends BaseFragment implements JournalChildRecyclerAdapter.JournalRecyclerInterface {
    private static final String ARG_DATE_TIMESTAMP = "arg_date_timestamp";

    public ObservableField<Boolean> noItems = new ObservableField<>(false);

    public JournalChildFragment() {
    }

    public static JournalChildFragment newInstance(Long timestamp) {
        JournalChildFragment fragment = new JournalChildFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_DATE_TIMESTAMP, timestamp);
        fragment.setArguments(args);
        return fragment;
    }

    private Long timestamp;

    private FragmentJournalChildBinding binding;
    private JournalChildViewModel model;
    private JournalChildRecyclerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            timestamp = getArguments().getLong(ARG_DATE_TIMESTAMP);
        }
        ViewModelProvider.Factory journalChildFactory = new AppFactory(getApplicationChild(), timestamp);
        model = ViewModelProviders.of(this, journalChildFactory).get(JournalChildViewModel.class);
        getLifecycle().addObserver(model);
        adapter = new JournalChildRecyclerAdapter(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_journal_child, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        binding.recycler.setAdapter(adapter);
        observeSetsAndReps(model);
    }

    private void observeSetsAndReps(JournalChildViewModel model) {
        model.getSetAndReps().observe(this, new Observer<List<ExerciseSetRepRelation>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseSetRepRelation> dateSetRepRelations) {
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

    @Override
    public void onDeleteSetClicked(JournalSetEntity setEntity) {
        model.deleteSet(setEntity);
    }
}
