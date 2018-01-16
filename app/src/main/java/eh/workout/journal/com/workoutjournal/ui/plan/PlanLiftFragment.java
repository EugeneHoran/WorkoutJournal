package eh.workout.journal.com.workoutjournal.ui.plan;

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
import eh.workout.journal.com.workoutjournal.ui.routine_new.RoutineLiftRecyclerAdapter;
import eh.workout.journal.com.workoutjournal.util.AnimationTransition;

public class PlanLiftFragment extends Fragment implements View.OnClickListener {
    private static final String TAG_FRAGMENT_FINAL = "tag_fragment_final";

    public PlanLiftFragment() {
    }

    public static PlanLiftFragment newInstance() {
        return new PlanLiftFragment();
    }

    private FragmentPlanLiftBinding binding;
    private PlanViewModel model;
    private RoutineLiftRecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getActivity() != null) {
            model = ViewModelProviders.of(getActivity()).get(PlanViewModel.class);
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
        binding.fab.setOnClickListener(this);
        observeExercises(model);
    }

    private void observeExercises(PlanViewModel model) {
        model.getExerciseLifts().observe(this, new Observer<List<ExerciseLiftEntity>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseLiftEntity> exerciseLiftEntities) {
                adapter.setItems(exerciseLiftEntities);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == binding.fab) {
            if (adapter.getSelectedList().size() == 0) {
                Snackbar.make(binding.fab, "Please select exercises to move on", Snackbar.LENGTH_SHORT).show();
            } else {
                if (getActivity() != null) {
                    model.setSelectedList(adapter.getSelectedList());
                    PlanAddActivity activity = (PlanAddActivity) getActivity();
                    activity.expandAppBar();
                    PlanFinalFragment finalFragment = PlanFinalFragment.newInstance();
                    initTransition(finalFragment);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .addSharedElement(binding.fab, "fab")
                            .replace(R.id.container, finalFragment, TAG_FRAGMENT_FINAL).addToBackStack(TAG_FRAGMENT_FINAL)
                            .commit();
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_exercise_selector, menu);
//        menu.findItem(R.id.action_search_exercise).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_exercise:
                ExerciseSelectorAddExerciseDialogFragment addExerciseDialogFragment = ExerciseSelectorAddExerciseDialogFragment.newInstance();
                addExerciseDialogFragment.show(getChildFragmentManager(), "TAG_ADD_LIFT_DIALOG_FRAGMENT");
                return true;
//            case R.id.action_search_exercise:
//                PlanAddActivity activity = (PlanAddActivity) getActivity();
//                if (activity != null) {
//                    activity.collapseAppBar();
//                }
//                return true;
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
