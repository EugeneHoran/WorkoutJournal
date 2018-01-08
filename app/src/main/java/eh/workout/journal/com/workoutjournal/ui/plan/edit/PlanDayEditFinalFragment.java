package eh.workout.journal.com.workoutjournal.ui.plan.edit;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Slide;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentPlanFinalBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;
import eh.workout.journal.com.workoutjournal.ui.routine_new.RoutineLiftRecyclerAdapter;
import eh.workout.journal.com.workoutjournal.util.DetailsTransition;


public class PlanDayEditFinalFragment extends Fragment implements View.OnClickListener {
    private static final String TAG_FRAGMENT_LIFTS = "tag_fragment_lifts";

    public PlanDayEditFinalFragment() {
    }

    public static PlanDayEditFinalFragment newInstance() {
        return new PlanDayEditFinalFragment();
    }

    private FragmentPlanFinalBinding binding;
    private PlanDayEditViewModel model;
    private RoutineLiftRecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            model = ViewModelProviders.of(getActivity()).get(PlanDayEditViewModel.class);
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
        binding.fab.setOnClickListener(this);
        binding.editLifts.setOnClickListener(this);
        observeExercises(model);
        observePlanName(model);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.fab) {
            model.getPlanName().setValue(binding.editPlanName.getText().toString());
            model.updatePlan();
            if (getActivity() != null) {
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                getActivity().finish();
            }
        } else if (view == binding.editLifts) {
            model.getPlanName().setValue(binding.editPlanName.getText().toString());
            PlanDayEditLiftFragment fragment = PlanDayEditLiftFragment.newInstance();
            initTransition(fragment);
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addSharedElement(binding.fab, "fab")
                        .replace(R.id.container, fragment, TAG_FRAGMENT_LIFTS)
                        .addToBackStack(TAG_FRAGMENT_LIFTS)
                        .commit();
            }
        }
    }

    private void observeExercises(PlanDayEditViewModel model) {
        model.getExercisesSelected().observe(this, new Observer<List<ExerciseLiftEntity>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseLiftEntity> exerciseLiftEntities) {
                adapter.setItems(exerciseLiftEntities);
            }
        });
    }

    private void observePlanName(PlanDayEditViewModel model) {
        model.getPlanName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.editPlanName.setText(s);
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
