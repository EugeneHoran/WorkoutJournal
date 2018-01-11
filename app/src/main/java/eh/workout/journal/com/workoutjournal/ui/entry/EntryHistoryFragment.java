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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getParentFragment() != null) {
            model = ViewModelProviders.of(getParentFragment()).get(EntryHistoryViewModel.class);
        }
        adapter = new EntryHistoryRecyclerAdapter();
    }

    private EntryHistoryViewModel model;
    private EntryHistoryRecyclerAdapter adapter;
    private FragmentEntryHistoryListBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_entry_history_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recycler.setAdapter(adapter);
        observeHistory(model);
        if (savedInstanceState != null) {
            initData();
        }
    }

    public void initData() {
        model.initData();
    }

    private void observeHistory(EntryHistoryViewModel model) {
        model.getObjectList().observe(this, new Observer<List<Object>>() {
            @Override
            public void onChanged(@Nullable List<Object> objects) {

                adapter.setItems(objects);
                binding.noItems.setVisibility(objects != null && objects.size() > 0 ? View.GONE : View.VISIBLE);
            }
        });
    }
}
