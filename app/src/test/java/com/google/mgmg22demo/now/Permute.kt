package com.google.mgmg22demo.now

import com.google.gson.Gson
import org.junit.Test
import java.util.LinkedList

/**
 * @description 46. 全排列
 * @Author matt.shen
 * @Date 2024/1/27
 * 给定一个不含重复数字的数组 nums ，返回其 所有可能的全排列 。你可以 按任意顺序 返回答案。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [1,2,3]
 * 输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 */
class Permute {
    var res = ArrayList<List<Int>>()
    fun permute(nums: IntArray): List<List<Int>> {
        val trace = LinkedList<Int>()
        backTrack(nums, trace)
        return res
    }

    fun backTrack(nums: IntArray, track: LinkedList<Int>) {
        if (nums.size == track.size) {
            res.add(ArrayList(track))
            return
        }
        for (num in nums) {
            if (track.contains(num))
                continue
            track.add(num)
            backTrack(nums, track)
            track.removeLast()
        }
    }

    @Test
    fun test() {
        val result = permute(intArrayOf(1, 2, 3))
        for (item in result) {
            println(Gson().toJson(item))
        }
    }
}