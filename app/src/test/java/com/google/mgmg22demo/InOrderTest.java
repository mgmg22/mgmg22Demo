package com.google.mgmg22demo;

import org.junit.Test;

import java.util.LinkedList;

public class InOrderTest {

    @Test
    public void test() {
        TreeNode t1 = new TreeNode(1);
        TreeNode t2 = new TreeNode(2);
        TreeNode t3 = new TreeNode(3);
        TreeNode t4 = new TreeNode(4);
        TreeNode t5 = new TreeNode(5);
        t1.left = t2;
        t1.right = t3;
        t2.left = t4;
        t2.right = t5;
        inOrder(t1);
        System.out.println("二叉树的深度=" + treePath(t1));
    }

    //中序遍历
    public void inOrder(TreeNode root) {
        TreeNode current = root;
        //把LinkedList作为栈使用
        LinkedList<TreeNode> s = new LinkedList<TreeNode>();
        while (current != null || !s.isEmpty()) {
            while (current != null) {
                s.addFirst(current);
                current = current.left;
            }
            if (!s.isEmpty()) {
                current = s.removeFirst();
                System.out.print(current.val + " -> ");
                current = current.right;
            }
        }
    }

    public int treePath(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = treePath(root.left);
        int right = treePath(root.right);
        return left >= right ? (left + 1) : (right + 1);
    }

}
