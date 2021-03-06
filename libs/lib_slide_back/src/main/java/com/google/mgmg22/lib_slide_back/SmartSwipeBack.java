package com.google.mgmg22.lib_slide_back;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.google.mgmg22.lib_slide_back.consumer.ActivitySlidingBackConsumer;

import java.util.ArrayList;


/**
 * A tool for achieving a one-line global activity swipe back via using some {@link SwipeConsumer}
 * simple usage:
 * <code>
 * <p>
 * //add swipe translucent back performance for all activities
 * // (default direction: left, previous activity related factor:0.5F)
 * SmartSwipeBack.activitySlidingBack(this, new SmartSwipeBack.ActivitySwipeBackFilter() {
 * public boolean onFilter(Activity activity) {
 * return !(activity instanceof MainActivity);
 * }
 * });
 * <p>
 * //add swipe back like mobile QQ (activity keep stay and finish activity with release velocity)
 * //SmartSwipeBack.activityStayBack(this, null);
 * <p>
 * //add bezier swipe back like XiaoMi (swipe with bezier back consumer at edge of screen)
 * //SmartSwipeBack.activityBezierBack(this, null);
 * </code>
 *
 * @author billy.qi
 */
public class SmartSwipeBack {
    public static final ArrayList<Activity> ACTIVITIES = new ArrayList<>();
    private static ActivitySwipeBackListener activitySwipeBackListener;

    /**
     * The core function for global activity swipe back
     *
     * @param application application
     * @param factory     factory to create SwipeConsumer for each Activity
     * @param filter      filter of activity, to determine which activity should finish via swipe motion
     */
    public static void activityBack(Application application, SwipeBackConsumerFactory factory, ActivitySwipeBackFilter filter) {
        if (activitySwipeBackListener == null) {
            activitySwipeBackListener = new ActivitySwipeBackListener(factory, filter);
        } else {
            application.unregisterActivityLifecycleCallbacks(activitySwipeBackListener);
            activitySwipeBackListener.mFactory = factory;
            activitySwipeBackListener.mFilter = filter;
        }
        application.registerActivityLifecycleCallbacks(activitySwipeBackListener);
    }


    ////////////////////////////////////////////
    //
    //  swipe back with ActivitySlidingBackConsumer
    //
    ////////////////////////////////////////////

    /**
     * find previous activity
     *
     * @param fromActivity the given activity to find its previous
     * @return the previous activity if exists
     * @see ActivitySlidingBackConsumer
     */
    public static Activity findPreviousActivity(Activity fromActivity) {
        if (fromActivity != null) {
            int index = ACTIVITIES.indexOf(fromActivity);
            if (index > 0) {
                return ACTIVITIES.get(index - 1);
            }
        }
        return null;
    }


    public interface SwipeBackConsumerFactory {
        /**
         * Create SwipeConsumer to do swipe back business for activity
         *
         * @param activity activity to wrap with swipe back
         * @return SwipeConsumer
         */
        SwipeConsumer createSwipeBackConsumer(Activity activity);
    }

    public interface ActivitySwipeBackFilter {
        /**
         * Determine whether the activity parameter should swipe back
         *
         * @param activity The activity to wrap or not
         * @return true: need to wrap with swipe back, false: do not wrap
         */
        boolean onFilter(Activity activity);
    }

    public static class ActivitySwipeBackListener implements Application.ActivityLifecycleCallbacks {
        private SwipeBackConsumerFactory mFactory;
        private ActivitySwipeBackFilter mFilter;

        ActivitySwipeBackListener(SwipeBackConsumerFactory factory, ActivitySwipeBackFilter filter) {
            this.mFactory = factory;
            this.mFilter = filter;
        }

        @Override
        public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
            ACTIVITIES.add(activity);
            if (mFactory == null) {
                return;
            }
            if ((mFilter != null && !mFilter.onFilter(activity))) {
                return;
            }
            SmartSwipe.wrap(activity).addConsumer(mFactory.createSwipeBackConsumer(activity));
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            ACTIVITIES.remove(activity);
        }
    }

}
