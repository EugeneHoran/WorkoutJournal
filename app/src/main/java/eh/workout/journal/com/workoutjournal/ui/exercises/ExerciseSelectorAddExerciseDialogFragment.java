package eh.workout.journal.com.workoutjournal.ui.exercises;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.util.UUID;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.DialogAddExerciseBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;


public class ExerciseSelectorAddExerciseDialogFragment extends DialogFragment {
    private AddExerciseDialogInterface listener;

    public ExerciseSelectorAddExerciseDialogFragment() {
    }

    public static ExerciseSelectorAddExerciseDialogFragment newInstance() {
        return new ExerciseSelectorAddExerciseDialogFragment();
    }

    private DialogAddExerciseBinding binding;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_add_exercise, null, false);
        builder.setTitle("Add Exercise");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!TextUtils.isEmpty(binding.exercise.getText().toString().trim())) {
                    ExerciseLiftEntity exerciseLiftEntity = new ExerciseLiftEntity(
                            UUID.randomUUID().toString(),
                            binding.exercise.getText().toString().trim(),
                            true);
                    if (listener != null) {
                        listener.saveNewExercise(exerciseLiftEntity);
                    }
                } else {
                    Toast.makeText(getActivity(), "Exercise can't be empty", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setView(binding.getRoot());
        return builder.create();
    }

    public void setListener(AddExerciseDialogInterface listener) {
        this.listener = listener;
    }

    public interface AddExerciseDialogInterface {
        void saveNewExercise(ExerciseLiftEntity exerciseLiftEntity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
