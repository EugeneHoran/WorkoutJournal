package eh.workout.journal.com.workoutjournal.ui.routine;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.transition.Slide;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentRoutineDaysBinding;
import eh.workout.journal.com.workoutjournal.util.DetailsTransition;

public class RoutineDayFragment extends Fragment {
    public RoutineDayFragment() {
    }

    public static RoutineDayFragment newInstance() {
        return new RoutineDayFragment();
    }

    private RoutineAddViewModel model;
    private RoutineDayRecyclerAdapter adapter;
    private FragmentRoutineDaysBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            model = ViewModelProviders.of(getActivity()).get(RoutineAddViewModel.class);
        }
        adapter = new RoutineDayRecyclerAdapter(true, false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_routine_days, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerDays.setAdapter(adapter);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.getSelectedList().size() == 0) {
                    Snackbar.make(binding.fab, "Select days to continue", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                model.setDaysString(adapter.getDaysString());
                model.setDaySelectorList(adapter.getSelectedList());
                RoutineAddFinalFragment fragment = RoutineAddFinalFragment.newInstance();
                initTransition(fragment);
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .addSharedElement(binding.fab, "fab")
                            .replace(R.id.container, fragment, RoutineAddActivity.TAG_FINAL_FRAGMENT).addToBackStack(RoutineAddActivity.TAG_FINAL_FRAGMENT)
                            .commit();
                }
            }
        });
    }

    private void initTransition(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementEnterTransition(new DetailsTransition());
            fragment.setEnterTransition(new Slide());
            fragment.setSharedElementReturnTransition(new DetailsTransition());
            fragment.setExitTransition(new Slide());
        }
    }
}
