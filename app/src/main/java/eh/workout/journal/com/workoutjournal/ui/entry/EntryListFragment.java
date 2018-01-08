package eh.workout.journal.com.workoutjournal.ui.entry;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentEntryListBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;


public class EntryListFragment extends Fragment implements EntryListRecyclerAdapter.EntryAdapterInterface, EntryEditDialogFragment.EditRepInterface {
    private static final String TAG_EDIT_REP_DIALOG_FRAGMENT = "tag_edit_rep_frag";

    public EntryListFragment() {
    }

    public static EntryListFragment newInstance() {
        return new EntryListFragment();
    }

    private FragmentEntryListBinding binding;
    private EntryViewModel model;
    private EntryListRecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getParentFragment() != null) {
            model = ViewModelProviders.of(getParentFragment()).get(EntryViewModel.class);
        }
        adapter = new EntryListRecyclerAdapter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_entry_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setModel(model);
        if (getActivity() != null)
            binding.recycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        binding.recycler.setAdapter(adapter);
        adapter.setListener(this);
        observeSetReps(model);
    }

    private void observeSetReps(EntryViewModel model) {
        model.getSetRepsRelation().observe(this, new Observer<ExerciseSetRepRelation>() {
            @Override
            public void onChanged(@Nullable ExerciseSetRepRelation exerciseSetRepRelation) {
                if (exerciseSetRepRelation != null) {
                    adapter.setItems(exerciseSetRepRelation.getJournalRepEntityList());
                } else {
                    adapter.setItems(null);
                }
            }
        });
    }

    @Override
    public void deleteRep(JournalRepEntity repEntity, List<JournalRepEntity> repEntityList) {
        model.deleteRep(repEntity, repEntityList);
    }

    @Override
    public void editRep(JournalRepEntity repEntity) {
        if (getParentFragment() != null) {
            EntryEditDialogFragment entryEditDialogFragment = EntryEditDialogFragment.newInstance();
            entryEditDialogFragment.setRepEntity(repEntity);
            entryEditDialogFragment.setListener(this);
            entryEditDialogFragment.show(getParentFragment().getChildFragmentManager(), TAG_EDIT_REP_DIALOG_FRAGMENT);
        }
    }

    @Override
    public void repEdited(JournalRepEntity repEntity) {
        model.updateRep(repEntity);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
