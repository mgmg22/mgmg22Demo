package com.google.mgmg22demo.history;



import java.util.HashMap;

/**
 * @Author shenxiaoshun
 * @Date 2021/2/22
 */
public class LruTest {

    public class LruImpl {

        private Node head;
        private Node end;

        /**
         *  lru容器中可以容纳最多元素
         */
        private final int limit;

        private final HashMap<String , Node > hashMap;

        /**
         * 初始化最大的容器
         * @param limit  可以容纳最多元素
         */
        public LruImpl(int limit){
            this.limit = limit;
            hashMap = new HashMap <>();
        }

        /**
         * 根据key获取对应的元素
         * @param key
         * @return
         */
        public String get(String key){
            Node node = hashMap.get(key);
            if(node == null){
                return null;
            }
            refreshLru(node);
            return node.value;
        }

        /**
         * 给容器中添加元素。判断该元素是否存在容器中
         * 1. 如果不存在，则添加到尾端.
         *      1. 判断容器容量是否大于最大值，大于则移除对应的元素，再将元素添加到尾端
         * 2. 如果存在，则刷新元素
         * @param key
         * @param value
         */
        public void put(String key, String value){
            Node node = hashMap.get(key);
            if(node == null){
                //如果大于，则去掉头结点元素
                if(hashMap.size() >= limit){
                    String headKey = removeNode(head);
                    hashMap.remove(headKey);
                }
                node = new Node(key, value);
                addNode(node);
                hashMap.put(key, node);
            }else{
                node.value = value;
                refreshLru(node);
            }
        }

        /**
         * 删除某个节点
         * @param key
         */
        public void remove(String key){
            Node node = hashMap.get(key);
            removeNode(node);
            hashMap.remove(key);
        }

        /**
         * 重新规划Lru容器(将被访问的节点刷新到尾部)
         * @param node
         */
        private void refreshLru(Node node) {
            // 如果访问的节点为尾部节点，则不需要做任何修改
            if(node == end){
                return;
            }

            //移除该节点
            removeNode(node);

            //添加节点
            addNode(node);
        }

        /**
         * 移除节点
         * 主要分三种情况：
         * 1. 头结点
         * 2. 尾结点
         * 3. 中间节点
         * @param node 被删除的节点
         */
        private String removeNode(Node node) {
            // 1. 如果是头结点
            if(node == head){
                head = head.next;
            }else if(node == end){
                node = node.pre;
            }else{
                node.pre.next = node.next;
                node.next.pre = node.pre;
            }
            return node.key;
        }


        /**
         * 尾部添加节点
         * 思路：判断尾部是否为空。
         * 1. 如果为空，则直接赋值给尾部节点
         * 2. 如果不为空，则需要追加在尾部
         * @param node 需要添加的节点
         */
        private void addNode(Node node) {
            if(end != null){
                end.next = node;
                node.pre = end;
                node.next = null;
            }
            end = node;
            if(head == null){
                head = node;
            }
        }
    }
}
