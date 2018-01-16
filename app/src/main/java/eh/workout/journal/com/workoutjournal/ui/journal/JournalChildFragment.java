package eh.workout.journal.com.workoutjournal.ui.journal;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentJournalChildBinding;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;
import eh.workout.journal.com.workoutjournal.ui.BaseFragment;

public class JournalChildFragment extends BaseFragment {
    private static final String ARG_DATE_TIMESTAMP = "arg_date_timestamp";
    private static final String ARG_JOURNAL_PAGE = "arg_journal_page";
    public ObservableField<Boolean> noItems = new ObservableField<>(false);

    public JournalChildFragment() {
    }

    public static JournalChildFragment newInstance(Long timestamp, int page) {
        JournalChildFragment fragment = new JournalChildFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_DATE_TIMESTAMP, timestamp);
        args.putInt(ARG_JOURNAL_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    private int journalPage;
    private int parentPage;
    private FragmentJournalChildBinding binding;
    private JournalChildViewModel model;
    private JournalChildRecyclerAdapter adapterJournal;
    private JournalParentViewModel modelParent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            journalPage = getArguments().getInt(ARG_JOURNAL_PAGE);
        }
        if (getParentFragment() != null) {
            modelParent = ViewModelProviders.of(getParentFragment()).get(JournalParentViewModel.class);
        }
        model = ViewModelProviders.of(this).get(JournalChildViewModel.class);
        adapterJournal = new JournalChildRecyclerAdapter(journalRecyclerInterface);
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
//        binding.recyclerJournal.setNestedScrollingEnabled(false);
        binding.recyclerJournal.setAdapter(adapterJournal);
        observeJournalPage();
    }

    public void resetData() {
        if (modelParent.getSetAndRepsList().hasActiveObservers()) {
            modelParent.getSetAndRepsList().removeObserver(observer);
        }
        modelParent.getSetAndRepsList().observe(JournalChildFragment.this, observer);
    }

    public void observeJournalPage() {
        JournalParentFragment fragment = (JournalParentFragment) getParentFragment();
        if (fragment != null) {
            fragment.getJournalPage().observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@Nullable Integer integer) {
                    if (integer != null) {
                        parentPage = integer;
                        if (journalPage == integer) {
                            modelParent.getSetAndRepsList().observe(JournalChildFragment.this, observer);
                        } else {
                            if (modelParent.getSetAndRepsList().hasActiveObservers()) {
                                modelParent.getSetAndRepsList().removeObserver(observer);
                            }
                        }
                    }
                }
            });
        }
    }

    Observer<List<ExerciseSetRepRelation>> observer = new Observer<List<ExerciseSetRepRelation>>() {
        @Override
        public void onChanged(@Nullable List<ExerciseSetRepRelation> setRepRelations) {
            if (setRepRelations != null) {
                if (parentPage == journalPage) {
                    Collections.reverse(setRepRelations);
                    adapterJournal.setItems(setRepRelations);
                    binding.recyclerJournal.scrollToPosition(0);
                    noItems.set(setRepRelations.size() == 0);
                }
            }
        }
    };

    private JournalChildRecyclerAdapter.JournalRecyclerCallbacks journalRecyclerInterface = new JournalChildRecyclerAdapter.JournalRecyclerCallbacks() {
        @Override
        public void onSetClicked(String setId, int inputType) {
            if (getParentFragment() != null) {
                JournalParentFragment journalParentFragment = (JournalParentFragment) getParentFragment();
                journalParentFragment.onExerciseClicked(setId, inputType);
            }
        }

        @Override
        public void onDeleteSetClicked(ExerciseSetRepRelation dateSetRepRelation) {
            model.deleteSet(dateSetRepRelation);
        }
    };
}
