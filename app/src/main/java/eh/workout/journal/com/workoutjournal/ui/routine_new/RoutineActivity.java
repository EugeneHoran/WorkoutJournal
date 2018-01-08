package eh.workout.journal.com.workoutjournal.ui.routine_new;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import android.view.View;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.ActivityAddPlanBinding;
import eh.workout.journal.com.workoutjournal.util.Constants;
import eh.workout.journal.com.workoutjournal.util.DetailsTransition;

public class RoutineActivity extends AppCompatActivity {
    public static final String TAG_SELECT_LIFTS_FRAGMENT = "tag_lifts_fragment";
    public static final String TAG_DAY_SELECTOR_FRAGMENT = "tag_day_fragment";
    public static final String TAG_FINAL_FRAGMENT = "tag_final_fragment";
    private int appBarHeight;
    private int screenHeight;
    private boolean searching = false;

    private int pageNumber;
    private ActivityAddPlanBinding binding;
    private AppBarLayout.ScrollingViewBehavior behavior;
    private RoutineViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getIntent().getIntExtra(Constants.JOURNAL_PAGE_RESULT_CODE_PLAN, 5000);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_plan);
        setSupportActionBar(binding.toolbar);
        setTitle("Routine exercises");
        binding.toolbar.setNavigationOnClickListener(navListener);
        model = ViewModelProviders.of(this).get(RoutineViewModel.class);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) binding.container.getLayoutParams();
        behavior = (AppBarLayout.ScrollingViewBehavior) params.getBehavior();
        initContainerHeights();
        if (savedInstanceState == null) {
            RoutineLiftFragment fragment = RoutineLiftFragment.newInstance();
            initTransition(fragment);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment, TAG_SELECT_LIFTS_FRAGMENT)
                    .commit();
        }
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                binding.appBar.setExpanded(true, true);
                searching = false;
                int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
                switch (backStackCount) {
                    case 0:
                        binding.toolbarLayout.setTitle("Routine exercises");
                        behavior.setOverlayTop(0);
                        break;
                    case 1:
                        binding.toolbarLayout.setTitle("Routine days");
                        behavior.setOverlayTop(0);
                        break;
                    case 2:
                        binding.toolbarLayout.setTitle("Save routine");
                        behavior.setOverlayTop(158);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void finish() {
        setResultFrom();
        super.finish();
    }

    public void setResultFrom() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.JOURNAL_PAGE_RESULT_CODE_PLAN, pageNumber);
        setResult(model.routineAdded ? RESULT_OK : RESULT_CANCELED, returnIntent);
    }

    /**
     * Calculate frame height
     */
    private void initContainerHeights() {
        appBarHeight = binding.appBar.getLayoutParams().height;
        screenHeight = screenHeight();
        binding.container.requestLayout();
        binding.container.getLayoutParams().height = screenHeight - (appBarHeight);
        binding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                binding.container.requestLayout();
                binding.container.getLayoutParams().height = screenHeight - (appBarHeight + verticalOffset);
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (searching) {
            expandAppBar();
        } else {
            super.onBackPressed();
        }
    }

    public void expandAppBar() {
        if (!binding.appBar.isActivated()) {
            unlockAppBarOpen();
        } else {
            binding.appBar.setExpanded(true);
        }
    }

    public void collapseAppBar() {
        lockAppBarClosed();
    }

    public void lockAppBarClosed() {
        searching = true;
        invalidateOptionsMenu();
        binding.search.setVisibility(View.VISIBLE);
        binding.appBar.setExpanded(false, true);
        binding.appBar.setActivated(false);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) binding.appBar.getLayoutParams();
        lp.height = (int) getResources().getDimension(R.dimen.toolbar_height_collapsed);
        initContainerHeights();
    }

    public void unlockAppBarOpen() {
        searching = false;
        binding.search.setVisibility(View.GONE);
        binding.appBar.setExpanded(true, false);
        binding.appBar.setActivated(true);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) binding.appBar.getLayoutParams();
        lp.height = (int) getResources().getDimension(R.dimen.toolbar_height_expanded);
        initContainerHeights();
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

    private View.OnClickListener navListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onBackPressed();
        }
    };


    /**
     * Transition
     */

    private void initTransition(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementEnterTransition(new DetailsTransition());
            fragment.setEnterTransition(new Slide());
            fragment.setSharedElementReturnTransition(new DetailsTransition());
            fragment.setExitTransition(new Slide());
        }
    }

}
