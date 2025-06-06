package com.google.mgmg22demo.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.mgmg22.extensions.startKtxActivity
import com.google.mgmg22demo.databinding.ActivityMainBinding
import com.google.mgmg22demo.lock.SyncUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("sxs","onCreate()")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        在这里调用的话，apk就无限重启。
//        android.os.Process.killProcess(android.os.Process.myPid())
        initView()
    }

    override fun onResume() {
        Log.e("sxs","onResume()")
        super.onResume()
    }

    override fun onStart() {
        Log.e("sxs","onStart()")
        super.onStart()
    }

    override fun onRestart() {
        Log.e("sxs","onRestart()")
        super.onRestart()
    }

    override fun onPause() {
        Log.e("sxs","onPause()")
        super.onPause()
    }

    override fun onStop() {
        Log.e("sxs","onStop()")
        super.onStop()
    }

    override fun onDestroy() {
        Log.e("sxs","onDestroy()")
        super.onDestroy()
    }

    private fun initView() {
        binding.btnStartWy.startKtxActivity<WyDemoActivity> { }
        val syncTest = SyncUnit()
        val thread1 = Thread(syncTest, "add")
        val thread2 = Thread(syncTest, "dec")
        thread1.start()
        thread2.start()
    }
}

