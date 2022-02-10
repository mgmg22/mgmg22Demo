package com.google.mgmg22demo.log

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.Settings


/**
 * @author AlanPan
 * @date 2020/8/12
 */
class HsLogManager : Application.ActivityLifecycleCallbacks {

    private var mobileInfoView: MobileInfoView? = null
    private var isStart: Boolean = false
    private lateinit var context: Context
    private var isRequestPermission: Boolean = true
    lateinit var mContext: Context
    private var mIsForeground = false //APP是否位于前台
    private lateinit var handler: Handler

    companion object {
        @JvmStatic
        val instance: HsLogManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            HsLogManager()
        }
    }

    fun init(context: Context): HsLogManager {
        mContext = context
        mobileInfoView = MobileInfoView(context)
        this.context = context
        if (context is Application) {
            context.registerActivityLifecycleCallbacks(this)
        }
        return this
    }

    private fun initHandler() {
        handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                val value = msg.obj
                when (msg.what) {
                    PerformanceDataManager.MSG_CPU -> {
                        mobileInfoView?.addOneRecord("cpu", value.toString(), false)
                    }
                    PerformanceDataManager.MSG_MEMORY -> {
                        mobileInfoView?.addOneRecord("memory", value.toString(), false)
                    }
                    PerformanceDataManager.MSG_FRAME -> {
                        mobileInfoView?.addOneRecord("fps", value.toString(), false)

                    }
                }
            }
        }
    }


    private fun start() {
        if (isStart) {
            return
        }
        mobileInfoView?.addView()

        initHandler()
        PerformanceDataManager.getInstance().initMainThread(handler)
        PerformanceDataManager.getInstance().init(mContext)
        PerformanceDataManager.getInstance().start()
    }


    private fun stop() {
        isStart = false
        mobileInfoView?.removeView()
        PerformanceDataManager.getInstance().destroy()
    }


    override fun onActivityResumed(activty: Activity) {
        if (isForegroundApp() && !mIsForeground) {
            mIsForeground = true
            start()
        }
    }

    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
        if (!isForegroundApp()) {
            mIsForeground = false
            requestPermission(activity)
            stop()
        }
    }

    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
        requestPermission(activity)
    }

    override fun onActivityStarted(p0: Activity) {

    }

    override fun onActivityDestroyed(p0: Activity) {

    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

    }


    @Synchronized
    fun requestPermission(activity: Activity?) {
        if (!isRequestPermission) {
            return
        }
        isRequestPermission = false

        try {
            //判断有没有悬浮窗权限，没有去申请
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(activity)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + context.packageName)
                )
                activity?.startActivityForResult(intent, 1101)
            }
        } catch (e: Exception) {

        }

    }

    /**
     * 判断APP是否为前台应用
     * @return
     */
    private fun isForegroundApp(): Boolean {
        val ac =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val packageName: String = context.packageName
        val runningApps =
            ac.runningAppProcesses
        if (packageName == null || runningApps == null || runningApps.isEmpty()) {
            return false
        }
        for (app in runningApps) {
            if (app.processName == packageName && app.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true
            }
        }
        return false
    }


}