package com.google.mgmg22demo.history

import org.junit.Test
import java.lang.ref.PhantomReference
import java.lang.ref.ReferenceQueue
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ReferenceUnitTest {

    // 弱引用示例
    @Test
    fun gcWeak() {
        val referenceQueue: ReferenceQueue<Any> = ReferenceQueue()
        val aa: WeakReference<ByteArray> =
            WeakReference(ByteArray(1024), referenceQueue)
//        System.gc()
        println("aa弱引用：$aa")
        println("aa对象：" + aa.get())
        println("Queue存放弱引用：" + referenceQueue.poll())
    }

    // 虚引用示例
    @Test
    fun gcPhantom() {
        val referenceQueue: ReferenceQueue<Any> = ReferenceQueue()
        val aa: PhantomReference<ByteArray> =
            PhantomReference(ByteArray(1024), referenceQueue)
        // System.gc()
        println("aa虚引用：$aa")
        println("aa对象：" + aa.get())
        println("Queue存放虚引用：" + referenceQueue.poll())
    }

    // 软引用示例
    @Test
    fun gcSoft() {
        val referenceQueue: ReferenceQueue<Any> = ReferenceQueue()
        val aa: SoftReference<ByteArray> =
            SoftReference(ByteArray(1024000000), referenceQueue)
         System.gc()
//         System.gc()
        println("aa软引用：$aa")
        println("aa对象：" + aa.get())
        println("Queue存放软引用：" + referenceQueue.poll())
    }

    //WeakHashMap
    @Test
    fun gcWeakHashMap() {
        val aaa: WeakHashMap<Any, Any> = WeakHashMap()
        for (i in 0..49) {
            aaa[Any()] = Any()
            System.gc()
            println("大小：" + aaa.size)
        }
    }
}
