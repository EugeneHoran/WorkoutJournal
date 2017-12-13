package eh.workout.journal.com.workoutjournal.ui.add.exercise;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentAddExerciseEntryBinding;


public class AddExerciseHistoryFragment extends Fragment {

    public AddExerciseHistoryFragment() {
    }

    public static AddExerciseHistoryFragment newInstance() {
        return new AddExerciseHistoryFragment();
    }

    FragmentAddExerciseEntryBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_exercise_entry, container, false);
        return binding.getRoot();
    }
}
