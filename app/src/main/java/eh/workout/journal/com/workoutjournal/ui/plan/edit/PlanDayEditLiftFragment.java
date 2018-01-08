package eh.workout.journal.com.workoutjournal.ui.plan.edit;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentPlanLiftBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;
import eh.workout.journal.com.workoutjournal.ui.exercises.ExerciseSelectorAddExerciseDialogFragment;
import eh.workout.journal.com.workoutjournal.ui.routine_new.RoutineLiftRecyclerAdapter;


public class PlanDayEditLiftFragment extends Fragment {
    public PlanDayEditLiftFragment() {
    }

    public static PlanDayEditLiftFragment newInstance() {
        return new PlanDayEditLiftFragment();
    }

    private FragmentPlanLiftBinding binding;
    private PlanDayEditViewModel model;
    private RoutineLiftRecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getActivity() != null) {
            model = ViewModelProviders.of(getActivity()).get(PlanDayEditViewModel.class);
        }
        adapter = new RoutineLiftRecyclerAdapter(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plan_lift, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recycler.setAdapter(adapter);
        observeExercises(model);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.getSelectedList().size() == 0) {
                    Snackbar.make(binding.fab, "Fab", Snackbar.LENGTH_SHORT).show();
                } else {
                    if (getActivity() != null) {
                        model.getExercises().setValue(adapter.getAllCheckedList());
                        model.getExercisesSelected().setValue(adapter.getSelectedList());
                        PlanDayEditActivity activity = (PlanDayEditActivity) getActivity();
                        activity.expandAppBar();
                        activity.getSupportFragmentManager().popBackStack();
                    }
                }
            }
        });
    }

    private void observeExercises(PlanDayEditViewModel model) {
        model.getExercises().observe(this, new Observer<List<ExerciseLiftEntity>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseLiftEntity> exerciseLiftEntities) {
                adapter.setItems(exerciseLiftEntities);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_exercise_selector, menu);
        menu.findItem(R.id.action_search_exercise).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_exercise:
                ExerciseSelectorAddExerciseDialogFragment addExerciseDialogFragment = ExerciseSelectorAddExerciseDialogFragment.newInstance();
                addExerciseDialogFragment.show(getChildFragmentManager(), "TAG_ADD_LIFT_DIALOG_FRAGMENT");
                return true;
            case R.id.action_search_exercise:
                PlanDayEditActivity activity = (PlanDayEditActivity) getActivity();
                if (activity != null) {
                    activity.collapseAppBar();
                }
                return true;
            default:
                break;
        }
        return false;
    }
}
