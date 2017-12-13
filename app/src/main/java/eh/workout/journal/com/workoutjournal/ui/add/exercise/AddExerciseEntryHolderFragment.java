package eh.workout.journal.com.workoutjournal.ui.add.exercise;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.NumberPicker;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentAddExerciseEntryHolderBinding;
import eh.workout.journal.com.workoutjournal.util.MultiTextWatcher;

public class AddExerciseEntryHolderFragment extends Fragment implements
        MultiTextWatcher.MultiTextWatcherInterfacePicker,
        NumberPicker.OnScrollListener,
        View.OnKeyListener {
    public AddExerciseEntryHolderFragment() {
    }

    public static AddExerciseEntryHolderFragment newInstance() {
        return new AddExerciseEntryHolderFragment();
    }

    private AddExerciseEntryViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getParentFragment() != null) {
            model = ViewModelProviders.of(getParentFragment()).get(AddExerciseEntryViewModel.class);
        }
    }

    FragmentAddExerciseEntryHolderBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_exercise_entry_holder, container, false);
        return binding.getRoot();
    }

    private EditText editWeight;
    private EditText editReps;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPickers();
    }

    private void initPickers() {
        editWeight = findInput(binding.pickerWeight);
        editReps = findInput(binding.pickerReps);
        MultiTextWatcher multiTextWatcher = new MultiTextWatcher();
        multiTextWatcher.registerEditText(editWeight);
        multiTextWatcher.registerEditText(editReps);
        multiTextWatcher.setCallback(this);
        binding.pickerReps.setMaxValue(100);
        binding.pickerReps.setMinValue(1);
        binding.pickerReps.setValue(10);
        binding.pickerWeight.setMaxValue(999);
        binding.pickerWeight.setMinValue(1);
        binding.pickerWeight.setValue(185);
        binding.pickerWeight.setOnScrollListener(this);
        binding.pickerReps.setOnScrollListener(this);
        editWeight.setOnKeyListener(this);
        editReps.setOnKeyListener(this);
        binding.saveSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    model.saveSet(String.valueOf(binding.pickerWeight.getValue()), String.valueOf(binding.pickerReps.getValue()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onScrollStateChange(NumberPicker numberPicker, int i) {
        InputMethodManager imm = (InputMethodManager) binding.getRoot().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(numberPicker.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            removeFocus();
            return true;
        }
        return false;
    }

    @Override
    public void onPickerChanged(EditText editText, Integer value) {
        if (editText == editWeight) {
            binding.pickerWeight.setValue(value);
        } else if (editText == editReps) {
            binding.pickerReps.setValue(value);
        }
    }

    private void removeFocus() {
        binding.parentHolder.requestFocus();
    }

    private EditText findInput(ViewGroup np) {
        int count = np.getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = np.getChildAt(i);
            if (child instanceof ViewGroup) {
                findInput((ViewGroup) child);
            } else if (child instanceof EditText) {
                return (EditText) child;
            }
        }
        return null;
    }
}
