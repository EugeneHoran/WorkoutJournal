package eh.workout.journal.com.workoutjournal.ui.exercises;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentExerciseSelectorBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanEntity;
import eh.workout.journal.com.workoutjournal.db.relations.PlanDaySetRelation;
import eh.workout.journal.com.workoutjournal.db.relations.PlanSetRelation;

public class ExercisePlanFragment extends Fragment {
    private static final String ARG_DATE_TIMESTAMP = "arg_date_timestamp";

    public ExercisePlanFragment() {
    }

    public static ExercisePlanFragment newInstance(Long timestamp) {
        ExercisePlanFragment fragment = new ExercisePlanFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_DATE_TIMESTAMP, timestamp);
        fragment.setArguments(args);
        return fragment;
    }

    private long timestamp;
    private FragmentExerciseSelectorBinding binding;
    private ExerciseRoutineViewModel model;
    private ExercisePlanRecyclerAdapter adapter;
    private List<PlanDaySetRelation> planDays = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            timestamp = getArguments().getLong(ARG_DATE_TIMESTAMP);
        }
        if (getParentFragment() != null) {
            model = ViewModelProviders.of(getParentFragment()).get(ExerciseRoutineViewModel.class);
        }
        adapter = new ExercisePlanRecyclerAdapter(new ExercisePlanRecyclerAdapter.ExercisePlanInterface() {
            @Override
            public void onDeletePlan(PlanEntity planEntity) {
                model.deletePlan(planEntity);
            }

            @Override
            public void onPlanClicked(PlanSetRelation planSetRelation) {
                if (planDays.size() > 0) {
                    for (int i = 0; i < planDays.size(); i++) {
                        PlanDaySetRelation planDaySetRelation = planDays.get(i);
                        if (planDaySetRelation.getPlanDayEntity().getPlanEntityId().equalsIgnoreCase(planSetRelation.getPlanEntity().getId())) {
                            Snackbar.make(binding.recycler, "Plan already set for this date", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                model.insertPlan(planSetRelation);
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_exercise_selector, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recycler.setAdapter(adapter);
        observerPlanList(model);
        observeDayPlanList(model);
    }

    public void observeDayPlanList(ExerciseRoutineViewModel model) {
        model.getPlanSetRelationDayLive().observe(this, new Observer<List<PlanDaySetRelation>>() {
            @Override
            public void onChanged(@Nullable List<PlanDaySetRelation> planDaySetRelations) {
                if (planDaySetRelations != null) {
                    planDays.clear();
                    planDays.addAll(planDaySetRelations);
                }
            }
        });
    }

    public void observerPlanList(ExerciseRoutineViewModel model) {
        model.getPlanSets().observe(this, new Observer<List<PlanSetRelation>>() {
            @Override
            public void onChanged(@Nullable List<PlanSetRelation> planSetRelations) {
                binding.noItems.setVisibility(planSetRelations != null && planSetRelations.size() > 0 ? View.GONE : View.VISIBLE);
                adapter.setItems(planSetRelations);
            }
        });
    }
}
