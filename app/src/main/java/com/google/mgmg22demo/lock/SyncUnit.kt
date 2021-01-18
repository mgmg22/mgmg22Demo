package com.google.mgmg22demo.lock

import android.util.Log

/**
 *1 无论是修饰方法还是修饰代码块都是 对象锁,当一个线程访问一个带synchronized方法时，由于对象锁的存在，
 * 所有加synchronized的方法都不能被访问（前提是在多个线程调用的是同一个对象实例中的方法）
2 无论是修饰静态方法还是锁定某个对象,都是 类锁.一个class其中的静态方法和静态变量在内存中只会加载和初始化一份，
所以，一旦一个静态的方法被申明为synchronized，此类的所有的实例化对象在调用该方法时，共用同一把锁，称之为类锁。
 */
class SyncUnit : Runnable {
    var count = 0

    override fun run() {
        val name = Thread.currentThread().name
        if ("add" == name) {
            addCount()
        } else {
            decCount()
        }
    }

    private fun addCount() {
        synchronized(this) {
            for (i in 0..4) {
                count += 1
                Log.e(
                    "sxs",
                    Thread.currentThread().name + System.currentTimeMillis() + "[$i]${count}"
                )
                Thread.sleep(10)
            }
        }
    }

    private fun decCount() {
        //对象锁，同一个对象实例中，synchronized修饰的所有方法不能并行执行
        synchronized(this) {
            for (i in 0..4) {
                count -= 1
                Log.e(
                    "sxs",
                    Thread.currentThread().name + System.currentTimeMillis() + "[$i]${count}"
                )
                Thread.sleep(20)
            }
        }
    }
}
