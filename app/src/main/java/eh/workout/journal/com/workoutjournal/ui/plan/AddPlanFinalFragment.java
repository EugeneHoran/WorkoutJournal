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
import eh.workout.journal.com.workoutjournal.databinding.FragmentAddPlanFinalBinding;

public class AddPlanFinalFragment extends Fragment {
    public AddPlanFinalFragment() {
    }

    public static AddPlanFinalFragment newInstance() {
        return new AddPlanFinalFragment();
    }

    private AddPlanViewModel model;
    private FragmentAddPlanFinalBinding binding;
    private AddPlanDaySelectRecyclerAdapter dayAdapter;
    private AddPlanSelectLiftRecyclerAdapter liftAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            model = ViewModelProviders.of(getActivity()).get(AddPlanViewModel.class);
        }
        dayAdapter = new AddPlanDaySelectRecyclerAdapter(false, false);
        liftAdapter = new AddPlanSelectLiftRecyclerAdapter(false);
        dayAdapter.setItems(model.getDaySelectorList());
        liftAdapter.setItems(model.getLifts());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_plan_final, container, false);
        binding.recyclerDays.setNestedScrollingEnabled(false);
        binding.recyclerLifts.setNestedScrollingEnabled(false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.txtNoSelectedDays.setVisibility(dayAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        binding.recyclerDays.setAdapter(dayAdapter);
        binding.recyclerLifts.setAdapter(liftAdapter);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setPlanName(getPlanName());
                model.insertNewPlan();
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
