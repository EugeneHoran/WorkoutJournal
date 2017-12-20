package eh.workout.journal.com.workoutjournal.ui.plan;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.ActivityAddPlanBinding;
import eh.workout.journal.com.workoutjournal.util.AppFactory;
import eh.workout.journal.com.workoutjournal.util.Constants;

public class AddPlanActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {
    private static final String TAG_SELECT_LIFTS_FRAGMENT = "tag_lifts_fragment";
    private static final String TAG_DAY_SELECTOR_FRAGMENT = "tag_day_fragment";

    private int pageNumber;
    private ActivityAddPlanBinding binding;
    private AddPlanViewModel model;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getIntent().getIntExtra(Constants.JOURNAL_PAGE_RESULT_CODE_PLAN, 5000);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_plan);
        fm = getSupportFragmentManager();
        model = ViewModelProviders.of(this, new AppFactory(getApplication())).get(AddPlanViewModel.class);
        binding.toolbar.setNavigationOnClickListener(navListener);
        binding.fab.setOnClickListener(fabListener);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        if (savedInstanceState == null) {
            fm.beginTransaction()
                    .replace(R.id.container, AddPlanSelectLiftsFragment.newInstance(), TAG_SELECT_LIFTS_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(pageNumber);
        super.onBackPressed();
    }

    @Override
    public void onBackStackChanged() {
        int stack = fm.getBackStackEntryCount();
        if (stack == 0) {
            binding.fab.setImageResource(R.drawable.ic_arrow_forward);
        } else {
            binding.fab.setImageResource(R.drawable.ic_check);
        }
    }

    public View.OnClickListener navListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onBackPressed();
        }
    };

    public View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (fm.getBackStackEntryCount() == 0) {
                AddPlanSelectLiftsFragment addPlanSelectLiftsFragment = (AddPlanSelectLiftsFragment) fm.findFragmentByTag(TAG_SELECT_LIFTS_FRAGMENT);
                if (addPlanSelectLiftsFragment != null) {
                    if (addPlanSelectLiftsFragment.getSelectedList().size() == 0) {
                        Snackbar.make(binding.fab, "Select lifts to continue", Snackbar.LENGTH_SHORT).show();
                        return;
                    } else {
                        model.setLifts(addPlanSelectLiftsFragment.getSelectedList());
                    }
                }
                fm.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.container, AddPlanDaySelectorFragment.newInstance(), TAG_DAY_SELECTOR_FRAGMENT).addToBackStack(TAG_DAY_SELECTOR_FRAGMENT)
                        .commit();
            } else {
                AddPlanDaySelectorFragment addPlanDaySelectorFragment = (AddPlanDaySelectorFragment) fm.findFragmentByTag(TAG_DAY_SELECTOR_FRAGMENT);
                if (addPlanDaySelectorFragment != null) {
                    model.setDaysString(addPlanDaySelectorFragment.getDaysString());
                }
                model.insertNewPlan();
                setResult(pageNumber);
                finish();
            }
        }
    };
}
