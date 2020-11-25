package com.google.mgmg22.libs_common.base;

import android.app.Application;
import android.content.Context;

/**
 * @Description:
 * @Author: mgmg22
 * @CreateDate: 2019-12-04 14:24
 */
public abstract class BaseApplication extends Application {
    //TODO 去掉，目前只是为了api不报错
    private static Application sContext;

    public BaseApplication() {
        sContext = this;
    }

    public static Context getContext() {
        return sContext;
    }
}
