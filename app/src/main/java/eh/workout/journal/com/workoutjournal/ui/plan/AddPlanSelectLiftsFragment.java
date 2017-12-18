package eh.workout.journal.com.workoutjournal.ui.plan;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentAddPlanSelectLiftsBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;


public class AddPlanSelectLiftsFragment extends Fragment {
    public AddPlanSelectLiftsFragment() {
    }

    public static AddPlanSelectLiftsFragment newInstance() {
        return new AddPlanSelectLiftsFragment();
    }

    private AddPlanViewModel model;
    private AddPlanLiftRecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(getActivity()).get(AddPlanViewModel.class);
        adapter = new AddPlanLiftRecyclerAdapter();
    }

    private FragmentAddPlanSelectLiftsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_plan_select_lifts, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recycler.setAdapter(adapter);
        binding.editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
                binding.recycler.scrollToPosition(0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        model.initLifts();
        observeLifts(model);
    }

    private void observeLifts(AddPlanViewModel model) {
        model.getLiftsLiveData().observe(this, new Observer<List<ExerciseLiftEntity>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseLiftEntity> exerciseLiftEntities) {
                adapter.setItems(exerciseLiftEntities);
            }
        });
    }
}
