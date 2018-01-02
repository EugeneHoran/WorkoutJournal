package eh.workout.journal.com.workoutjournal.ui.exercises;

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
import eh.workout.journal.com.workoutjournal.databinding.FragmentExerciseSelectorBinding;
import eh.workout.journal.com.workoutjournal.db.relations.PlanSetRelation;

public class ExercisePlanFragment extends Fragment {
    private static final String ARG_DATE_TIMESTAMP = "arg_date_timestamp";

    public ExercisePlanFragment() {
    }

    public static ExercisePlanFragment newInstance(Long timestamp) {
        ExercisePlanFragment fragment = new ExercisePlanFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_DATE_TIMESTAMP, timestamp);
        fragment.setArguments(args);
        return fragment;
    }

    private long timestamp;
    private FragmentExerciseSelectorBinding binding;
    private ExerciseRoutineViewModel model;
    private ExercisePlanRecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            timestamp = getArguments().getLong(ARG_DATE_TIMESTAMP);
        }
        if (getParentFragment() != null) {
            model = ViewModelProviders.of(getParentFragment()).get(ExerciseRoutineViewModel.class);
        }
        adapter = new ExercisePlanRecyclerAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_exercise_selector, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recycler.setAdapter(adapter);
        observerPlanList(model);
    }

    public void observerPlanList(ExerciseRoutineViewModel model) {
        model.getPlanSets().observe(this, new Observer<List<PlanSetRelation>>() {
            @Override
            public void onChanged(@Nullable List<PlanSetRelation> planSetRelations) {
                adapter.setItems(planSetRelations);
            }
        });
    }
}
