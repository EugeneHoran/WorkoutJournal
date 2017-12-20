package eh.workout.journal.com.workoutjournal.ui.plan;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentAddLiftDayPickerBinding;

public class AddPlanDaySelectorFragment extends Fragment {
    public AddPlanDaySelectorFragment() {
    }

    public static AddPlanDaySelectorFragment newInstance() {
        return new AddPlanDaySelectorFragment();
    }

    FragmentAddLiftDayPickerBinding binding;
    private AddPlanDaySelectRecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new AddPlanDaySelectRecyclerAdapter();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_lift_day_picker, container, false);
        binding.recyclerDays.setAdapter(adapter);
        return binding.getRoot();
    }

    public String getDaysString() {
        return adapter.getDaysString();
    }
}
