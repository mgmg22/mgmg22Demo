package com.google.mgmg22demo.now

import org.junit.Test

/**
 * @description 438. 找到字符串中所有字母异位词
 * @Author matt.shen
 * @Date 2024/2/1
 * 给定两个字符串 s 和 p，找到 s 中所有 p 的 异位词 的子串，返回这些子串的起始索引。不考虑答案输出的顺序。
 *
 * 异位词 指由相同字母重排列形成的字符串（包括相同的字符串）。
 *
 *
 *
 * 示例 1:
 *
 * 输入: s = "cbaebabacd", p = "abc"
 * 输出: [0,6]
 * 解释:
 * 起始索引等于 0 的子串是 "cba", 它是 "abc" 的异位词。
 * 起始索引等于 6 的子串是 "bac", 它是 "abc" 的异位词。
 */
class Anagrams {
    fun findAnagrams(s: String, p: String): List<Int> {
        // https://baijiahao.baidu.com/s?id=1764873820835917050&wfr=spider&for=pc
        val result = mutableListOf<Int>()
        val chars = IntArray(128)
        for (item in p.toCharArray()) chars[item - 'a']++
        var head = 0
        var tail = 0
        val sc = s.toCharArray()
        while (tail < sc.size) {
            if (chars[sc[tail] - 'a'] > 0) {
                chars[sc[tail++] - 'a']--
                if (tail - head == p.length) result.add(head)
            } else {
                chars[sc[head++] - 'a']++
            }
        }
        return result
    }

    @Test
    fun test() {
        println(findAnagrams("baa", "aa"))
    }
}