package com.github.superzhc.java.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * 集合
 *
 * @author superz
 * @create 2021/11/25 9:45
 */
public class CollectionMain {
    static class MyArrayList {
        /**
         * 默认初始容量是 10
         */
        ArrayList<String> arrayList;

        /**
         * 扩容：
         * ArrayList 每次新增元素时都会需要进行容量检测判断，若新增元素后元素的个数会超过 ArrayList 的容量，就会进行扩容操作来满足新增元素的需求。
         *
         * 注意事项：
         * 1. 计算新的容量大小为当前容量的 1.5 倍
         */
    }

    static class MyLinkedList {
        /**
         * LinkedList 通过一个 Node 数据结构来实现，包括 next、prev
         */
        LinkedList<String> linkedList;
    }

    static class MyHashMap {
        /**
         * 默认初始容量 16
         * 默认加载因子 0.75
         */
        HashMap<String, Object> hashMap;

        /**
         * HashMap 是一个链表散列，HashMap 底层实现还是数组，只是数组的每一项都是一个链条，数组里每个元素存储的都是一个链表的头节点。
         *
         * 扩容：
         * HashMap 的底层数组长度总是 2 的 n 次方
         */
    }

    static class MyLinkedHashMap{
        LinkedHashMap<String,Object> linkedHashMap;
    }
}
