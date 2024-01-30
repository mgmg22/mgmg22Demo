package com.google.mgmg22demo.now

import java.util.LinkedList
import java.util.Queue

/**
 * @description 111. 二叉树的最小深度
 * @Author matt.shen
 * @Date 2024/1/30
 * 给定一个二叉树，找出其最小深度。
 *
 * 最小深度是从根节点到最近叶子节点的最短路径上的节点数量。
 *
 * 说明：叶子节点是指没有子节点的节点。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：root = [3,9,20,null,null,15,7]
 * 输出：2
 */
class MinDepth {
    fun minDepth(root: TreeNode?): Int {
        if (root == null) return 0
        val q: Queue<TreeNode> = LinkedList()
        q.offer(root)
        var depth = 1
        while (q.isNotEmpty()) {
            for (item in q) {
                val cur = q.poll()
                if (cur.left == null && cur.right == null) {
                    return depth
                }
                cur.left?.let {
                    q.offer(it)
                }
                cur.right?.let {
                    q.offer(it)
                }
            }
            depth++
        }
        return depth
    }
}