package eh.workout.journal.com.workoutjournal.ui.plan.edit;

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
import android.widget.EditText;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentAddPlanSelectLiftsBinding;
import eh.workout.journal.com.workoutjournal.ui.routine.RoutineLiftRecyclerAdapter;
import eh.workout.journal.com.workoutjournal.util.views.SimpleTextWatcher;


public class EditPlanLiftSelectorFragment extends Fragment {
    public EditPlanLiftSelectorFragment() {
    }

    public static EditPlanLiftSelectorFragment newInstance() {
        return new EditPlanLiftSelectorFragment();
    }

    private EditPlanViewModel model;
    private RoutineLiftRecyclerAdapter adapter;
    private Unregistrar keyboardRegister;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            model = ViewModelProviders.of(getActivity()).get(EditPlanViewModel.class);
        }
        adapter = new RoutineLiftRecyclerAdapter(true);
        adapter.setItems(model.getAllExercises().getValue());
    }

    private FragmentAddPlanSelectLiftsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_plan_select_lifts, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recycler.setAdapter(adapter);
        new SimpleTextWatcher(new SimpleTextWatcher.SimpleTextWatcherInterface() {
            @Override
            public void onTextChanged(EditText editText, String string, CharSequence charSequence, int count) {
                adapter.getFilter().filter(charSequence);
                binding.recycler.scrollToPosition(0);
            }
        }, true).registerEditText(binding.editSearch);

        if (getActivity() != null) {
            keyboardRegister = KeyboardVisibilityEvent.registerEventListener(getActivity(), new KeyboardVisibilityEventListener() {
                @Override
                public void onVisibilityChanged(boolean isOpen) {
                    binding.title.setVisibility(isOpen ? View.GONE : View.VISIBLE);
                }
            });
        }

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    if (adapter.getSelectedList().size() == 0) {
                        Snackbar.make(binding.fab, "Select lifts to continue", Snackbar.LENGTH_SHORT).show();
                        return;
                    } else {
                        model.getAllExercises().setValue(adapter.getAllCheckedList());
                        model.getAllSelectedExercises().setValue(adapter.getSelectedList());
                    }
                    getActivity().onBackPressed();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        keyboardRegister.unregister();
        super.onDetach();
    }
}
