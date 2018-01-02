package eh.workout.journal.com.workoutjournal.ui.routine.edit;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentEditRoutineDayBinding;
import eh.workout.journal.com.workoutjournal.ui.routine.RoutineDayRecyclerAdapter;


public class EditRoutineDaySelectorFragment extends Fragment {
    public EditRoutineDaySelectorFragment() {
    }

    public static EditRoutineDaySelectorFragment newInstance() {
        return new EditRoutineDaySelectorFragment();
    }

    private EditRoutineViewModel model;
    private RoutineDayRecyclerAdapter adapter;
    private FragmentEditRoutineDayBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            model = ViewModelProviders.of(getActivity()).get(EditRoutineViewModel.class);
        }
        adapter = new RoutineDayRecyclerAdapter(true, true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_routine_day, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerDays.setAdapter(adapter);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.getSelectedDaysOfWeek().setValue(adapter.getSelectedList());
                model.getAllDaysOfWeek().setValue(adapter.getItemList());
                getActivity().onBackPressed();
            }
        });
        adapter.setItems(model.getAllDaysOfWeek().getValue());
    }
}
