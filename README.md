# mgmg22Demo
包含以下内容：
一些UI效果实现
工具类
ktx 扩展和语法练习

[网易云音乐宇宙尘埃特效](https://juejin.cn/post/6871049441546567688)

[获取进程名函数优化](https://juejin.cn/post/6877127949452050446)



## [全局侧滑返回方案原理](https://qibilly.com/SmartSwipe-tutorial/pages/SmartSwipeBack.html)


### 1.注册生命周期监听 包裹viewGroup

```java
@Override
public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
    ACTIVITIES.add(activity);
    if (mFactory == null) {
        return;
    }
    boolean hasSlideForbid = activity.getClass().isAnnotationPresent(SlideForbidAnnotation.class);
    if ((mFilter != null && !mFilter.onFilter(activity)) || hasSlideForbid) {
        return;
    }
    SmartSwipe.wrap(activity).addConsumer(mFactory.createSwipeBackConsumer(activity));
}
```



```java
public static SmartSwipeWrapper wrap(Activity activity) {
    SmartSwipeWrapper wrapper = peekWrapperFor(activity);
    if (wrapper != null) {
        return wrapper;
    }
    View decorView = activity.getWindow().getDecorView();
    if (decorView instanceof ViewGroup) {
        ViewGroup group = (ViewGroup) decorView;
        int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = group.getChildAt(i);
            if (child.findViewById(android.R.id.content) != null) {
                return wrap(child);
            }
        }
    }
    View contentView = decorView.findViewById(android.R.id.content);
    return wrap(contentView);
}
```

### 2.触摸事件处理

[ViewDragHelper](https://www.jianshu.com/p/23f73088acc4) 它是Google官方推出的手势滑动辅助类，极大程度地简化了我们对控件的手势滑动跟踪及处理。让我们能够更加便捷地开发自定义ViewGroup控件，实现拖拽以及弹性滚动等功能。事实上，官方的SlidingPaneLayout和DrawerLayout都是利用ViewDragHelper实现的。



```java
public static SwipeHelper create(ViewGroup forParent, float sensitivity, SwipeConsumer consumer, Interpolator interpolator) {
    final SwipeHelper helper = create(forParent, consumer, interpolator);
    helper.mTouchSlop = (int) (helper.mTouchSlop * (1 / sensitivity));
    return helper;
}
```



ViewDragHelper接受并处理父控件传递过来的触摸事件，该方法内部会分析MotionEvent 事件，并根据需要，触发监听回调事件。需要强调的是：**父控件的onTouchEvent实现方法需要调用processTouchEvent 方法，才能将事件传递给ViewDragHelper让其分析处理。**



```java
public void processTouchEvent(MotionEvent ev) {
    final int action = ev.getActionMasked();
    final int actionIndex = ev.getActionIndex();

    if (action == MotionEvent.ACTION_DOWN && mDragState != STATE_DRAGGING) {
        // Reset things for a new event stream, just in case we didn't get
        // the whole previous stream.
        cancel();
    }

    if (mVelocityTracker == null) {
        mVelocityTracker = VelocityTracker.obtain();
    }
    mVelocityTracker.addMovement(ev);

    switch (action) {
        case MotionEvent.ACTION_DOWN: {
            final float x = ev.getX();
            final float y = ev.getY();
            final int pointerId = ev.getPointerId(0);

            saveInitialMotion(x, y, pointerId);

            // Since the parent is already directly processing this touch event,
            // there is no reason to delay for a slop before dragging.
            // Start immediately if possible.
            if (mDragState != STATE_DRAGGING) {
                trySwipe(pointerId, mDragState == STATE_SETTLING || mDragState == STATE_NONE_TOUCH, x, y, 0, 0);
            }

            break;
        }

        case MotionEvent.ACTION_POINTER_DOWN: {
            final int pointerId = ev.getPointerId(actionIndex);
            final float x = ev.getX(actionIndex);
            final float y = ev.getY(actionIndex);
            saveInitialMotion(x, y, pointerId);
            if (mDragState == STATE_DRAGGING) {
                trySwipe(pointerId, true, x, y, 0, 0);
            }
            break;
        }

        case MotionEvent.ACTION_MOVE: {
            if (mDragState == STATE_DRAGGING) {
                // If pointer is invalid then skip the ACTION_MOVE.
                if (!isValidPointerForActionMove(mActivePointerId)) {
                    break;
                }

                final int index = ev.findPointerIndex(mActivePointerId);
                if (index < 0) {
                    break;
                }
                final float x = ev.getX(index);
                final float y = ev.getY(index);
                final int idx = (int) (x - mLastMotionX[mActivePointerId]);
                final int idy = (int) (y - mLastMotionY[mActivePointerId]);

                dragTo(mClampedDistanceX + idx, mClampedDistanceY + idy, idx, idy);

                saveLastMotion(ev);
            } else {
                // Check to see if any pointer is now over a draggable view.
                final int pointerCount = ev.getPointerCount();
                for (int i = 0; i < pointerCount; i++) {
                    final int pointerId = ev.getPointerId(i);

                    // If pointer is invalid then skip the ACTION_MOVE.
                    if (!isValidPointerForActionMove(pointerId)) {
                        continue;
                    }

                    final float x = ev.getX(i);
                    final float y = ev.getY(i);
                    float downX = mInitialMotionX[pointerId];
                    float downY = mInitialMotionY[pointerId];
                    final float dx = x - downX;
                    final float dy = y - downY;

                    if (checkTouchSlop(dx, dy) && trySwipe(pointerId, false, downX, downY, dx, dy)) {
                        break;
                    }
                }
                saveLastMotion(ev);
            }
            break;
        }

        case MotionEvent.ACTION_POINTER_UP: {
            final int pointerId = ev.getPointerId(actionIndex);
            if (mDragState == STATE_DRAGGING && pointerId == mActivePointerId) {
                // Try to find another pointer that's still holding on to the captured view.
                int newActivePointer = INVALID_POINTER;
                final int pointerCount = ev.getPointerCount();
                for (int i = 0; i < pointerCount; i++) {
                    final int id = ev.getPointerId(i);
                    if (id == mActivePointerId) {
                        // This one's going away, skip.
                        continue;
                    }
                    if (!isValidPointerForActionMove(id)) {
                        continue;
                    }

                    if (trySwipe(id, true, mInitialMotionX[id], mInitialMotionX[id], 0, 0)) {
                        newActivePointer = mActivePointerId;
                        break;
                    }
                }

                if (newActivePointer == INVALID_POINTER) {
                    // We didn't find another pointer still touching the view, release it.
                    releaseViewForPointerUp();
                }
            }
            clearMotionHistory(pointerId);
            break;
        }

        case MotionEvent.ACTION_UP: {
            if (mDragState == STATE_DRAGGING) {
                releaseViewForPointerUp();
            }
            cancel();
            break;
        }

        case MotionEvent.ACTION_CANCEL: {
            if (mDragState == STATE_DRAGGING) {
                dispatchViewReleased(0, 0);
            }
            cancel();
            break;
        }
        default:
    }
}
```



### 3.设置透明布局，计算滑动距离



```java
//onAttachToWrapper时设置背景为透明
public static void convertWindowToTranslucent(Activity activity) {
        if (activity != null) {
            View contentView = activity.findViewById(android.R.id.content);
            Drawable background = contentView.getBackground();
            if (background == null) {
                TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowBackground});
                int windowBg = a.getResourceId(0, 0);
                a.recycle();
                if (windowBg != 0) {
                    contentView.setBackgroundResource(windowBg);
                }
            }
            Window window = activity.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.getDecorView().setBackgroundDrawable(null);
            SmartSwipeWrapper wrapper = SmartSwipe.peekWrapperFor(activity);
            if (wrapper != null) {
                wrapper.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    } 

```



```java
//计算侧滑距离
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
                case DIRECTION_RIGHT:
                //计算mProgress在ACTION_UP时判断是否
                    mProgress = Math.abs((float) mCurSwipeDistanceX / mSwipeOpenDistance);
                    break;
                case DIRECTION_TOP:
                case DIRECTION_BOTTOM:
                    mProgress = Math.abs((float) mCurSwipeDistanceY / mSwipeOpenDistance);
                    break;
                default:
            }
        }
        if ((mDirection & DIRECTION_HORIZONTAL) > 0) {
            int realDistanceX = clampedDistanceX;
            if (mSwipeDistanceCalculator != null) {
                realDistanceX = mSwipeDistanceCalculator.calculateSwipeDistance(clampedDistanceX, mProgress);
            }
            dx = realDistanceX - mCurDisplayDistanceX;
            dy = 0;
            mCurDisplayDistanceX = realDistanceX;
        } else if ((mDirection & DIRECTION_VERTICAL) > 0) {
            int realDistanceY = clampedDistanceY;
            if (mSwipeDistanceCalculator != null) {
                realDistanceY = mSwipeDistanceCalculator.calculateSwipeDistance(clampedDistanceY, mProgress);
            }
            dx = 0;
            dy = realDistanceY - mCurDisplayDistanceY;
            mCurDisplayDistanceY = realDistanceY;
        }
        onDisplayDistanceChanged(mCurDisplayDistanceX, mCurDisplayDistanceY, dx, dy);
    }
    if (this.mProgress != lastProgress) {
        boolean settling = getDragState() == SwipeHelper.STATE_SETTLING;
        notifySwipeProgress(settling);
    }
}
```



```java
//获取上一个activity的根布局，设置位移
public void onSwipeAccepted(int activePointerId, boolean settling, float initialMotionX, float initialMotionY) {
    if (!mActivityTranslucentUtil.isTranslucent()) {
        mActivityTranslucentUtil.convertActivityToTranslucent();
    }
    if (mRelativeMoveFactor > 0) {
        mHorizontalSwiping = (mDirection & DIRECTION_HORIZONTAL) > 0;
        Activity previousActivity = SmartSwipeBack.findPreviousActivity(mActivity);
        if (previousActivity != null) {
            mPreviousActivityContentView = previousActivity.getWindow().getDecorView();
            switch (mDirection) {
                case DIRECTION_LEFT:    initTranslation = -(int) (mWidth * mRelativeMoveFactor); break;
                case DIRECTION_RIGHT:   initTranslation = (int) (mWidth * mRelativeMoveFactor); break;
                case DIRECTION_TOP:     initTranslation = -(int) (mHeight * mRelativeMoveFactor); break;
                case DIRECTION_BOTTOM:  initTranslation = (int) (mHeight * mRelativeMoveFactor); break;
                default:
            }
            movePreviousActivityContentView(initTranslation);
        }
    }
    super.onSwipeAccepted(activePointerId, settling, initialMotionX, initialMotionY);
}
```



### 4触发侧滑返回



```java
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
```

根据mProgress参数判断是open还是close

```java
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
```




### 5状态栏适配



```java
public static void setFullWindow(Window window) {
    if (window == null) {
        return;
    }
    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    window.setStatusBarColor(Color.TRANSPARENT);
    window.setFormat(PixelFormat.TRANSLUCENT);
    try {
        window.setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```



### 6其他注意事项



##屏蔽侧滑的方式：

```java
activitySlidingBack(this, new SmartSwipeBack.ActivitySwipeBackFilter() {
                    @Override
                    public boolean onFilter(Activity activity) {
                        //根据传入的activity，返回true代表需要侧滑返回；false表示不需要侧滑返回
                        return !(activity instanceof MainActivity)
                                ......//此处新增配置
                          				
                    }
                });
            }
```




##滑动冲突

带有水平方向上的滑动距离监听的页面，如轮播图等
与webview的交互



禁止使用：

```
requestWindowFeature(Window.FEATURE_NO_TITLE)
```

**侧滑过程中会触发上个Activity的生命周期变化 onStart() onStop()**

