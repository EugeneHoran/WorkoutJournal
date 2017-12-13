package eh.workout.journal.com.workoutjournal.ui.add.exercise;

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
import eh.workout.journal.com.workoutjournal.databinding.FragmentAddExerciseEntryBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;


public class AddExerciseEntryFragment extends Fragment implements AddExerciseEntryRecyclerAdapter.EntryAdapterInterface {
    public AddExerciseEntryFragment() {
    }

    public static AddExerciseEntryFragment newInstance() {
        return new AddExerciseEntryFragment();
    }

    private FragmentAddExerciseEntryBinding binding;
    private AddExerciseEntryViewModel model;
    private AddExerciseEntryRecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getParentFragment() != null) {
            model = ViewModelProviders.of(getParentFragment()).get(AddExerciseEntryViewModel.class);
        }
        adapter = new AddExerciseEntryRecyclerAdapter();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_exercise_entry, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recycler.setAdapter(adapter);
        adapter.setListener(this);
        observeSetReps(model);
    }

    private void observeSetReps(AddExerciseEntryViewModel model) {
        model.getObservableSetReps().observe(this, new Observer<ExerciseSetRepRelation>() {
            @Override
            public void onChanged(@Nullable ExerciseSetRepRelation exerciseSetRepRelation) {
                if (exerciseSetRepRelation != null) {
                    adapter.setItems(exerciseSetRepRelation.getJournalRepEntityList());
                }
            }
        });
    }

    @Override
    public void deleteRepEntity(JournalRepEntity repEntity) {
        model.deleteRep(repEntity);
    }

    @Override
    public void deleteRep(JournalRepEntity repEntity, List<JournalRepEntity> repEntityList) {
        model.deleteRepAndFilter(repEntity, repEntityList);
    }
}
