package com.google.mgmg22demo;

/**
 * @Author shenxiaoshun
 * @Date 2021/2/22
 */

/**
 * 节点类
 * 包含四个元素：key、value、pre节点、next节点
 */
class ListNode {
    String value;
    ListNode next;

    ListNode(String value) {
        this.value = value;
    }
}