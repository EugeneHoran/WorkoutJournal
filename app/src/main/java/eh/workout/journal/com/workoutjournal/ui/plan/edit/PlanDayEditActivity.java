package eh.workout.journal.com.workoutjournal.ui.plan.edit;

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
import eh.workout.journal.com.workoutjournal.databinding.ActivityPlanEditBinding;
import eh.workout.journal.com.workoutjournal.util.AppFactory;
import eh.workout.journal.com.workoutjournal.util.Constants;
import eh.workout.journal.com.workoutjournal.util.DetailsTransition;

public class PlanDayEditActivity extends AppCompatActivity {
    private static final String TAG_FRAGMENT_FINAL = "tag_frag_final";
    private int pageNumber;
    private int appBarHeight;
    private int screenHeight;
    private boolean searching = false;

    private ActivityPlanEditBinding binding;
    PlanDayEditViewModel model;
    private AppBarLayout.ScrollingViewBehavior behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_plan_edit);
        setSupportActionBar(binding.toolbar);
        setTitle("Edit plan");
        pageNumber = getIntent().getIntExtra(Constants.JOURNAL_PAGE_RESULT_CODE_PLAN, Constants.JOURNAL_PAGE_TODAY);
        String planId = getIntent().getStringExtra(Constants.EDIT_PLAN_ID);
        model = ViewModelProviders.of(this, new AppFactory(getApplication(), planId)).get(PlanDayEditViewModel.class);
        binding.toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        binding.toolbar.setNavigationOnClickListener(navListener);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) binding.container.getLayoutParams();
        behavior = (AppBarLayout.ScrollingViewBehavior) params.getBehavior();
        initContainerHeights();
        if (savedInstanceState == null) {
            if (behavior != null) {
                behavior.setOverlayTop(158);
            }
            PlanDayEditFinalFragment fragment = PlanDayEditFinalFragment.newInstance();
            initTransition(fragment);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.container,
                            fragment,
                            TAG_FRAGMENT_FINAL
                    )
                    .commit();
        }

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                searching = false;
                if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                    binding.toolbarLayout.setTitle("Plan exercises");
                    if (behavior != null) {
                        behavior.setOverlayTop(0);
                    }
                } else {
                    if (behavior != null) {
                        behavior.setOverlayTop(158);
                    }
                    binding.toolbarLayout.setTitle("Edit plan");
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
        setResult(RESULT_OK, returnIntent);
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
