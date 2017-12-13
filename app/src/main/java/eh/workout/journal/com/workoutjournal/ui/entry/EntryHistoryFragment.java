package eh.workout.journal.com.workoutjournal.ui.entry;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentEntryHistoryListBinding;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;


public class EntryHistoryFragment extends Fragment {

    public EntryHistoryFragment() {
    }

    public static EntryHistoryFragment newInstance() {
        return new EntryHistoryFragment();
    }

    private EntryViewModel model;
    private EntryHistoryRecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getParentFragment() != null) {
            model = ViewModelProviders.of(getParentFragment()).get(EntryViewModel.class);
        }
        adapter = new EntryHistoryRecyclerAdapter();
    }

    FragmentEntryHistoryListBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_entry_history_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recycler.setAdapter(adapter);
        observeSetReps(model);
    }

    private void observeSetReps(EntryViewModel model) {
        model.getHistory().observe(this, new Observer<List<ExerciseSetRepRelation>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseSetRepRelation> exerciseSetRepRelations) {
                if (exerciseSetRepRelations != null) {
                    binding.noItems.setVisibility(exerciseSetRepRelations.size() == 0 ? View.VISIBLE : View.GONE);
                    adapter.setItems(exerciseSetRepRelations);
                } else {
                    binding.noItems.setVisibility(View.GONE);
                }
            }
        });
    }
}
