package eh.workout.journal.com.workoutjournal.ui.entry;

import android.arch.lifecycle.Observer;
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
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseOrmEntity;
import eh.workout.journal.com.workoutjournal.util.Constants;
import eh.workout.journal.com.workoutjournal.util.ExerciseDataHelper;
import eh.workout.journal.com.workoutjournal.util.MyStringUtil;
import eh.workout.journal.com.workoutjournal.util.OrmHelper;
import eh.workout.journal.com.workoutjournal.util.views.QueryTextWatcher;
import eh.workout.journal.com.workoutjournal.util.views.SimpleTextWatcher;

public class EntryInputFragment extends Fragment implements
        QueryTextWatcher.MultiTextWatcherInterfacePicker,
        NumberPicker.OnScrollListener,
        View.OnKeyListener {
    public EntryInputFragment() {
    }

    public static EntryInputFragment newInstance(int inputType) {
        EntryInputFragment fragment = new EntryInputFragment();
        Bundle args = new Bundle();
        args.putInt(EntryParentFragment.ARG_LIFT_INPUT_TYPE, inputType);
        fragment.setArguments(args);
        return fragment;
    }

    private EntryViewModelNew model;
    private int inputType = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getParentFragment() != null) {
            model = ViewModelProviders.of(getParentFragment()).get(EntryViewModelNew.class);
        }
        if (getArguments() != null) {
            inputType = getArguments().getInt(EntryParentFragment.ARG_LIFT_INPUT_TYPE);
        }
    }

    private FragmentEntryInputBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_entry_input, container, false);
        if (inputType == ExerciseDataHelper.EXERCISE_TYPE_BODY) {
            binding.viewInput.pickerWeight.setVisibility(View.GONE);
            binding.viewInput.textWeight.setVisibility(View.GONE);
            binding.viewInput.spaceWeight.setVisibility(View.GONE);
        }
        return binding.getRoot();
    }

    private EditText editWeight;
    private EditText editReps;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setModel(model);
        observeOneRepMax(model);
        initPickers();
    }

    private void observeOneRepMax(EntryViewModelNew model) {
        model.getOrmEntityLiveData().observe(this, new Observer<ExerciseOrmEntity>() {
            @Override
            public void onChanged(@Nullable ExerciseOrmEntity ormEntity) {
                if (ormEntity == null) {
                    binding.ormHolder.setVisibility(View.GONE);
                } else {
                    binding.ormHolder.setVisibility(View.VISIBLE);
                    if (ormEntity.getInputType() == Constants.EXERCISE_TYPE_WEIGHT) {
                        binding.txtOrm.setText(MyStringUtil.formatOneRepMaxWeight(ormEntity));
                    } else {
                        binding.txtOrm.setText(MyStringUtil.formatOneRepMaxReps(ormEntity));
                    }
                }
            }
        });
    }

    private void initPickers() {
        editWeight = findInput(binding.viewInput.pickerWeight);
        editReps = findInput(binding.viewInput.pickerReps);
        new SimpleTextWatcher(new SimpleTextWatcher.SimpleTextWatcherInterface() {
            @Override
            public void onTextChanged(EditText editText, String string, CharSequence charSequence, int count) {

            }
        }, true).registerEditText(editWeight).registerEditText(editReps);
        QueryTextWatcher queryTextWatcher = new QueryTextWatcher();
        queryTextWatcher.registerEditText(editWeight);
        queryTextWatcher.registerEditText(editReps);
        queryTextWatcher.setCallback(this);
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
                binding.ormHolder.setVisibility(View.VISIBLE);
                String weight = String.valueOf(binding.viewInput.pickerWeight.getValue());
                String reps = String.valueOf(binding.viewInput.pickerReps.getValue());
                model.saveRep(inputType == 0 ? weight : null, reps, getOneRepMax(weight, reps));
            }
        });
    }

    private double getOneRepMax(String weight, String reps) {
        switch (inputType) {
            case ExerciseDataHelper.EXERCISE_TYPE_WEIGHT:
                return OrmHelper.getOneRepMax(weight, reps);
            case ExerciseDataHelper.EXERCISE_TYPE_BODY:
                return Double.valueOf(reps);
            default:
                return 0;
        }
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
