package eh.workout.journal.com.workoutjournal.ui.routine_new.edit;

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
import eh.workout.journal.com.workoutjournal.databinding.FragmentEditRoutineBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;
import eh.workout.journal.com.workoutjournal.db.relations.RoutineSetRelation;
import eh.workout.journal.com.workoutjournal.model.DaySelector;
import eh.workout.journal.com.workoutjournal.ui.routine_new.RoutineRecyclerAdapter;
import eh.workout.journal.com.workoutjournal.ui.routine_new.RoutineLiftRecyclerAdapter;
import eh.workout.journal.com.workoutjournal.util.DetailsTransition;


public class EditRoutineFragment extends Fragment {
    public EditRoutineFragment() {
    }

    public static EditRoutineFragment newInstance() {
        return new EditRoutineFragment();
    }

    private EditRoutineViewModel model;
    private FragmentEditRoutineBinding binding;
    private RoutineRecyclerAdapter adapterDays;
    private RoutineLiftRecyclerAdapter adapterLifts;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            model = ViewModelProviders.of(getActivity()).get(EditRoutineViewModel.class);
        }
        adapterDays = new RoutineRecyclerAdapter(false, true);
        adapterLifts = new RoutineLiftRecyclerAdapter(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_routine, container, false);
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
                    EditRoutineDaySelectorFragment fragment = EditRoutineDaySelectorFragment.newInstance();
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
                    EditRoutineLiftSelectorFragment fragment = EditRoutineLiftSelectorFragment.newInstance();
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
                    EditRoutineActivity activity = (EditRoutineActivity) getActivity();
                    activity.setResultFrom(true);
                }
            }
        });


        observePlanData(model);
    }

    private void observePlanData(final EditRoutineViewModel model) {
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
