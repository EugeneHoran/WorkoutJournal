package eh.workout.journal.com.workoutjournal.ui.plan.edit;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Slide;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentEditPlanEditorBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;
import eh.workout.journal.com.workoutjournal.db.relations.RoutineSetRelation;
import eh.workout.journal.com.workoutjournal.model.DaySelector;
import eh.workout.journal.com.workoutjournal.ui.routine.RoutineDayRecyclerAdapter;
import eh.workout.journal.com.workoutjournal.ui.routine.RoutineLiftRecyclerAdapter;
import eh.workout.journal.com.workoutjournal.util.DetailsTransition;


public class EditPlanFragment extends Fragment {
    public EditPlanFragment() {
    }

    public static EditPlanFragment newInstance() {
        return new EditPlanFragment();
    }

    private EditPlanViewModel model;
    private FragmentEditPlanEditorBinding binding;
    private RoutineDayRecyclerAdapter adapterDays;
    private RoutineLiftRecyclerAdapter adapterLifts;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            model = ViewModelProviders.of(getActivity()).get(EditPlanViewModel.class);
        }
        adapterDays = new RoutineDayRecyclerAdapter(false, true);
        adapterLifts = new RoutineLiftRecyclerAdapter(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_plan_editor, container, false);
        binding.setModel(model);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerDays.setAdapter(adapterDays);
        binding.recyclerLifts.setAdapter(adapterLifts);
        binding.daysTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    EditPlanDaySelectorFragment fragment = EditPlanDaySelectorFragment.newInstance();
                    initTransition(fragment);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .addSharedElement(binding.fab, "fab")
                            .replace(R.id.container, fragment)
                            .addToBackStack(null).commit();
                }
            }
        });
        binding.liftsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    EditPlanLiftSelectorFragment fragment = EditPlanLiftSelectorFragment.newInstance();
                    initTransition(fragment);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .addSharedElement(binding.fab, "fab")
                            .replace(R.id.container, fragment)
                            .addToBackStack(null).commit();
                }
            }
        });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    model.updatePlan(getTitle());
                    getActivity().onBackPressed();
                }
            }
        });
        observePlanData(model);
    }

    private void observePlanData(final EditPlanViewModel model) {
        model.getPlanSetRelation().observe(this, new Observer<RoutineSetRelation>() {
            @Override
            public void onChanged(@Nullable RoutineSetRelation routineSetRelation) {
                if (routineSetRelation != null) {
                    if (!model.titleSet) {
                        binding.editPlanName.setText(routineSetRelation.getRoutineEntity().getRoutineName());
                    }
                    binding.title.requestFocus();
                }
            }
        });
        model.getSelectedDaysOfWeek().observe(this, new Observer<List<DaySelector>>() {
            @Override
            public void onChanged(@Nullable List<DaySelector> daySelectors) {
                if (daySelectors != null) {
                    model.noSelectedDays.set(daySelectors.size() == 0);
                    adapterDays.setItems(daySelectors);
                }
            }
        });
        model.getAllSelectedExercises().observe(this, new Observer<List<ExerciseLiftEntity>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseLiftEntity> exerciseLiftEntities) {
                adapterLifts.setItems(exerciseLiftEntities);
            }
        });
    }


    private String getTitle() {
        if (TextUtils.isEmpty(binding.editPlanName.getText().toString().trim())) {
            return null;
        }
        return binding.editPlanName.getText().toString().trim();
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
