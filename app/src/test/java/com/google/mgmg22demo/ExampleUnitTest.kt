package com.google.mgmg22demo

import com.google.mgmg22.extensions.sameAs
import io.reactivex.Observable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    /**
     *扩展函数
     */
    private fun String.lastChar(): Char = this[this.length - 1]

    /**
     * 扩展属性 lastChar获取String的最后一个字符
     */
    private val String.lastChar: Char
        get() = get(length - 1)

    /**
     *测试扩展
     */
    @Test
    fun testFunExtension() {
        val str = "test extension fun"
        println(str.lastChar())
        val s = "abc"
        println(s.lastChar)
    }

    /**
     * json解析
     */
    @Test
    fun testJsonFormat() {
//        val json = "{\"merchantId\":728,\"userName\":\"testUserName01\",\"accountType\":0,\"createTime\":\"01-09 10:00:00\",\"merchantStatus\":1,\"identityStatus\":1,\"bindCardStatus\":3,\"storeId\":47,\"storeStatus\":3,\"submitStatus\":0}"
//        var result: MerchantInfo = Gson().fromJson(json)
//        println("LogTagggg:" + result.merchantId.toString() + "," + result.createTime)
//        println(result.toString())
//        val (accountType, bindCardStatus) = result
//        println("LogTagggg:$accountType,$bindCardStatus")
    }

    /**
     * 名称遮蔽、作用域
     */
    @Test
    fun testInc() {
        inc(1)
    }

    fun inc(num: Int) {
        val num = 2
//        var max=10
        if (num > 0) {
            val num = 3
//            println("max: " + max)
//            max++
//            println("max: " + max)
        }
        println("num: " + num)
//        println("max: " + max)
    }

    /**
     * 列表
     */
    @Test
    fun listDEmo() {
        val list = listOf("aaa", "bbb", "ccc", "ddd", "eee", "fff", "ggg")
        list.forEach {
            println(it)
        }

        for (i in 2..5) println(list[i])
        //显示声明
//        list.forEach { item ->
//            println(item)
//        }
    }

    /**
     * kotlin if
     */
    fun max2(a: Int, b: Int) = if (a > b) a else b

    /**
     * kotlin when
     */
    @Test
    fun testWhen() {
        var result = 99
        //        when (result) {
//            100 -> println("sss")
//            99 -> println("ss")
//            in 95..99 -> println("s")
//            in 90..94 -> println("a")
//            in 80..89 -> println("A")
//            else -> println("failed")
//        }

        fun doSomeThing(x: Int) = -1 * x //必须要放到使用者上边才能识别
        when (result) {
            doSomeThing(result) -> println("$result")
            else -> println("else:$result") //输出结果：else ：99
        }
    }

    /**
     * 展开数组
     */
    @Test
    fun testChangeArray() {
        changeArray(arrayOf("111", "222", "333", "444", "555"))
    }

    fun changeArray(args: Array<String>) {
        val list = listOf("000", *args) //展开运算符展开数组内容
        println(list)
        for (i in 0 until list.size) {
            println(list[i])
        }
    }

    /**
     * let
     */
    @Test
    fun testLet() {
        val temp = "ceshi"
//        val temp: String? = null
        temp?.let { temp -> println("aa$temp") }
    }

    /**
     * null-safe
     */
    @Test
    fun testNullSafe() {
        val temp: Int? = null
        println(temp?.toString() ?: "is null")
    }

    /**
     * 伴生
     */
    class A {
        private val age: Int = 10

        companion object {
            fun test() {
                println("Companion Object...")
                println(A().age)
            }
        }
    }

    @Test
    fun testCompanion() {
        A.test()
        //默认名称
        A.Companion.test()
    }


    /**
     * 中缀调用
     */
    @Test
    fun testTo() {
        val map = mapOf("ss" to "A", 2 to "B", 3 to "C")//to实际上一个返回Pair对象的函数，不是属于map结构内部的运算符，但是to在语法层面使用很像中缀运算符调用
        map.forEach { key, value ->
            println("key: $key   value:$value")
        }
    }

    /**
     * 字符串比较
     */
    @Test
    fun testCompare() {
        val strA = "A"
        val strB = "B"
        if (strA sameAs strB) {//中缀调用 sameAs
            println("str is the same")
        } else {
            println("str is the different")
        }
    }

    /**
     * shuffled
     */
    @Test
    fun testRandom() {
        val adjectives: List<String> = arrayListOf(
            "Acidic",
            "Bitter",
            "Cool"
        )
        val startTime = System.currentTimeMillis()
        for (i in 0..10) {
            println(adjectives.shuffled()[0])
        }
        println("执行时间：${System.currentTimeMillis() - startTime}ms")
    }

    /**
     * random
     */
    @Test
    fun testRandom2() {
        val startTime = System.currentTimeMillis()
        for (i in 0..10) {
            println((1..10000).random())
        }
        println("执行时间：${System.currentTimeMillis() - startTime}ms")
    }


    private fun mockInterval() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
            .subscribe {
                println("执行时间：${System.currentTimeMillis()}ms")
            }
    }

    /**
     * test interval
     */
    @Test
    fun testInterval() {
        mockInterval()
        mockInterval()
    }

    @Test
    fun main() = runBlocking {
        val jobs = List(100_000) {
            // launch a lot of coroutines and list their jobs
            launch {
                delay(1000L)
                print(".$it")
            }
        }
        jobs.forEach { it.join() } // wait for all jobs to complete
    }
}
