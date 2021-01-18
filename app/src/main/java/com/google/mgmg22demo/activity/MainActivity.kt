package com.google.mgmg22demo.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.mgmg22.extensions.startKtxActivity
import com.google.mgmg22demo.R
import com.google.mgmg22demo.lock.SyncUnit
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        在这里调用的话，apk就无限重启。
//        android.os.Process.killProcess(android.os.Process.myPid())
        initView()
    }

    private fun initView() {
        btn_start_wy.startKtxActivity<WyDemoActivity> { }
        val syncTest = SyncUnit()
        val thread1 = Thread(syncTest, "add")
        val thread2 = Thread(syncTest, "dec")
        thread1.start()
        thread2.start()
    }
}

