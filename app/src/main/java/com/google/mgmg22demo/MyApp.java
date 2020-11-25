package com.google.mgmg22demo;

import android.app.Activity;
import android.app.Application;
import android.graphics.Color;

import com.google.mgmg22.lib_slide_back.SmartSwipe;
import com.google.mgmg22.lib_slide_back.SmartSwipeBack;
import com.google.mgmg22.lib_slide_back.SmartSwipeWrapper;
import com.google.mgmg22.lib_slide_back.SwipeConsumer;
import com.google.mgmg22.lib_slide_back.consumer.ActivitySlidingBackConsumer;
import com.google.mgmg22.lib_slide_back.listener.SimpleSwipeListener;
import com.google.mgmg22.libs_common.base.BaseApplication;
import com.google.mgmg22demo.activity.MainActivity;

import static com.google.mgmg22.lib_slide_back.SmartSwipeBack.activityBack;
import static com.google.mgmg22.lib_slide_back.SwipeConsumer.DIRECTION_LEFT;

/**
 * @Author mgmg22
 * @Date 2020/11/25
 */
public class MyApp extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        activitySlidingBack(this, new SmartSwipeBack.ActivitySwipeBackFilter() {
            @Override
            public boolean onFilter(Activity activity) {
                //根据传入的activity，返回true代表需要侧滑返回；false表示不需要侧滑返回
                //启动流程页面
                return !(activity instanceof MainActivity);
            }
        });
    }

    public static void activitySlidingBack(Application application, SmartSwipeBack.ActivitySwipeBackFilter filter) {
        final float factor = 0.5f;
        final int edgeSizeSmall = SmartSwipe.dp2px(20, application);
        final int edgeSize = 0; // 强制设置有效边缘尺寸为全屏！！！
        final int shadowColor = 0x80000000;
        final int shadowSize = SmartSwipe.dp2px(10, application);
        final int direction = DIRECTION_LEFT;

        activityBack(application, new SmartSwipeBack.SwipeBackConsumerFactory() {
            @Override
            public SwipeConsumer createSwipeBackConsumer(final Activity activity) {
                return new ActivitySlidingBackConsumer(activity)
                        .setRelativeMoveFactor(factor)
                        .setScrimColor(Color.TRANSPARENT)
                        .setShadowColor(shadowColor)
                        .setShadowSize(shadowSize)
                        .setEdgeSize(edgeSize)
                        .enableDirection(direction)
                        .addListener(new SimpleSwipeListener() {
                            @Override
                            public void onSwipeOpened(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction) {
                                if (activity != null) {
                                    activity.finish();
                                    activity.overridePendingTransition(R.anim.anim_none, R.anim.anim_none);
                                }
                            }
                        });
            }
        }, filter);
    }
}
