package com.google.mgmg22.libs_common.component;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import com.google.mgmg22.libs_common.base.BaseApplication;
import com.google.mgmg22.libs_common.helper.PackageUtils;

import static com.google.mgmg22.libs_common.ext.CommonExtensKt.getToken;


/**
 * 工具类
 *
 * @author zhangll
 */
public class PreferenceUtils {
    private static final String PREFS_CART_NUMBER = "mzdkCartNumber";
    private static final String SHARED_PREFERENCES_NAME = "sharedMzdk";

    public static Boolean getBoolean(String key, boolean defaultValue) {
        return getSharedPreferences().getBoolean(key, defaultValue);
    }

    public static void saveBoolean(String key, boolean value) {
        Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String getString(String key, String defaultValue) {
        return getSharedPreferences().getString(key, defaultValue);
    }

    public static void saveString(String key, String value) {
        Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void remove(String key) {
        Editor editor = getSharedPreferences().edit();
        editor.remove(key);
        editor.commit();
    }

    public static String getStringVersion(String key, String defaultValue) {
        return getSharedPreferences().getString(key + PackageUtils.getVersion(), defaultValue);
    }

    public static void saveStringVersion(String key, String value) {
        Editor editor = getSharedPreferences().edit();
        editor.putString(key + PackageUtils.getVersion(), value);
        editor.commit();
    }

    public static void removeStringVersion(String key) {
        Editor editor = getSharedPreferences().edit();
        editor.remove(key + PackageUtils.getVersion());
        editor.commit();
    }

    public static int getInt(String key, int defaultValue) {
        return getSharedPreferences().getInt(key, defaultValue);
    }

    public static void saveInt(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).commit();
    }

    private static SharedPreferences getSharedPreferences() {
        SharedPreferences sharedPreferences = BaseApplication.getContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Application.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static int getCartNumber() {
        //TODO 待优化
        String token = getToken();
        if (token == null || token.length() == 0) {
            return 0;
        }
        return getSharedPreferences().getInt(PREFS_CART_NUMBER, 0);
    }

    public static void saveCartNumber(int cartNumber) {
        SharedPreferences spf = getSharedPreferences();
        spf.edit().putInt(PREFS_CART_NUMBER, cartNumber).commit();
    }

    //  以下的代码是处理 到货提醒 按钮的。代码设计的比较辍比，不想用数据库了。直接sharedPreference了。
    public static void saveArriveAllStr(String skuId) {
        String goodsArrvieAllStr = getString("goodsArrvieAllStr", "");
        String newArriveAllStr = goodsArrvieAllStr + "###" + skuId;
        PreferenceUtils.saveString("goodsArrvieAllStr", newArriveAllStr);
    }

    public static boolean isArriveSaved(String skuId) {
        String goodsArrvieAllStr = getString("goodsArrvieAllStr", "");
        return goodsArrvieAllStr.contains(skuId);
    }

    public static void clearArriveRemindData() {
        saveString("goodsArrvieAllStr", "");
    }

    public static void clearArriveMindItem(String skuId) {
        String goodsArrvieAllStr = getString("goodsArrvieAllStr", "");
        String newArriveAllStr = goodsArrvieAllStr.replaceAll(skuId, "");
        PreferenceUtils.saveString("goodsArrvieAllStr", newArriveAllStr);
    }

}
