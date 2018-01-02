package eh.workout.journal.com.workoutjournal.ui.plan;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.transition.Slide;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.ActivityAddPlanBinding;
import eh.workout.journal.com.workoutjournal.util.DetailsTransition;

public class PlanAddActivity extends AppCompatActivity {
    private static final String TAG_FRAGMENT_LIFTS = "tag_fragment_lifts";
    private int appBarHeight;
    private int screenHeight;

    private ActivityAddPlanBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_plan);
        setSupportActionBar(binding.toolbar);
        setTitle("Select exercises");
        binding.toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        binding.toolbar.setNavigationOnClickListener(navListener);
        ViewModelProviders.of(this).get(PlanViewModel.class);
        initContainerHeights();
        if (savedInstanceState == null) {
            PlanLiftFragment fragment = PlanLiftFragment.newInstance();
            initTransition(fragment);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.container,
                            fragment,
                            TAG_FRAGMENT_LIFTS
                    )
                    .commit();
        }
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) binding.container.getLayoutParams();
                AppBarLayout.ScrollingViewBehavior behavior = (AppBarLayout.ScrollingViewBehavior) params.getBehavior();
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    binding.toolbarLayout.setTitle("Select exercises");
                    if (behavior != null) {
                        behavior.setOverlayTop(0);
                    }
                } else {
                    if (behavior != null) {
                        behavior.setOverlayTop(158);
                    }
                    binding.toolbarLayout.setTitle("Add plan");
                }
            }
        });
    }

    public void expandAppBar() {
        binding.appBar.setExpanded(true);
    }

    public void collapseAppBar() {
        binding.appBar.setExpanded(false);
    }

    private View.OnClickListener navListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onBackPressed();
        }
    };

    private void initTransition(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementEnterTransition(new DetailsTransition());
            fragment.setEnterTransition(new Slide());
            fragment.setSharedElementReturnTransition(new DetailsTransition());
            fragment.setExitTransition(new Slide());
        }
    }

    /**
     * Calculate frame height
     */
    private void initContainerHeights() {
        appBarHeight = binding.appBar.getLayoutParams().height;
        screenHeight = screenHeight();
        binding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                binding.container.requestLayout();
                binding.container.getLayoutParams().height = screenHeight - (appBarHeight + verticalOffset);
            }
        });
    }

    private int screenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels - statusBarHeight();
    }

    public int statusBarHeight() {
        final Resources resources = getResources();
        final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        } else {
            return (int) Math.ceil((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 24 : 25) * resources.getDisplayMetrics().density);
        }
    }

}
