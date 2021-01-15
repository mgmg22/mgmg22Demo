package com.google.mgmg22.lib_slide_back;

/**
 * 页面动态控制是否拦截侧滑返回
 * 使用方式：
 * 1在要拦截侧滑返回的Activity实现该接口
 * 2在Activity的onCreate添加    SmartSwipe.wrap(this).setOnInterceptSwipeBack(this);
 */
public interface OnInterceptSwipeBack {
    //返回值为true时不可侧滑
    boolean isDisallowIntercept();
}
