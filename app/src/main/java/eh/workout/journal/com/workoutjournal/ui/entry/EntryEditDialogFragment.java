package eh.workout.journal.com.workoutjournal.ui.entry;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.ViewEntryPickersBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;
import eh.workout.journal.com.workoutjournal.util.OrmHelper;

public class EntryEditDialogFragment extends DialogFragment {

    public EntryEditDialogFragment() {
    }

    public static EntryEditDialogFragment newInstance() {
        return new EntryEditDialogFragment();
    }

    private EditRepInterface listener;

    public void setListener(EditRepInterface listener) {
        this.listener = listener;
    }

    public interface EditRepInterface {
        void repEdited(JournalRepEntity repEntity);
    }

    private JournalRepEntity repEntity;

    public void setRepEntity(JournalRepEntity repEntity) {
        this.repEntity = repEntity;
    }

    private ViewEntryPickersBinding binding;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.view_entry_pickers, null, false);
        builder.setTitle("Edit ");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (listener != null) {
                    String weight = String.valueOf(binding.pickerWeight.getValue());
                    String reps = String.valueOf(binding.pickerReps.getValue());
                    double oneRepMax = OrmHelper.getOneRepMax(weight, reps);
                    repEntity.setOneRepMax(oneRepMax);
                    repEntity.setWeight(weight);
                    repEntity.setReps(reps);
                    listener.repEdited(repEntity);
                }
            }
        });
        initViews();
        builder.setView(binding.getRoot());
        return builder.create();
    }

    private void initViews() {
        binding.pickerReps.setMaxValue(100);
        binding.pickerReps.setMinValue(1);
        binding.pickerReps.setValue(Integer.valueOf(repEntity.getReps()));
        binding.pickerWeight.setMaxValue(999);
        binding.pickerWeight.setMinValue(1);
        if (repEntity.getWeight() != null) {
            binding.pickerWeight.setValue(Integer.valueOf(repEntity.getWeight()));
        } else {
            binding.pickerWeight.setVisibility(View.GONE);
        }
    }

}
