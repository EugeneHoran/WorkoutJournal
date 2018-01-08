package eh.workout.journal.com.workoutjournal.ui.plan;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentPlanFinalBinding;
import eh.workout.journal.com.workoutjournal.ui.routine_new.RoutineLiftRecyclerAdapter;


public class PlanFinalFragment extends Fragment {
    public PlanFinalFragment() {
    }

    public static PlanFinalFragment newInstance() {
        return new PlanFinalFragment();
    }

    private FragmentPlanFinalBinding binding;
    private PlanViewModel model;
    private RoutineLiftRecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getActivity() != null) {
            model = ViewModelProviders.of(getActivity()).get(PlanViewModel.class);
        }
        adapter = new RoutineLiftRecyclerAdapter(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plan_final, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recycler.setAdapter(adapter);
        adapter.setItems(model.getExerciseLiftEntities());
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.insertPlan(TextUtils.isEmpty(binding.editPlanName.getText()) ? null : binding.editPlanName.getText().toString());
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
        binding.editLifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });
    }
}
