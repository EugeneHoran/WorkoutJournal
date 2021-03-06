package eh.workout.journal.com.workoutjournal.ui.routine_new;

import android.arch.lifecycle.Observer;
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
import eh.workout.journal.com.workoutjournal.ui.plan.PlanAddActivity;
import eh.workout.journal.com.workoutjournal.util.AnimationTransition;


public class RoutineLiftFragment extends Fragment {

    public RoutineLiftFragment() {
    }

    public static RoutineLiftFragment newInstance() {
        return new RoutineLiftFragment();
    }

    private RoutineViewModel model;
    private RoutineLiftRecyclerAdapter adapter;
    private FragmentPlanLiftBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getActivity() != null) {
            model = ViewModelProviders.of(getActivity()).get(RoutineViewModel.class);
        }
        adapter = new RoutineLiftRecyclerAdapter(true);
    }

    private void observeLiftList(RoutineViewModel model) {
        model.getLiftList().observe(this, new Observer<List<ExerciseLiftEntity>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseLiftEntity> exerciseLiftEntities) {
                adapter.setItems(exerciseLiftEntities);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plan_lift, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recycler.setAdapter(adapter);
        observeLiftList(model);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.getSelectedList().size() == 0) {
                    Snackbar.make(binding.fab, "Select exercises to continue", Snackbar.LENGTH_SHORT).show();
                    return;
                } else {
                    model.setSelectedList(adapter.getAllCheckedList());
                    model.setLiftListSelected(adapter.getSelectedList());
                }
                RoutineDaySelectorFragment fragment = RoutineDaySelectorFragment.newInstance();
                initTransition(fragment);
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .addSharedElement(binding.fab, "fab")
                            .replace(R.id.container, fragment, RoutineActivity.TAG_DAY_SELECTOR_FRAGMENT).addToBackStack(RoutineActivity.TAG_DAY_SELECTOR_FRAGMENT)
                            .commit();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_exercise_selector, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_exercise:
                ExerciseSelectorAddExerciseDialogFragment addExerciseDialogFragment = ExerciseSelectorAddExerciseDialogFragment.newInstance();
                addExerciseDialogFragment.show(getChildFragmentManager(), "TAG_ADD_LIFT_DIALOG_FRAGMENT");
                return true;
            default:
                break;
        }
        return false;
    }

    private void initTransition(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementEnterTransition(new AnimationTransition());
            fragment.setEnterTransition(new Slide());
            fragment.setSharedElementReturnTransition(new AnimationTransition());
            fragment.setExitTransition(new Slide());
        }
    }
}
