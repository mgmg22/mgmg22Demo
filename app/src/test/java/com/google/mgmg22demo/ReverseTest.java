package com.google.mgmg22demo;

import org.junit.Test;

/**
 * 反转单链表
 *
 * @Author shenxiaoshun
 * @Date 2021/3/10
 */
public class ReverseTest {

    @Test
    public void test() {
        ListNode l1 = new ListNode("1");
        ListNode l2 = new ListNode("2");
        ListNode l3 = new ListNode("3");
        ListNode l4 = new ListNode("4");
        ListNode l5 = new ListNode("5");
        l1.next = l2;
        l2.next = l3;
        l3.next = l4;
        l4.next = l5;
        printListNode(l1);
        printListNode(reverse(l1));
    }

    public void printListNode(ListNode head) {
        String result = "打印：";
        ListNode temp = head;
        while (temp != null) {
            result += temp.value + ",";
            temp = temp.next;
        }
        System.out.println(result);
    }

    public ListNode reverse(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode last = reverse(head.next);
        head.next.next = head;
        head.next = null;
        return last;
    }

}
