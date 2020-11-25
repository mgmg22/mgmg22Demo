package com.google.mgmg22demo.activity

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.mgmg22demo.R
import com.google.mgmg22.lib_util.ProcessUtil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testGetCurrentProcessNameByApplication()
        testGetCurrentProcessNameByActivityThread()
//        在这里调用的话，apk就无限重启。
//        android.os.Process.killProcess(android.os.Process.myPid())
        testGetCurrentProcessNameByActivityManager()
    }

    private fun testGetCurrentProcessNameByApplication() {
        val beginTime = SystemClock.elapsedRealtimeNanos()
        ProcessUtil.getCurrentProcessNameByApplication()
        Log.i(
                "sxshttp",
                "getCurrentProcessNameByApplication duration=${SystemClock.elapsedRealtimeNanos() - beginTime}"
        )
    }

    private fun testGetCurrentProcessNameByActivityThread() {
        val beginTime = SystemClock.elapsedRealtimeNanos()
        ProcessUtil.getCurrentProcessNameByActivityThread()
        Log.i(
                "sxshttp",
                "getCurrentProcessNameByActivityThread duration=${SystemClock.elapsedRealtimeNanos() - beginTime}"
        )
    }

    private fun testGetCurrentProcessNameByActivityManager() {
        val beginTime = SystemClock.elapsedRealtimeNanos()
        ProcessUtil.getCurrentProcessNameByActivityManager(
                this
        )
        Log.i(
                "sxshttp",
                "getCurrentProcessNameByActivityManager duration=${SystemClock.elapsedRealtimeNanos() - beginTime}"
        )
    }

}

