package com.google.mgmg22demo.now

import org.junit.Test

class FibTestKt {
    fun fibKt(n: Int): Int {
        if (n == 0) return 0
        if (n == 1 || n == 2) return 1

        var pre = 1
        var cur = 1
        for (i in 3..n) {
            val sum = pre + cur
            pre = cur
            cur = sum
        }
        return cur
    }

    @Test
    fun testFibKt() {
        val sum = fibKt(10)
        println(sum)
    }
}
