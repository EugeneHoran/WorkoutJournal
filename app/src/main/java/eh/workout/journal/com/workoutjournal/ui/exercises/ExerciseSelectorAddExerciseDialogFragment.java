package eh.workout.journal.com.workoutjournal.ui.exercises;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.DialogAddExerciseBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;


@SuppressWarnings("ConstantConditions")
public class ExerciseSelectorAddExerciseDialogFragment extends DialogFragment {
    public ExerciseSelectorAddExerciseDialogFragment() {
    }

    public static ExerciseSelectorAddExerciseDialogFragment newInstance() {
        return new ExerciseSelectorAddExerciseDialogFragment();
    }

    private ExerciseSelectorViewModel model;
    private DialogAddExerciseBinding binding;
    private List<ExerciseLiftEntity> lifts;
    private boolean dataLoaded = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        model = ViewModelProviders.of(this).get(ExerciseSelectorViewModel.class);
        observeExerciseList(model);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_add_exercise, null, false);
        builder.setTitle("Add Exercise");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!TextUtils.isEmpty(binding.exercise.getText().toString().trim())) {
                    if (!duplicates(binding.exercise.getText().toString().trim(), binding.spinnerEquipmentType.getSelectedItemPosition())) {
                        ExerciseLiftEntity liftEntity = new ExerciseLiftEntity();
                        liftEntity.setId(UUID.randomUUID().toString());
                        liftEntity.setName(binding.exercise.getText().toString().trim());
                        liftEntity.setRecent(true);
                        liftEntity.setTimestampRecent(new Date().getTime());
                        liftEntity.setExerciseEquipmentId(binding.spinnerEquipmentType.getSelectedItemPosition());
                        liftEntity.setExerciseGroupId(binding.spinnerExerciseBody.getSelectedItemPosition());
                        liftEntity.setExerciseInputType(getInputType(binding.spinnerEquipmentType.getSelectedItemPosition()));
                        if (dataLoaded) {
                            model.addExercise(liftEntity);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Duplicate", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Exercise can't be empty", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setView(binding.getRoot());
        return builder.create();
    }


    private int getInputType(int pos) {
        if (pos <= 3) {
            return 0;
        } else if (pos == 4) {
            return 1;
        } else {
            return 2;
        }
    }

    boolean duplicates(String exerciseName, int equipmentPos) {
        boolean duplicate = false;
        for (int i = 0; i < lifts.size(); i++) {
            if (lifts.get(i).getName().equalsIgnoreCase(exerciseName) && lifts.get(i).getExerciseEquipmentId() == equipmentPos) {
                duplicate = true;
            }
        }
        return duplicate;
    }

    private void observeExerciseList(ExerciseSelectorViewModel model) {
        model.getAllExercises().observe(this, new Observer<List<ExerciseLiftEntity>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseLiftEntity> exerciseLiftEntities) {
                lifts = exerciseLiftEntities;
                dataLoaded = true;
            }
        });
    }
}
