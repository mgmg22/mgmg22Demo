package com.google.mgmg22.lib_slide_back;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.AbsSeekBar;

import com.google.mgmg22.lib_slide_back.internal.ScrimView;
import com.google.mgmg22.lib_slide_back.internal.SwipeHelper;
import com.google.mgmg22.lib_slide_back.internal.ViewCompat;
import com.google.mgmg22.lib_slide_back.listener.SimpleSwipeListener;
import com.google.mgmg22.lib_slide_back.listener.SwipeListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Base class to consume swipe motion event,
 * all subclasses should manage the following 4 direction swipes:
 * <pre>
 *  1. {@link #DIRECTION_LEFT}
 * </pre>
 * <pre>
 * To consume Motion Event via:
 *  when contentView is idle, SwipeConsumer hold the swipe motion event via {@link #tryAcceptMoving(int, float, float, float, float)}
 *  In other cases, when contentView is settling, SwipeConsumer hold the swipe motion event via {@link #tryAcceptSettling(int, float, float)}
 * </pre>
 *
 * @author billy.qi
 */
public abstract class SwipeConsumer {
    public static final int DIRECTION_NONE = 0;
    public static final int DIRECTION_LEFT = 1;
    public static final int DIRECTION_HORIZONTAL = DIRECTION_LEFT ;
    public static final int DIRECTION_ALL = DIRECTION_HORIZONTAL;
    public static final int RELEASE_MODE_NONE = 0;
    public static final int RELEASE_MODE_AUTO_CLOSE = 1;
    public static final int RELEASE_MODE_AUTO_OPEN = 2;
    public static final int RELEASE_MODE_AUTO_OPEN_CLOSE = RELEASE_MODE_AUTO_OPEN | RELEASE_MODE_AUTO_CLOSE;
    public static final int RELEASE_MODE_HOLE_OPEN = 4;
    public static final float PROGRESS_CLOSE = 0F;
    public static final float PROGRESS_OPEN = 1F;
    public static int DEFAULT_OPEN_DISTANCE_IN_DP = 150;
    protected final List<SwipeListener> mListeners = new CopyOnWriteArrayList<>();
    /**
     * The wrapper which this SwipeConsumer attached to, it would not be null
     */
    protected SmartSwipeWrapper mWrapper;
    /**
     * current swipe direction
     */
    protected int mDirection;
    /**
     * cached distance by last swipe
     */
    protected int mCachedSwipeDistanceX, mCachedSwipeDistanceY;
    /**
     * distance by current swipe
     */
    protected int mCurSwipeDistanceX, mCurSwipeDistanceY;
    protected int mSwipeOpenDistance;
    protected boolean mOpenDistanceSpecified;
    protected int mSwipeMaxDistance;
    /**
     * distance to display for subclass to show UI
     */
    protected int mCurDisplayDistanceX, mCurDisplayDistanceY;
    protected float mProgress;
    protected volatile boolean mSwiping;


    /////////////////////////////////////////////
    //
    //  common settings  ↓↓↓↓↓
    //
    /////////////////////////////////////////////
    protected SwipeHelper mSwipeHelper;
    /**
     * The interpolator when touch released, it would be used by {@link SwipeHelper#setInterpolator(Context, Interpolator)}
     */
    protected Interpolator mInterpolator;
    /**
     * The edge pixel size for SwipeConsumer to consume touch event
     *
     * @see #tryAcceptMoving(int, float, float, float, float)
     */
    protected int mEdgeSize;
    protected int mReleaseMode = RELEASE_MODE_AUTO_CLOSE;
    /**
     * to open this SwipeConsumer, distance should not less than this value
     */
    protected int mOpenDistance;
    protected float mOverSwipeFactor = 0F;
    protected boolean mDisableSwipeOnSettling;
    protected Integer mMaxSettleDuration;
    /**
     * by default: enable nested scroll and nested fly for all direction
     */
    protected int mEnableNested = (DIRECTION_ALL << 4) | DIRECTION_ALL;
    /**
     * the wrapper width, it`s value assigned via {@link #onMeasure(int, int)}
     */
    protected int mWidth;
    /**
     * the wrapper height, it`s value assigned via {@link #onMeasure(int, int)}
     */
    protected int mHeight;
    /**
     * set auto close or not when SmartSwipeWrapper.onDetachedFromWindow() has been called.
     * default is true excepted Activity???BackConsumer(such as {@link ActivitySlidingBackConsumer})
     */
    protected boolean mAutoCloseOnWrapperDetachedFromWindow = true;
    /**
     * default enabled direction: none
     */
    private int mEnableDirection = DIRECTION_NONE;
    private int mLockDirection = DIRECTION_NONE;
    private final float mSensitivity = 1F;

    /**
     * always swipe by default when settling
     *
     * @param pointerId pointer id
     * @param downX     motion event x for pointerId
     * @param downY     motion event y for pointerId
     * @return swipe or not
     */
    public boolean tryAcceptSettling(int pointerId, float downX, float downY) {
        if (isNestedAndDisabled(pointerId, mDirection)) {
            return false;
        }
        if (mDisableSwipeOnSettling && getDragState() == SwipeHelper.STATE_SETTLING) {
            return false;
        }
        return isDirectionEnable(mDirection) && !isDirectionLocked(mDirection);
    }

    public boolean tryAcceptMoving(int pointerId, float downX, float downY, float dx, float dy) {
        int dir = calSwipeDirection(pointerId, downX, downY, dx, dy);
        boolean handle = dir != DIRECTION_NONE;
        if (handle) {
            mDirection = dir;
        }
        return handle;
    }

    public int calSwipeDirection(int pointerId, float downX, float downY, float dx, float dy) {
        if (mDirection == DIRECTION_NONE) {
            if (pointerId == SwipeHelper.POINTER_NESTED_SCROLL && ((mEnableNested) & DIRECTION_ALL) == 0
                    || pointerId == SwipeHelper.POINTER_NESTED_FLY && ((mEnableNested >> 4) & DIRECTION_ALL) == 0) {
                //nested scrolling or fling, but all direction disabled for nested scrolling & fling
                return DIRECTION_NONE;
            }
        }
        float absX = Math.abs(dx);
        float absY = Math.abs(dy);
        if ((mCurSwipeDistanceX != 0 || mCurSwipeDistanceY != 0)) {
            if (dx == 0 && dy == 0) {
                return DIRECTION_NONE;
            }
            //already swiped, checkout whether the swipe direction as same as last one
            if ((mDirection & DIRECTION_HORIZONTAL) > 0 && absX > absY) {
                if (!isDirectionLocked(mDirection) && !isNestedAndDisabled(pointerId, mDirection)) {
                    //it seams like it wants to continue current swiping, now, check whether any child can scroll
                    boolean canChildScroll = canChildScroll(mWrapper, mDirection, pointerId, downX, downY, dx, dy);
                    return canChildScroll ? DIRECTION_NONE : mDirection;
                }
            }
            return DIRECTION_NONE;
        }
        int dir = DIRECTION_NONE;
        boolean handle = false;
        if (absX == 0 && absY == 0) {
            if (mEdgeSize > 0) {
                if (isLeftEnable() && downX <= mEdgeSize) {
                    dir = DIRECTION_LEFT;
                    handle = true;
                }
            }
        } else {
            if (absX > absY) {
                if (dx > 0 && isLeftEnable()) {
                    dir = DIRECTION_LEFT;
                    handle = true;
                }
            }
            if (handle) {
                if (mEdgeSize > 0) {
                    //edge size has set, just check it, ignore all child views` scroll ability (also include child Wrappers) inside this Wrapper
                    switch (dir) {
                        case DIRECTION_LEFT:
                            handle = downX <= mEdgeSize;
                            break;
                        default:
                    }
                } else {
                    //no edge size set, check any child can scroll on this direction
                    // (absolutely, also check whether child Wrapper can consume this swipe motion event)
                    handle = !canChildScroll(mWrapper, dir, pointerId, downX, downY, dx, dy);
                }
            }
        }
        if (handle) {
            // nested fling and enabled
            if (pointerId == SwipeHelper.POINTER_NESTED_FLY && isNestedFlyEnable(dir)) {
                return dir;
            }
            if (isDirectionLocked(dir)) {
                return DIRECTION_NONE;
            }
            if (isNestedAndDisabled(pointerId, dir)) {
                return DIRECTION_NONE;
            }
            return dir;
        }
        return DIRECTION_NONE;
    }

    protected boolean isNestedAndDisabled(int pointerId, int direction) {
        return pointerId == SwipeHelper.POINTER_NESTED_SCROLL && !isNestedScrollEnable(direction)
                || pointerId == SwipeHelper.POINTER_NESTED_FLY && !isNestedFlyEnable(direction);
    }

    protected boolean canChildScroll(ViewGroup parentView, int direction, int pointerId, float downX, float downY, float dx, float dy) {
        boolean canScroll = false;
        View topChild = findTopChildUnder(parentView, (int) downX, (int) downY);
        if (topChild instanceof SmartSwipeWrapper) {
            SmartSwipeWrapper wrapper = (SmartSwipeWrapper) topChild;
            SwipeHelper swipeHelper = wrapper.mHelper;
            SwipeConsumer consumer;
            if (swipeHelper != null && (consumer = swipeHelper.getSwipeConsumer()) != null) {
                int dir = consumer.calSwipeDirection(pointerId, downX, downY, dx, dy);
                canScroll = dir != DIRECTION_NONE && consumer.getProgress() < PROGRESS_OPEN;
            } else {
                List<SwipeConsumer> allConsumers = wrapper.getAllConsumers();
                for (SwipeConsumer sc : allConsumers) {
                    if (sc != null && sc.calSwipeDirection(pointerId, downX, downY, dx, dy) != DIRECTION_NONE) {
                        canScroll = true;
                        break;
                    }
                }
            }
        } else if (topChild != null) {
            switch (direction) {
                case DIRECTION_LEFT:
                    if (topChild instanceof AbsSeekBar) {
                        AbsSeekBar seekBar = (AbsSeekBar) topChild;
                        int progress = seekBar.getProgress();
                        int min = 0;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            min = seekBar.getMin();
                        }
                        int max = seekBar.getMax();
                        canScroll = dx > 0 && progress < max || dx < 0 && progress > min;
                    } else {
                        canScroll = topChild.canScrollHorizontally(dx > 0 ? -1 : 1);
                    }
                    break;
                default:
            }
        }
        if (!canScroll && topChild instanceof ViewGroup) {
            return canChildScroll((ViewGroup) topChild, direction, pointerId, downX - topChild.getLeft(), downY - topChild.getTop(), dx, dy);
        }
        return canScroll;
    }

    public void onSwipeAccepted(int activePointerId, boolean settling, float initialMotionX, float initialMotionY) {
        mSwiping = true;
        ViewParent parent = mWrapper.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        if ((mCurSwipeDistanceX != 0 || mCurSwipeDistanceY != 0)) {
            mCachedSwipeDistanceX = mCurSwipeDistanceX;
            mCachedSwipeDistanceY = mCurSwipeDistanceY;
        }
        mSwipeOpenDistance = getSwipeOpenDistance();
        if (mOverSwipeFactor > 0) {
            mSwipeMaxDistance = (int) (mSwipeOpenDistance * (1 + mOverSwipeFactor));
        } else {
            mSwipeMaxDistance = mSwipeOpenDistance;
        }
        notifySwipeStart();
    }

    public void onSwipeReleased(float xVelocity, float yVelocity) {
        ViewParent parent = mWrapper.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(false);
        }
        notifySwipeRelease(xVelocity, yVelocity);
        if (mProgress >= PROGRESS_OPEN) {
            if ((mReleaseMode & RELEASE_MODE_HOLE_OPEN) == RELEASE_MODE_HOLE_OPEN) {
                smoothSlideTo(PROGRESS_OPEN);
                return;
            }
        }
        switch (mReleaseMode & RELEASE_MODE_AUTO_OPEN_CLOSE) {
            default:
            case RELEASE_MODE_NONE:
                break;
            case RELEASE_MODE_AUTO_CLOSE:
                if (mProgress >= PROGRESS_OPEN) {
                    onOpened();
                }
                smoothSlideTo(PROGRESS_CLOSE);
                break;
            case RELEASE_MODE_AUTO_OPEN:
                smoothSlideTo(PROGRESS_OPEN);
                break;
            case RELEASE_MODE_AUTO_OPEN_CLOSE:
                smoothOpenOrClose(xVelocity, yVelocity);
                break;
        }
    }

    protected void smoothOpenOrClose(float xVelocity, float yVelocity) {
        boolean open = false;
        switch (mDirection) {
            case DIRECTION_LEFT:
                open = xVelocity > 0 || xVelocity == 0 && mProgress > 0.5F;
                break;
            default:
                break;
        }
        smoothSlideTo(open ? PROGRESS_OPEN : PROGRESS_CLOSE);
    }

    public void setCurrentStateAsClosed() {
        onClosed();
        reset();
    }

    protected void notifySwipeOpened() {
        for (SwipeListener listener : mListeners) {
            if (listener != null) {
                listener.onSwipeOpened(mWrapper, this, mDirection);
            }
        }
    }

    protected void notifySwipeClosed() {
        for (SwipeListener listener : mListeners) {
            if (listener != null) {
                listener.onSwipeClosed(mWrapper, this, mDirection);
            }
        }
    }

    protected void notifyAttachToWrapper() {
        for (SwipeListener listener : mListeners) {
            if (listener != null) {
                listener.onConsumerAttachedToWrapper(mWrapper, this);
            }
        }
    }

    protected void notifyDetachFromWrapper() {
        for (SwipeListener listener : mListeners) {
            if (listener != null) {
                listener.onConsumerDetachedFromWrapper(mWrapper, this);
            }
        }
    }

    protected void notifySwipeStateChanged(int state) {
        for (SwipeListener listener : mListeners) {
            if (listener != null) {
                listener.onSwipeStateChanged(mWrapper, this, state, mDirection, mProgress);
            }
        }
    }

    protected void notifySwipeStart() {
        for (SwipeListener listener : mListeners) {
            if (listener != null) {
                listener.onSwipeStart(mWrapper, this, mDirection);
            }
        }
    }

    protected void notifySwipeRelease(float xVelocity, float yVelocity) {
        for (SwipeListener listener : mListeners) {
            if (listener != null) {
                listener.onSwipeRelease(mWrapper, this, mDirection, mProgress, xVelocity, yVelocity);
            }
        }
    }

    protected void notifySwipeProgress(boolean settling) {
        for (SwipeListener listener : mListeners) {
            if (listener != null) {
                listener.onSwipeProcess(mWrapper, this, mDirection, settling, mProgress);
            }
        }
    }

    public int getHorizontalRange(float dx, float dy) {
        if (mCurSwipeDistanceX != 0
                || dx > 0 && isLeftEnable() && !isLeftLocked()) {
            return getSwipeOpenDistance();
        }
        return 0;
    }

    public int getVerticalRange(float dx, float dy) {
        if (mCurSwipeDistanceY != 0) {
            return getSwipeOpenDistance();
        }
        return 0;
    }

    public int clampDistanceHorizontal(int distanceX, int dx) {
        if (mCachedSwipeDistanceX != 0) {
            distanceX += mCachedSwipeDistanceX;
            mCachedSwipeDistanceX = 0;
        }
        if ((mDirection & DIRECTION_LEFT) > 0 && isLeftEnable()) {
            return SmartSwipe.ensureBetween(distanceX, 0, mSwipeMaxDistance);
        }
        return 0;
    }

    public int clampDistanceVertical(int distanceY, int dy) {
        if (mCachedSwipeDistanceY != 0) {
            distanceY += mCachedSwipeDistanceY;
            mCachedSwipeDistanceY = 0;
        }
        return 0;
    }

    /**
     * The core function to change layouts
     *
     * @param clampedDistanceX swipe horizontal distance clamped via {@link #clampDistanceHorizontal(int, int)}
     * @param clampedDistanceY swipe vertical distance clamped via {@link #clampDistanceVertical(int, int)}
     * @param dx               delta x distance from last call
     * @param dy               delta y distance from last call
     * @see #clampDistanceHorizontal(int, int)
     * @see #clampDistanceVertical(int, int)
     */
    public void onSwipeDistanceChanged(int clampedDistanceX, int clampedDistanceY, int dx, int dy) {
        int maxDistance = getOpenDistance();
        if (maxDistance <= 0) {
            return;
        }
        float lastProgress = this.mProgress;
        if (clampedDistanceX != mCurSwipeDistanceX || clampedDistanceY != mCurSwipeDistanceY) {
            mCurSwipeDistanceX = clampedDistanceX;
            mCurSwipeDistanceY = clampedDistanceY;

            if (mSwipeOpenDistance <= 0) {
                mProgress = 0;
            } else {
                switch (mDirection) {
                    case DIRECTION_LEFT:
                        mProgress = Math.abs((float) mCurSwipeDistanceX / mSwipeOpenDistance);
                        break;
                    default:
                }
            }
            if ((mDirection & DIRECTION_HORIZONTAL) > 0) {
                int realDistanceX = clampedDistanceX;
                dx = realDistanceX - mCurDisplayDistanceX;
                dy = 0;
                mCurDisplayDistanceX = realDistanceX;
            }
            onDisplayDistanceChanged(mCurDisplayDistanceX, mCurDisplayDistanceY, dx, dy);
        }
        if (this.mProgress != lastProgress) {
            boolean settling = getDragState() == SwipeHelper.STATE_SETTLING;
            notifySwipeProgress(settling);
        }
    }

    /**
     * Called to refresh UI when swipe distance is changed. final value has clamped and modified by Resistor(if it not null)
     *
     * @param distanceXToDisplay distance changed in pixels along the X axis to show UI
     * @param distanceYToDisplay distance changed in pixels along the Y axis to show UI
     * @param dx                 Change in X position from the last call
     * @param dy                 Change in Y position from the last call
     */
    protected abstract void onDisplayDistanceChanged(int distanceXToDisplay, int distanceYToDisplay, int dx, int dy);

    /**
     * Called when SwipeConsumer add to Wrapper
     *
     * @param wrapper     Wrapper to add to
     * @param swipeHelper SwipeHelper bind to this SwipeConsumer
     */
    public void onAttachToWrapper(SmartSwipeWrapper wrapper, SwipeHelper swipeHelper) {
        this.mWrapper = wrapper;
        if (this.mOpenDistance == 0) {
            this.mOpenDistance = SmartSwipe.dp2px(DEFAULT_OPEN_DISTANCE_IN_DP, wrapper.getContext());
        }
        this.mSwipeHelper = swipeHelper;
        if (mMaxSettleDuration != null) {
            mSwipeHelper.setMaxSettleDuration(mMaxSettleDuration);
        }
        //compat for xml usage
        if (mWrapper.isInflateFromXml()) {
            initChildrenFormXml();
        }
        notifyAttachToWrapper();
    }

    /**
     * init children via xml usage of {@link SmartSwipeWrapper}
     */
    protected void initChildrenFormXml() {

    }

    /**
     * Called when removed from Wrapper
     */
    public void onDetachFromWrapper() {
        notifyDetachFromWrapper();
        reset();
    }

    /**
     * Called when Wrapper#dispatchDraw(Canvas) called
     *
     * @param canvas canvas
     */
    public void dispatchDraw(Canvas canvas) {

    }

    /**
     * Called when Wrapper#onDraw(Canvas) called
     *
     * @param canvas canvas
     */
    public void onDraw(Canvas canvas) {

    }

    /**
     * Called when Wrapper#onMeasure(int, int) called
     *
     * @param widthMeasureSpec  widthMeasureSpec
     * @param heightMeasureSpec heightMeasureSpec
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = mWrapper.getMeasuredWidth();
        mHeight = mWrapper.getMeasuredHeight();
    }

    /**
     * Called when Wrapper#onLayout(boolean, int, int, int, int) called
     *
     * @param changed changed or not
     * @param left    left position
     * @param top     top position
     * @param right   right position
     * @param bottom  bottom position
     * @return true:    Wrapper will not call super.onLayout(...),
     * false:   Wrapper will call super.onLayout(...) to layout children
     */
    public boolean onLayout(boolean changed, int left, int top, int right, int bottom) {
        return false;
    }


    /**
     * Find the topmost child under the given point within the parent view's coordinate system.
     *
     * @param parentView the parent view
     * @param x          X position to test in the parent's coordinate system
     * @param y          Y position to test in the parent's coordinate system
     * @return The topmost child view under (x, y) or null if none found.
     */
    public View findTopChildUnder(ViewGroup parentView, int x, int y) {
        final int childCount = parentView.getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            final View child = parentView.getChildAt(i);
            if (x >= child.getLeft() && x < child.getRight()
                    && y >= child.getTop() && y < child.getBottom()
                    && child.getVisibility() == View.VISIBLE) {
                if (child instanceof ScrimView && !child.isFocusable() && !child.isClickable()) {
                    continue;
                }
                return child;
            }
        }
        return null;
    }

    public void onStateChanged(int state) {
        notifySwipeStateChanged(state);
        if (state == SwipeHelper.STATE_IDLE) {
            mSwiping = false;
            if (mProgress >= 1F) {
                onOpened();
            } else if (mProgress <= 0F) {
                onClosed();
            }
        }
    }

    protected void onOpened() {
        notifySwipeOpened();
    }

    protected void onClosed() {
        notifySwipeClosed();
        mDirection = DIRECTION_NONE;
    }

    protected void reset() {
        mDirection = DIRECTION_NONE;
        mProgress = PROGRESS_CLOSE;
        mCachedSwipeDistanceX = mCurSwipeDistanceX = mCurDisplayDistanceX = 0;
        mCachedSwipeDistanceY = mCurSwipeDistanceY = mCurDisplayDistanceY = 0;
    }

    public Interpolator getInterpolator() {
        return mInterpolator;
    }


    public float getSensitivity() {
        return mSensitivity;
    }

    /**
     * set the mode when released (default value: {@link #RELEASE_MODE_AUTO_CLOSE})
     *
     * @param releaseMode {@link #RELEASE_MODE_NONE}
     *                    / {@link #RELEASE_MODE_AUTO_CLOSE}
     *                    / {@link #RELEASE_MODE_AUTO_OPEN}
     *                    / {@link #RELEASE_MODE_AUTO_OPEN_CLOSE}
     *                    / {@link #RELEASE_MODE_HOLE_OPEN}
     * @return this
     */
    public SwipeConsumer setReleaseMode(int releaseMode) {
        this.mReleaseMode = releaseMode;
        return this;
    }


    public SwipeConsumer setEdgeSize(int edgeSize) {
        this.mEdgeSize = edgeSize;
        return this;
    }

    public SwipeHelper getSwipeHelper() {
        return mSwipeHelper;
    }

    public int getDragState() {
        return mSwipeHelper.getDragState();
    }

    public float getProgress() {
        return mProgress;
    }


    public int getSwipeOpenDistance() {
        return mOpenDistance;
    }

    public int getOpenDistance() {
        return mOpenDistance;
    }


    public SwipeConsumer removeListener(SwipeListener listener) {
        mListeners.remove(listener);
        return this;
    }

    /**
     * add a {@link SwipeListener} as an observer of swipe details
     *
     * @param listener will be called when swipe event happens
     * @return this
     * @see SwipeListener
     * @see SimpleSwipeListener
     */
    public SwipeConsumer addListener(SwipeListener listener) {
        if (listener != null && !mListeners.contains(listener)) {
            this.mListeners.add(listener);
            if (mWrapper != null) {
                listener.onConsumerAttachedToWrapper(mWrapper, this);
            }
        }
        return this;
    }

    public float getOverSwipeFactor() {
        return mOverSwipeFactor;
    }


    public boolean isAutoCloseOnWrapperDetachedFromWindow() {
        return mAutoCloseOnWrapperDetachedFromWindow;
    }

    public SwipeConsumer close() {
        return close(false);
    }

    public SwipeConsumer smoothClose() {
        return close(true);
    }

    public SwipeConsumer close(final boolean smooth) {
        if (mDirection != DIRECTION_NONE && mProgress != 0) {
            onSwipeAccepted(0, true, 0, 0);
            mCachedSwipeDistanceX = 0;
            mCachedSwipeDistanceY = 0;
            boolean isLocked = isDirectionLocked(mDirection);
            if (!isLocked) {
                lockDirection(mDirection);
                addListener(new SimpleSwipeListener() {
                    @Override
                    public void onSwipeClosed(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction) {
                        unlockDirection(direction);
                        removeListener(this);
                    }
                });
            }
            if (smooth) {
                smoothSlideTo(0, 0);
            } else {
                smoothSlideTo(0, 0, 0, 0);
            }
        }
        return this;
    }

    public SwipeConsumer smoothSlideTo(float progress) {
        slideTo(false, progress);
        return this;
    }

    public SwipeConsumer slideTo(boolean smooth, float progress) {
        progress = SmartSwipe.ensureBetween(progress, 0F, 1F);
        int finalX = 0, finalY = 0;
        int distance = (int) (mSwipeOpenDistance * progress);
        switch (mDirection) {
            case DIRECTION_LEFT:
                finalX = distance;
                break;
            default:
                break;
        }
        if (smooth) {
            smoothSlideTo(finalX, finalY);
        } else {
            smoothSlideTo(finalX, finalY, finalX, finalY);
        }
        return this;
    }


    public int getDirection() {
        return mDirection;
    }

    public SwipeConsumer enableDirection(int direction, boolean enable) {
        if (enable) {
            return enableDirection(direction);
        } else {
            return disableDirection(direction);
        }
    }

    public SwipeConsumer enableDirection(int direction) {
        mEnableDirection |= direction;
        return this;
    }

    public SwipeConsumer disableDirection(int direction) {
        if ((mDirection & direction) != 0) {
            close();
        }
        mEnableDirection &= ~direction;
        return this;
    }

    public boolean isDirectionEnable(int direction) {
        return direction != DIRECTION_NONE && (mEnableDirection & direction) == direction;
    }

    public boolean isLeftEnable() {
        return (mEnableDirection & DIRECTION_LEFT) != 0;
    }

    public SwipeConsumer lockDirection(int direction) {
        mLockDirection |= direction;
        return this;
    }

    public SwipeConsumer unlockDirection(int direction) {
        mLockDirection &= ~direction;
        return this;
    }

    public boolean isDirectionLocked(int direction) {
        return direction != DIRECTION_NONE && (mLockDirection & direction) == direction;
    }


    public boolean isLeftLocked() {
        return (mLockDirection & DIRECTION_LEFT) != 0;
    }


    public boolean isNestedScrollEnable(int direction) {
        return (mEnableNested & direction) == direction;
    }


    public boolean isNestedFlyEnable(int direction) {
        return ((mEnableNested >> 4) & direction) == direction;
    }

    public void smoothSlideTo(int startX, int startY, int finalX, int finalY) {
        if (mSwipeHelper != null && mWrapper != null) {
            mSwipeHelper.smoothSlideTo(startX, startY, finalX, finalY);
            ViewCompat.postInvalidateOnAnimation(mWrapper);
        }
    }

    public void smoothSlideTo(int finalX, int finalY) {
        if (mSwipeHelper != null && mWrapper != null) {
            mSwipeHelper.smoothSlideTo(finalX, finalY);
            ViewCompat.postInvalidateOnAnimation(mWrapper);
        }
    }

}
