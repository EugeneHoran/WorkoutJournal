package eh.workout.journal.com.workoutjournal.ui.plan;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.ActivityAddPlanBinding;
import eh.workout.journal.com.workoutjournal.util.AppFactory;

public class AddPlanActivity extends AppCompatActivity {
    ActivityAddPlanBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_plan);
        ViewModelProvider.Factory factory = new AppFactory(getApplication());
        ViewModelProviders.of(this, factory).get(AddPlanViewModel.class);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, AddPlanSelectLiftsFragment.newInstance()).commit();
        }
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left
                                    , R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.container, AddPlanDaySelectorFragment.newInstance()).addToBackStack(null).commit();
                }
            }
        });
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int stack = getSupportFragmentManager().getBackStackEntryCount();
                if (stack == 0) {
                    binding.next.setText("NEXT");
                } else {
                    binding.next.setText("FINISH");
                }
            }
        });
    }
}
