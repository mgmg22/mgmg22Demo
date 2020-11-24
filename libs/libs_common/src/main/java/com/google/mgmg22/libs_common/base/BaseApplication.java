package com.google.mgmg22.libs_common.base;

import android.app.Application;
import android.content.Context;

/**
 * @Description:
 * @Author: 沈晓顺
 * @CreateDate: 2019-12-04 14:24
 */
public class BaseApplication extends Application {
    private static Application sContext;

    public BaseApplication() {
        sContext = this;
    }

    public static Context getContext() {
        return sContext;
    }

}
