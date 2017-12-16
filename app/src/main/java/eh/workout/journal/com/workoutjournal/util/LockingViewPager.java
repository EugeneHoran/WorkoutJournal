package eh.workout.journal.com.workoutjournal.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class LockingViewPager extends ViewPager {
    private boolean enableSwiping = true;

    public LockingViewPager(Context context) {
        super(context);
    }

    public LockingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void enableSwiping(boolean enableSwiping) {
        this.enableSwiping = enableSwiping;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return enableSwiping && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return enableSwiping && super.onTouchEvent(event);
    }
}
