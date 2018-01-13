package eh.workout.journal.com.workoutjournal.ui.exercises;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentExerciseSelectorBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;
import eh.workout.journal.com.workoutjournal.ui.BaseFragment;

public class ExerciseSelectorFragment extends BaseFragment implements
        ExerciseSelectorRecyclerAdapter.ExerciseAdapterInterface {

    public ExerciseSelectorFragment() {
    }

    public static ExerciseSelectorFragment newInstance() {
        return new ExerciseSelectorFragment();
    }

    private ExerciseSelectorViewModel model;
    private ExerciseSelectorRecyclerAdapter adapter;
    private ExerciseSelectorInterface listener;
    private FragmentExerciseSelectorBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getParentFragment() != null) {
            model = ViewModelProviders.of(getParentFragment()).get(ExerciseSelectorViewModel.class);
        }
        adapter = new ExerciseSelectorRecyclerAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_exercise_selector, container, false);
        binding.noItems.setVisibility(View.GONE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recycler.setAdapter(adapter);
        adapter.setListener(this);
        observeExerciseList(model);
    }

    public void observeExerciseList(ExerciseSelectorViewModel model) {
        model.observeExerciseList().observe(this, new Observer<List<Object>>() {
            @Override
            public void onChanged(@Nullable List<Object> objectList) {
                adapter.setItems(objectList);
                binding.recycler.scrollToPosition(0);
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

    @Override
    public void exerciseSelected(ExerciseLiftEntity exerciseLift) {
        model.addExerciseToRecent(exerciseLift);
        if (listener != null) {
            listener.liftSelected(exerciseLift.getId(), exerciseLift.getExerciseInputType());
        }
    }

    @Override
    public void removeFromRecent(ExerciseLiftEntity exerciseLiftEntity) {
        model.removeExerciseFromRecent(exerciseLiftEntity);
    }

    public void setListener(ExerciseSelectorInterface listener) {
        this.listener = listener;
    }

    public interface ExerciseSelectorInterface {
        void liftSelected(String exerciseId, int inputType);
    }
}
