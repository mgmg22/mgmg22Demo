package com.google.mgmg22demo.now

import org.junit.Test

/**
 * @description example
 * @Author matt.shen
 * @Date 2024/1/24
 */
class Reverse {
    private fun reverseListNode(head: ListNode?): ListNode? {
        var pre: ListNode? = null
        var cur = head
        while (cur != null) {
            val nxt: ListNode? = cur.next
            cur.next = pre
            pre = cur
            cur = nxt
        }
        return pre
    }

    @Test
    fun testReverseListNode() {
        val a = ListNode("1")
        val b = ListNode("2")
        val c = ListNode("3")
        a.next = b
        b.next = c
        var temp: ListNode? = reverseListNode(a)
        while (temp != null) {
            println(temp.value)
            temp = temp.next
        }

    }
}