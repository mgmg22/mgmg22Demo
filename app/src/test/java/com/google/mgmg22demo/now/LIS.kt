package com.google.mgmg22demo.now

import com.google.gson.Gson
import org.junit.Test

/**
 * @description 300. 最长递增子序列
 * @Author matt.shen
 * @Date 2024/2/1
 * 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
 *
 * 子序列 是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。例如，[3,6,2,7] 是数组 [0,3,1,6,2,2,7] 的子序列。
 *
 *
 * 示例 1：
 *
 * 输入：nums = [10,9,2,5,3,7,101,18]
 * 输出：4
 * 解释：最长递增子序列是 [2,3,7,101]，因此长度为 4 。
 */
class LIS {
    fun lengthOfLIS(nums: IntArray): Int {
        val dp = Array(nums.size) { 1 }
        for (i in 0 until nums.size) {
            for (j in 0 until i) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1)
//                    print(i)
//                    println(Gson().toJson(dp))
                }
            }
        }
        var res = 0
        for (item in dp) {
            res = Math.max(res, item)
        }
        return res
    }

    @Test
    fun test() {
        println(lengthOfLIS(arrayOf(10, 9, 2, 5, 3, 7, 101, 18).toIntArray()))
    }
}