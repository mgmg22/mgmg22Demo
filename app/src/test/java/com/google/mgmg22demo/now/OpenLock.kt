package com.google.mgmg22demo.now

import org.junit.Test
import java.util.LinkedList
import java.util.Queue

/**
 * @description 752. 打开转盘锁
 * @Author matt.shen
 * @Date 2024/1/30
 * 你有一个带有四个圆形拨轮的转盘锁。每个拨轮都有10个数字： '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' 。每个拨轮可以自由旋转：例如把 '9' 变为 '0'，'0' 变为 '9' 。每次旋转都只能旋转一个拨轮的一位数字。
 *
 * 锁的初始数字为 '0000' ，一个代表四个拨轮的数字的字符串。
 *
 * 列表 deadends 包含了一组死亡数字，一旦拨轮的数字和列表里的任何一个元素相同，这个锁将会被永久锁定，无法再被旋转。
 *
 * 字符串 target 代表可以解锁的数字，你需要给出解锁需要的最小旋转次数，如果无论如何不能解锁，返回 -1 。
 *
 *
 *
 * 示例 1:
 *
 * 输入：deadends = ["0201","0101","0102","1212","2002"], target = "0202"
 * 输出：6
 * 解释：
 * 可能的移动序列为 "0000" -> "1000" -> "1100" -> "1200" -> "1201" -> "1202" -> "0202"。
 * 注意 "0000" -> "0001" -> "0002" -> "0102" -> "0202" 这样的序列是不能解锁的，
 * 因为当拨动到 "0102" 时这个锁就会被锁定。
 */
class OpenLock {

    //    BFS
    fun openLock(deadends: Array<String>, target: String): Int {
        val q: Queue<String> = LinkedList()
        val dead = hashSetOf<String>()
        deadends.forEach { dead.add(it) }
        val visited = hashSetOf<String>()
        q.offer("0000")
        visited.add("0000")
        var step = 0
        while (q.isNotEmpty()) {
            for (i in 0 until q.size) {
                val cur = q.poll()
//                println("cur=$cur")
                if (deadends.contains(cur)) {
                    continue
                }
                if (cur == target) {
                    return step
                }
                for (j in 0 until cur.length) {
                    val up = plusOne(cur, j)
//                    print("up=$up")
                    val down = minusOne(cur, j)
//                    print("down=$down")
                    if (!visited.contains(up)) {
                        q.offer(up)
                        visited.add(up)
                    }
                    if (!visited.contains(down)) {
                        q.offer(down)
                        visited.add(down)
                    }
                }
            }
            step++
        }
        return -1
    }

    //    双向BFS
    fun openLockTwo(deadends: Array<String>, target: String): Int {
        var q1 = hashSetOf<String>()
        var q2 = hashSetOf<String>()
        val dead = hashSetOf<String>()
        deadends.forEach { dead.add(it) }
        val visited = hashSetOf<String>()
        q1.add("0000")
        q2.add(target)
        var step = 0
        while (q1.isNotEmpty() && q2.isNotEmpty()) {
            val temp = hashSetOf<String>()
            for (cur in q1) {
//                println("cur=$cur")
                if (deadends.contains(cur)) {
                    continue
                }
                if (q2.contains(cur)) {
                    return step
                }
                visited.add(cur)
                for (j in 0 until cur.length) {
                    val up = plusOne(cur, j)
//                    print("up=$up")
                    val down = minusOne(cur, j)
//                    print("down=$down")
                    if (!visited.contains(up)) {
                        temp.add(up)
                    }
                    if (!visited.contains(down)) {
                        temp.add(down)
                    }
                }

            }
            step++
            q1 = q2
            q2 = temp
//            println("交换")
        }
        return -1
    }

    fun plusOne(s: String, j: Int): String {
        val ch = s.toCharArray()
        if (ch[j] == '9') {
            ch[j] = '0'
        } else {
            ch[j] = ch[j] + 1
        }
        return String(ch)
    }

    fun minusOne(s: String, j: Int): String {
        val ch = s.toCharArray()
        if (ch[j] == '0') {
            ch[j] = '9'
        } else {
            ch[j] = ch[j] - 1
        }
        return String(ch)
    }

    @Test
    fun test() {
        println(openLock(arrayOf("0201", "0101", "0102", "1212", "2002"), "0202"))
    }
}