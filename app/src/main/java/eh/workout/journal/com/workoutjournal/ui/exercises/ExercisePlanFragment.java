package eh.workout.journal.com.workoutjournal.ui.exercises;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentExerciseSelectorBinding;


public class ExercisePlanFragment extends Fragment {

    public ExercisePlanFragment() {
    }

    public static ExercisePlanFragment newInstance() {
        return new ExercisePlanFragment();
    }

    private FragmentExerciseSelectorBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_exercise_selector, container, false);
        return binding.getRoot();
    }
}
