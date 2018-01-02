package eh.workout.journal.com.workoutjournal.ui.routine.edit;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.ActivityEditRoutineBinding;
import eh.workout.journal.com.workoutjournal.util.AppFactory;
import eh.workout.journal.com.workoutjournal.util.Constants;

public class EditRoutineActivity extends AppCompatActivity {

    private int pageNumber;
    private EditRoutineViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getIntent().getIntExtra(Constants.JOURNAL_PAGE_RESULT_CODE_PLAN, Constants.JOURNAL_PAGE_TODAY);
        String planId = getIntent().getStringExtra(Constants.EDIT_PLAN_ID);
        ActivityEditRoutineBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_routine);
        initToolbar(binding);
        model = ViewModelProviders.of(this, new AppFactory(getApplication(), planId)).get(EditRoutineViewModel.class);
        model.initEditData();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, EditRoutineFragment.newInstance()).commit();
        }
    }

    private void initToolbar(ActivityEditRoutineBinding binding) {
        binding.toolbar.setNavigationOnClickListener(navListener);
        binding.toolbar.inflateMenu(R.menu.menu_edit_move_delete);
        Menu menu = binding.toolbar.getMenu();
        menu.findItem(R.id.action_move).setVisible(false);
        menu.findItem(R.id.action_edit).setVisible(false);
        menu.findItem(R.id.action_delete).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_delete) {
                    model.deletePlan();
                    onBackPressed();
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResultFrom();
        super.onBackPressed();
    }

    @Override
    public void finish() {
        setResultFrom();
        super.finish();
    }

    public void setResultFrom() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.JOURNAL_PAGE_RESULT_CODE_PLAN, pageNumber);
        setResult(RESULT_OK, returnIntent);
    }

    public View.OnClickListener navListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onBackPressed();
        }
    };
}
