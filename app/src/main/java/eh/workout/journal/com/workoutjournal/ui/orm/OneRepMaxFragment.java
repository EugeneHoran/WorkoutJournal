package eh.workout.journal.com.workoutjournal.ui.orm;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentOneRepMaxBinding;
import eh.workout.journal.com.workoutjournal.util.Constants;
import eh.workout.journal.com.workoutjournal.util.OrmHelper;
import eh.workout.journal.com.workoutjournal.util.views.SimpleSpinnerListener;
import eh.workout.journal.com.workoutjournal.util.views.SimpleTextWatcher;


public class OneRepMaxFragment extends Fragment {
    private static final String ARG_WHICH = "arg_which";

    public OneRepMaxFragment() {
    }

    public static OneRepMaxFragment newInstance(int which) {
        OneRepMaxFragment fragment = new OneRepMaxFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WHICH, which);
        fragment.setArguments(args);
        return fragment;
    }

    private int which;
    private FragmentOneRepMaxBinding binding;
    private EditText editWeight;
    private Spinner spinnerReps;
    private OneRepMaxRecyclerAdapter adapterOrm;
    private SimpleTextWatcher simpleTextWatcher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            which = getArguments().getInt(ARG_WHICH);
        }
        adapterOrm = new OneRepMaxRecyclerAdapter(which);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_one_rep_max, container, false);
        binding.setNavListener(navListener);
        if (getActivity() != null) {
            binding.recycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        }
        if (which == Constants.ORM_PERCENTAGES) {
            binding.setTitle("Percentage Calculator");
            binding.viewToolbarWeightReps.percentageContainer.setVisibility(View.VISIBLE);
        } else {
            binding.setTitle("One Rep Max Calculator");
        }
        editWeight = binding.viewToolbarWeightReps.editWeight;
        spinnerReps = binding.viewToolbarWeightReps.spinnerReps;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (which == Constants.ORM_PERCENTAGES) {
            simpleTextWatcher = new SimpleTextWatcher(new SimpleTextWatcher.SimpleTextWatcherInterface() {
                @Override
                public void onTextChanged(EditText editText, String string, CharSequence charSequence, int count) {
                    if (count == 0) {
                        adapterOrm.setItems(null);
                        return;
                    }
                    if (count == 3) {
                        removeWeightFocus();
                    }
                    adapterOrm.setItems(OrmHelper.getPercentageList(null, 0, Double.valueOf(binding.viewToolbarWeightReps.orm.getText().toString().trim())));
                }
            }, true)
                    .registerEditText(binding.viewToolbarWeightReps.orm);
        }
        new SimpleTextWatcher(new SimpleTextWatcher.SimpleTextWatcherInterface() {
            @Override
            public void onTextChanged(EditText editText, String string, CharSequence charSequence, int count) {
                sendDataToAdapter(getWeightLifted(), getRepsPerformed());
                if (count == 3) {
                    removeWeightFocus();
                }
            }
        }, true)
                .registerEditText(editWeight);

        new SimpleSpinnerListener().registerSpinner(spinnerReps)
                .setCallback(new SimpleSpinnerListener.RepsSpinnerInterface() {
                    @Override
                    public void onRepsChanged() {
                        sendDataToAdapter(getWeightLifted(), getRepsPerformed());
                        removeWeightFocus();
                    }
                });
        binding.recycler.setAdapter(adapterOrm);
    }

    private void sendDataToAdapter(Integer weight, int reps) {
        if (which == Constants.ORM_ONE_REP_MAX) {
            adapterOrm.setItems(weight == null ? null : OrmHelper.ormList(weight, reps));
        } else {
            if (weight == null) {
                adapterOrm.setItems(null);
                return;
            }
            simpleTextWatcher.setRegister(false);
            binding.viewToolbarWeightReps.orm.setText(OrmHelper.getOrm(weight, reps));
            adapterOrm.setItems(OrmHelper.getPercentageList(weight, reps, null));
            simpleTextWatcher.setRegister(true);
        }
    }

    private Integer getWeightLifted() {
        if (TextUtils.isEmpty(editWeight.getText().toString().trim())) {
            return null;
        }
        return Integer.valueOf(editWeight.getText().toString().trim());
    }

    private int getRepsPerformed() {
        return spinnerReps.getSelectedItemPosition() + 2;
    }

    private void removeWeightFocus() {
        InputMethodManager imm = (InputMethodManager) binding.getRoot().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(editWeight.getWindowToken(), 0);
        }
        binding.viewToolbarWeightReps.eqHolder.requestFocus();
    }

    private View.OnClickListener navListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        }
    };
}
