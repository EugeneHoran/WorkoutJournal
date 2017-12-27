package eh.workout.journal.com.workoutjournal.ui.plan;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.transition.Slide;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.ActivityAddPlanBinding;
import eh.workout.journal.com.workoutjournal.util.AppFactory;
import eh.workout.journal.com.workoutjournal.util.Constants;
import eh.workout.journal.com.workoutjournal.util.DetailsTransition;

public class AddPlanActivity extends AppCompatActivity {
    public static final String TAG_SELECT_LIFTS_FRAGMENT = "tag_lifts_fragment";
    public static final String TAG_DAY_SELECTOR_FRAGMENT = "tag_day_fragment";
    public static final String TAG_FINAL_FRAGMENT = "tag_final_fragment";

    private int pageNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getIntent().getIntExtra(Constants.JOURNAL_PAGE_RESULT_CODE_PLAN, 5000);
        ActivityAddPlanBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_plan);
        ViewModelProviders.of(this, new AppFactory(getApplication())).get(AddPlanViewModel.class);
        binding.toolbar.setNavigationOnClickListener(navListener);
        if (savedInstanceState == null) {
            AddPlanSelectLiftsFragment fragment = AddPlanSelectLiftsFragment.newInstance();
            initTransition(fragment);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment, TAG_SELECT_LIFTS_FRAGMENT)
                    .commit();
        }
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
