package com.google.mgmg22demo;

import org.junit.Test;

/**
 * leetcode 2. 两数相加
 * 逆序
 */
public class AddTwoNumbersTest {

    class ListIntNode {
        int value;
        ListIntNode next;

        ListIntNode(int value) {
            this.value = value;
        }
    }

    @Test
    public void addTwoNumbersTest() {
        ListIntNode a1 = new ListIntNode(2);
        ListIntNode a2 = new ListIntNode(4);
        ListIntNode a3 = new ListIntNode(3);

        ListIntNode b1 = new ListIntNode(5);
        ListIntNode b2 = new ListIntNode(6);
        ListIntNode b3 = new ListIntNode(4);
        a1.next = a2;
        a2.next = a3;

        b1.next = b2;
        b2.next = b3;
        ListIntNode addTwo = reverse(addTwoNumbers(a1, b1));
        while (addTwo != null) {
            System.out.println(addTwo.value);
            addTwo = addTwo.next;
        }

    }

    public ListIntNode addTwoNumbers(ListIntNode l1, ListIntNode l2) {
        ListIntNode result = new ListIntNode(0);
        ListIntNode p = l1, q = l2, cur = result;
        int curry = 0;
        while (p != null || q != null) {
            int x = (p != null) ? p.value : 0;
            int y = (q != null) ? q.value : 0;
            int sum = x + y + curry;
            curry = sum / 10;

            cur.next = new ListIntNode(sum % 10);
            cur = cur.next;

            if (p != null) p = p.next;
            if (q != null) q = q.next;
        }
        if (curry > 0) {
            cur.next = new ListIntNode(curry);
        }
        return result.next;
    }

    public ListIntNode reverse(ListIntNode root) {
        ListIntNode cur = root, pre = null;
        while (cur != null) {
            ListIntNode nxt = cur.next;
            cur.next = pre;
            pre = cur;
            cur = nxt;
        }
        return pre;
    }
}
