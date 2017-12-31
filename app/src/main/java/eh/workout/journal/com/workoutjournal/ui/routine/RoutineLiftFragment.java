package eh.workout.journal.com.workoutjournal.ui.routine;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.transition.Slide;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar;

import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentRoutineLiftsBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;
import eh.workout.journal.com.workoutjournal.util.DetailsTransition;
import eh.workout.journal.com.workoutjournal.util.views.SimpleTextWatcher;

public class RoutineLiftFragment extends Fragment {
    public RoutineLiftFragment() {
    }

    public static RoutineLiftFragment newInstance() {
        return new RoutineLiftFragment();
    }

    private RoutineAddViewModel model;
    private RoutineLiftRecyclerAdapter adapter;
    private Unregistrar keyboardRegister;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            model = ViewModelProviders.of(getActivity()).get(RoutineAddViewModel.class);
        }
        adapter = new RoutineLiftRecyclerAdapter(true);
    }

    private FragmentRoutineLiftsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_routine_lifts, container, false);
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
        observeLiftList(model);
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
                if (adapter.getSelectedList().size() == 0) {
                    Snackbar.make(binding.fab, "Select lifts to continue", Snackbar.LENGTH_SHORT).show();
                    return;
                } else {
                    model.getLiftList().setValue(adapter.getAllCheckedList());
                    model.setLiftListSelected(adapter.getSelectedList());
                }
                RoutineDayFragment fragment = RoutineDayFragment.newInstance();
                initTransition(fragment);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addSharedElement(binding.fab, "fab")
                        .replace(R.id.container, fragment, RoutineAddActivity.TAG_DAY_SELECTOR_FRAGMENT).addToBackStack(RoutineAddActivity.TAG_DAY_SELECTOR_FRAGMENT)
                        .commit();
            }
        });
    }

    private void observeLiftList(RoutineAddViewModel model) {
        model.getLiftList().observe(this, new Observer<List<ExerciseLiftEntity>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseLiftEntity> exerciseLiftEntities) {
                adapter.setItems(exerciseLiftEntities);
            }
        });
    }

    private void initTransition(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementEnterTransition(new DetailsTransition());
            fragment.setEnterTransition(new Slide());
            fragment.setSharedElementReturnTransition(new DetailsTransition());
            fragment.setExitTransition(new Slide());
        }
    }

    @Override
    public void onDetach() {
        keyboardRegister.unregister();
        super.onDetach();
    }
}
