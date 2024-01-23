package com.google.mgmg22demo.history;

/**
 * @Author shenxiaoshun
 * @Date 2021/2/22
 */

/**
 * 节点类
 * 包含四个元素：key、value、pre节点、next节点
 */
class Node{
    String key;
    String value;
    Node pre;
    Node next;

    Node(String key, String value){
        this.key = key;
        this.value = value;
    }
}