package eh.workout.journal.com.workoutjournal.ui.exercises;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentExerciseSelectorBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseGroupEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;

public class ExerciseGroupFragment extends Fragment {


    public ExerciseGroupFragment() {
    }

    public static ExerciseGroupFragment newInstance() {
        return new ExerciseGroupFragment();
    }

    private FragmentExerciseSelectorBinding binding;
    private ExerciseGroupViewModel model;
    private ExerciseGroupRecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getParentFragment() != null) {
            model = ViewModelProviders.of(getParentFragment()).get(ExerciseGroupViewModel.class);
        }
        adapter = new ExerciseGroupRecyclerAdapter(new ExerciseGroupRecyclerAdapter.GroupRecyclerInterface() {
            @Override
            public void onGroupItemClicked(ExerciseGroupEntity exerciseGroupEntity) {
                model.groupItemClicked(exerciseGroupEntity);
            }

            @Override
            public void onReturnToGroupClicked() {
                returnToGroup();
            }

            @Override
            public void exerciseSelected(ExerciseLiftEntity exerciseLift) {
                if (listener != null) {
                    listener.liftSelected(exerciseLift.getId(), exerciseLift.getExerciseInputType());
                }
            }
        });
    }

    public void returnToGroup() {
        model.returnToGroupList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_exercise_selector, container, false);
        binding.noItems.setVisibility(View.GONE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recycler.setAdapter(adapter);
        observeObjectList(model);
    }

    private void observeObjectList(ExerciseGroupViewModel model) {
        model.getGroupAndLiftObjectList().observe(this, new Observer<List<Object>>() {
            @Override
            public void onChanged(@Nullable List<Object> objectList) {
                adapter.setItems(objectList);
            }
        });
        model.getAllExercisesLive().observe(this, new Observer<List<ExerciseLiftEntity>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseLiftEntity> exerciseLiftEntities) {

            }
        });
    }

    //    @Override
//    public void onGroupItemClicked(ExerciseGroupEntity exerciseGroupEntity) {
//        model.groupItemClicked(exerciseGroupEntity);
//    }
//
//    @Override
//    public void onReturnToGroupClicked() {
//        model.returnToGroupList();
//    }
//
//    @Override
//    public void exerciseSelected(ExerciseLiftEntity exerciseLift) {
////        listener.liftSelected(exerciseLift.getId(), exerciseLift.getExerciseInputType());
//    }
    private GroupInterface listener;

    public void setListener(GroupInterface listener) {
        this.listener = listener;
    }

    public interface GroupInterface {
        void liftSelected(String exerciseId, int inputType);
    }
}
