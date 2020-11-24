package com.google.mgmg22.libs_common.helper;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.mgmg22.libs_common.base.BaseApplication;


/**
 * @Description:
 * @Author: 沈晓顺
 * @CreateDate: 2019-12-04 16:58
 */
public class PackageUtils {
    public static String getVersion() {
        String version = "";
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = BaseApplication.getContext().getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(BaseApplication.getContext().getPackageName(), 0);
            version = packInfo.versionName;
        } catch (Exception e) {
        }
        return version;
    }
}
