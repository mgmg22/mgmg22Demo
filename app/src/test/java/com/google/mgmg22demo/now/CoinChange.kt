package com.google.mgmg22demo.now

import org.junit.Test
import kotlin.math.min

/**
 * @description 322. 零钱兑换
 * @Author matt.shen
 * @Date 2024/1/25
 * 给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
 *
 * 计算并返回可以凑成总金额所需的 最少的硬币个数 。如果没有任何一种硬币组合能组成总金额，返回 -1 。
 *
 * 你可以认为每种硬币的数量是无限的。
 */
class CoinChange {
    //    从底向上
    fun coinChange(coins: IntArray, amount: Int): Int {
        val dp = Array(amount + 1) { amount + 1 }
        dp[0] = 0
        for (i in dp.indices) {
            for (coin in coins) {
                if (i - coin < 0) continue
                dp[i] = min(dp[i], 1 + dp[i - coin])
            }
        }
        return if (dp[amount] == amount + 1)
            -1
        else
            dp[amount]
    }

    //    从顶向下，暴力递归
//    fun coinChange(coins: IntArray, amount: Int): Int {
//        if (amount == 0) return 0
//        if (amount < 0) return -1
//        var res = Int.MAX_VALUE
//        for (coin in coins) {
//            val subProblem = coinChange(coins, amount - coin)
//            if (subProblem == -1) continue
//            res = min(res, subProblem + 1)
//        }
//        return if (res != Int.MAX_VALUE) {
//            res
//        } else
//            -1
//    }

    //    从顶向下，优化
//    fun coinChange(coins: IntArray, amount: Int): Int {
//        if (amount == 0) return 0
//        if (amount < 0) return -1
//        var res = Int.MAX_VALUE
//        val memo = Array(amount + 1) { 0 }
//        if (memo[amount] != 0)
//            return memo[amount]
//        for (coin in coins) {
//            val subProblem = coinChange(coins, amount - coin)
//            if (subProblem == -1) continue
//            res = min(res, subProblem + 1)
//        }
//        memo[amount] = if (res != Int.MAX_VALUE) {
//            res
//        } else
//            -1
//        return memo[amount]
//    }

    @Test
    fun testCoinChange() {
        println(coinChange(arrayOf(1, 2, 5).toIntArray(), 100))
    }
}