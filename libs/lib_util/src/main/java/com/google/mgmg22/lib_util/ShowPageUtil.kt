package com.google.mgmg22.lib_util

import android.Manifest
import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager

//在添加 Fragment 时，需要设置 Fragment 的 Tag才可以看到
object ShowPageUtil {

    private const val channel = "显示当前页面名称"
    private const val notificationId = 888

    private lateinit var application: Application

    fun init(application: Application) {
        ShowPageUtil.application = application
        createNotificationChannel()
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
            override fun onActivityResumed(activity: Activity) {
                upDataPageInfo(activity)
            }

            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityDestroyed(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

        })
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun upDataPageInfo(activity: Activity?) {
        val sb = StringBuilder()
        val fragmentList = arrayListOf<Fragment>()
        activity?.apply {
            (activity as? AppCompatActivity)?.let {
                val fragments = it.supportFragmentManager.fragments
                fragments.forEach {
                    processAllFragment(it, fragmentList)
                }
            }
        }

        //处理当前显示的fragment
        fragmentList.forEach { fragment ->
            if (fragment.javaClass.name.contains("wpt")) {
                if (fragment.parentFragment != null && fragmentIsShow(fragment.parentFragment) && fragmentIsShow(
                        fragment
                    )
                ) {
                    handlerShowInfo(fragment, fragmentList, sb)
                } else if (fragment.parentFragment == null && fragmentIsShow(fragment)) {
                    handlerShowInfo(fragment, fragmentList, sb)
                }
            }

        }

        refreshNotification(activity?.javaClass?.simpleName + " 按Home键再打开app可刷新", sb.toString())
    }

    private fun handlerShowInfo(
        fragment: Fragment?,
        fragmentList: java.util.ArrayList<Fragment>,
        sb: StringBuilder
    ) {
        //需要判断是否viewpager+fragment的模式
        (fragment?.view?.parent as? ViewPager)?.apply {
            val viewPagerInFragment = getViewPagerInFragment(id, fragmentList)
            if (viewPagerInFragment != null && fragmentIsShow(viewPagerInFragment)) {
                //viewPager + Fragment的模式 且viewPager属于Activity
                sb.append("-->${getFragmentName(fragment)}  ")
            } else if (viewPagerInFragment == null && fragmentIsShow(fragment)) {
                //viewPager + Fragment的模式 且viewPager属于Fragment
                sb.append("-->${getFragmentName(fragment)}  ")
            }
        } ?: kotlin.run {
            sb.append("-->${getFragmentName(fragment)}  ")
        }
    }

    private fun getViewPagerInFragment(
        id: Int,
        fragmentList: java.util.ArrayList<Fragment>
    ): Fragment? {
        fragmentList.forEach {
            if (it.view?.findViewById<View>(id) != null) {
                //Log.e("tag", "viewPager所属fragment = ${getFragmentName(it)}")
                return it
            }
        }
        return null
    }

    // 遍历所有fragment
    private fun processAllFragment(fragment: Fragment?, fragmentList: ArrayList<Fragment>) {
        if (fragment == null) {
            return
        }

        fragmentList.add(fragment)

        for (childFragment in fragment.childFragmentManager.fragments) {
            processAllFragment(childFragment, fragmentList)
        }
    }

    private fun fragmentIsShow(fragment: Fragment?): Boolean {
        //多个fragmetn 通过控制绑定的viewGroup来控制是否显示的情况，比如UgcMainActivity
        val isVisible = (fragment?.view?.parent as? ViewGroup)?.visibility == View.VISIBLE
        if (isVisible.not()) {
//            Log.e("tag","没有显示的fragment = ${getFragmentName(fragment)}")
        }
        return fragment != null && fragment.isHidden.not() && fragment.userVisibleHint && isVisible
    }


    private fun getFragmentName(fragment: Fragment?) = fragment?.javaClass?.simpleName

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun refreshNotification(title: String?, content: String?) {
        val appInfo = application.applicationInfo
        val notification = NotificationCompat.Builder(application, channel)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSound(null)
            .setSound(null, AudioManager.STREAM_NOTIFICATION)
            .setVibrate(null)
            .setSmallIcon(appInfo.icon)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        NotificationManagerCompat.from(application).notify(notificationId, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channel, channel, NotificationManager.IMPORTANCE_DEFAULT)
            channel.setSound(null, null)
            val notificationManager: NotificationManager =
                application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}