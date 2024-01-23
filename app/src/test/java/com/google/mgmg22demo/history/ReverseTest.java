package com.google.mgmg22demo.history;

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
//        printListNode(reverse(l1));
//        printListNode(reverseNum(l1));
        printListNode(reverseKGroup(l1, 2));
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

    public ListNode reverseNum(ListNode head) {
        ListNode pre, cur;
        pre = null;
        cur = head;
        while (cur != null) {
            ListNode nxt = cur.next;
            // 逐个结点反转
            cur.next = pre;
            // 更新指针位置
            pre = cur;
            cur = nxt;
        }
        return pre;
    }

    /**
     * 反转区间 [a, b) 的元素，注意是左闭右开
     */
    ListNode reverse(ListNode a, ListNode b) {
        ListNode pre, cur;
        pre = null;
        cur = a;
        // while 终止的条件改一下就行了
        while (cur != b) {
            ListNode nxt = cur.next;
            cur.next = pre;
            pre = cur;
            cur = nxt;
        }
        // 返回反转后的头结点
        return pre;
    }

    /**
     * k个一组反转链表
     *
     * @param head
     * @param k
     * @return
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        if (head == null) return head;
        ListNode a, b;
        a = b = head;
        for (int i = 0; i < k; i++) {
            // 不足 k 个，不需要反转，base case
            if (b == null) return head;
            b = b.next;
        }
        // 反转前 k 个元素
        ListNode newHead = reverse(a, b);
        // 递归反转后续链表并连接起来
        a.next = reverseKGroup(b, k);
        return newHead;
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
