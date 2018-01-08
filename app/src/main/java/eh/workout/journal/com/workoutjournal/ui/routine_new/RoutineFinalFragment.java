package eh.workout.journal.com.workoutjournal.ui.routine_new;

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
import eh.workout.journal.com.workoutjournal.databinding.FragmentRoutineFinalNewBinding;


public class RoutineFinalFragment extends Fragment {
    public RoutineFinalFragment() {
    }

    public static RoutineFinalFragment newInstance() {
        return new RoutineFinalFragment();
    }

    private RoutineViewModel model;
    private RoutineRecyclerAdapter adapterDay;
    private RoutineLiftRecyclerAdapter adapterLift;
    private FragmentRoutineFinalNewBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            model = ViewModelProviders.of(getActivity()).get(RoutineViewModel.class);
        }
        adapterDay = new RoutineRecyclerAdapter(false, false);
        adapterLift = new RoutineLiftRecyclerAdapter(false);
        adapterDay.setItems(model.getDaySelectorList());
        adapterLift.setItems(model.getLiftListSelected());
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_routine_final_new, container, false);
        binding.recyclerDays.setNestedScrollingEnabled(false);
        binding.recyclerLifts.setNestedScrollingEnabled(false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerDays.setAdapter(adapterDay);
        binding.recyclerLifts.setAdapter(adapterLift);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.insertNewPlan(getPlanName());
                if (binding.switchAddPlan.isChecked()) {
                    model.insertPlan(getPlanName());
                }
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
    }

    public String getPlanName() {
        if (!TextUtils.isEmpty(binding.editPlanName.getText().toString().trim())) {
            return binding.editPlanName.getText().toString().trim();
        }
        return null;
    }
}
