package com.google.mgmg22demo.now

import org.junit.Test

class FibTestKt {
    fun fibKt(n: Int): Int {
        if (n == 0) return 0
        if (n == 1 || n == 2) return 1

        val dp = Array(n + 1) { 0 }
        dp[1] = 1
        dp[2] = 1
        for (i in 3..n) {
            dp[i] = dp[i - 2] + dp[i - 1]
        }
        return dp[n]
    }

    @Test
    fun testFibKt() {
        val sum = fibKt(10)
        println(sum)
    }
}
