package eh.workout.journal.com.workoutjournal.ui.exercises;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.FragmentExerciseSelectorBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;
import eh.workout.journal.com.workoutjournal.ui.BaseFragment;

public class ExerciseSelectorFragment extends BaseFragment implements
        SearchView.OnQueryTextListener,
        ExerciseSelectorRecyclerAdapter.ExerciseAdapterInterface,
        ExerciseSelectorAddExerciseDialogFragment.AddExerciseDialogInterface {
    private static final String ARG_DATE_TIMESTAMP = "arg_date_timestamp";
    public final String toolbarTitle = "Select Exercise";

    public ExerciseSelectorFragment() {
    }

    public static ExerciseSelectorFragment newInstance(Long timestamp) {
        ExerciseSelectorFragment fragment = new ExerciseSelectorFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_DATE_TIMESTAMP, timestamp);
        fragment.setArguments(args);
        return fragment;
    }

    private ExerciseSelectorViewModel model;
    private Long timestamp;
    private ExerciseSelectorRecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(ExerciseSelectorViewModel.class);
        if (getArguments() != null) {
            timestamp = getArguments().getLong(ARG_DATE_TIMESTAMP);
        }
        adapter = new ExerciseSelectorRecyclerAdapter();
    }

    private FragmentExerciseSelectorBinding binding;
    private Toolbar toolbar;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_exercise_selector, container, false);
        toolbar = binding.viewToolbar != null ? binding.viewToolbar.toolbar : null;
        searchView = binding.viewToolbar != null ? binding.viewToolbar.searchView : null;
        binding.setFragment(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.inflateMenu(R.menu.menu_exercise_selector);
        searchView.setOnQueryTextListener(this);
        binding.recycler.setAdapter(adapter);
        adapter.setListener(this);
        observeExerciseList(model);
    }

    public View.OnClickListener navListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        }
    };

    public boolean searchVisible() {
        return searchView.getVisibility() == View.VISIBLE;
    }

    public void hideSearch() {
        searchView.setVisibility(View.GONE);
        searchView.setQuery(null, true);
        model.queryExercises(null);
        toolbar.inflateMenu(R.menu.menu_exercise_selector);
    }

    @Override
    public boolean onQueryTextChange(String s) {
        model.queryExercises(s);
        return false;
    }

    public void observeExerciseList(ExerciseSelectorViewModel model) {
        model.observeExerciseList().observe(this, new Observer<List<Object>>() {
            @Override
            public void onChanged(@Nullable List<Object> objectList) {
                adapter.setItems(objectList);
            }
        });
    }

    @Override
    public void exerciseSelected(ExerciseLiftEntity exerciseLiftEntity) {
        model.addExerciseToRecent(exerciseLiftEntity);
        navToAddExerciseFragment(binding.viewToolbar.appBar, exerciseLiftEntity.getId(), timestamp);
    }

    @Override
    public void removeFromRecent(ExerciseLiftEntity exerciseLiftEntity) {
        model.removeExerciseFromRecent(exerciseLiftEntity);
    }

    @Override
    public void saveNewExercise(ExerciseLiftEntity exerciseLiftEntity) {
        model.addExercise(exerciseLiftEntity);
    }

    public Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.action_add_exercise:
                    dialogNewExercise(ExerciseSelectorFragment.this);
                    break;
                case R.id.action_search_exercise:
                    toolbar.getMenu().clear();
                    searchView.setVisibility(View.VISIBLE);
                    break;
                default:
                    return false;
            }
            return false;
        }
    };

    @Override
    public boolean onQueryTextSubmit(String s) {
        model.queryExercises(s);
        return false;
    }
}
