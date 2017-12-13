package eh.workout.journal.com.workoutjournal.ui.entry;

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
import eh.workout.journal.com.workoutjournal.databinding.FragmentEntryInputBinding;
import eh.workout.journal.com.workoutjournal.util.MultiTextWatcher;

public class EntryInputFragment extends Fragment implements
        MultiTextWatcher.MultiTextWatcherInterfacePicker,
        NumberPicker.OnScrollListener,
        View.OnKeyListener {
    public EntryInputFragment() {
    }

    public static EntryInputFragment newInstance() {
        return new EntryInputFragment();
    }

    private EntryViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getParentFragment() != null) {
            model = ViewModelProviders.of(getParentFragment()).get(EntryViewModel.class);
        }
    }

    private FragmentEntryInputBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_entry_input, container, false);
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
        editWeight = findInput(binding.viewInput.pickerWeight);
        editReps = findInput(binding.viewInput.pickerReps);
        MultiTextWatcher multiTextWatcher = new MultiTextWatcher();
        multiTextWatcher.registerEditText(editWeight);
        multiTextWatcher.registerEditText(editReps);
        multiTextWatcher.setCallback(this);
        binding.viewInput.pickerReps.setMaxValue(100);
        binding.viewInput.pickerReps.setMinValue(1);
        binding.viewInput.pickerReps.setValue(10);
        binding.viewInput.pickerWeight.setMaxValue(999);
        binding.viewInput.pickerWeight.setMinValue(1);
        binding.viewInput.pickerWeight.setValue(185);
        binding.viewInput.pickerWeight.setOnScrollListener(this);
        binding.viewInput.pickerReps.setOnScrollListener(this);
        editWeight.setOnKeyListener(this);
        editReps.setOnKeyListener(this);
        binding.saveSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    model.saveSet(String.valueOf(binding.viewInput.pickerWeight.getValue()), String.valueOf(binding.viewInput.pickerReps.getValue()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onStop() {
        binding.cardHolder.setVisibility(View.INVISIBLE);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (binding.cardHolder.getVisibility() == View.INVISIBLE) {
            binding.cardHolder.setVisibility(View.VISIBLE);
        }
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
            binding.viewInput.pickerWeight.setValue(value);
        } else if (editText == editReps) {
            binding.viewInput.pickerReps.setValue(value);
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
