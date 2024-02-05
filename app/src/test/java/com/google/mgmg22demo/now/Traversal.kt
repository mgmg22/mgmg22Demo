package com.google.mgmg22demo.now

import java.util.LinkedList

/**
 * @description 先序 根左右；中序 左根右；后序 左右根
 * @Author matt.shen
 * @Date 2024/2/5
 */
class Traversal {
    //中序遍历
    fun inorderTraversal(root: TreeNode?): IntArray {
        // write code here
        val res = mutableListOf<Int>()
        if (root == null) return res.toIntArray()
        var cur = root
        val stack = LinkedList<TreeNode?>()
        stack.add(root)
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {
                stack.add(cur)
                cur = cur.left
            }
            cur = stack.pollLast()
            res.add(cur!!.`val`)
            cur = cur.right
        }
        return res.toIntArray()
    }

    //先序遍历
    fun preorderTraversal(root: TreeNode?): IntArray {
        // write code here
        val res = mutableListOf<Int>()
        if (root == null) return res.toIntArray()
        val stack = LinkedList<TreeNode>()
        var cur = root
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {
                stack.add(cur)
                res.add(cur.`val`)
                cur = cur.left
            }
            cur = stack.pollLast()
            cur = cur.right
        }
        return res.toIntArray()
    }

    //后序遍历
    fun postorderTraversal(root: TreeNode?): IntArray {
        // write code here
        val res = mutableListOf<Int>()
        if (root == null) return res.toIntArray()
        val stack = LinkedList<TreeNode>()
        var cur = root
        var pre: TreeNode? = null
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {
                stack.add(cur)
                cur = cur.left
            }
            cur = stack.peekLast()
            if (cur.right != null && pre != cur.right) { // 右子树存在，且没有被遍历
                cur = cur.right
            } else {//右子树已经被遍历
                cur = stack.pollLast()
                res.add(cur!!.`val`)
                pre = cur// 前一个遍历的节点
                cur = null// 根已经遍历，置空避免判断内层的while
            }

        }
        return res.toIntArray()
    }


}