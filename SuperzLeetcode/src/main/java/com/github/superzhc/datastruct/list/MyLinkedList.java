package com.github.superzhc.datastruct.list;

import java.util.Iterator;

/**
 * 2020年07月29日 superz add
 */
public class MyLinkedList<T>
{
    private int size;
    /* 头 */
    private Node head;
    // /* 尾 */
    // private Node tail;

    public MyLinkedList() {
        clear();
    }

    public void clear() {
        size = 0;
        head = new Node(null, null, null);
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public void add(int index, T item) {
        Node<T> node = getNode(index);

        /**
         *  新增节点的过程如下：
         *   A---------------->B 之间新增变为如下：
         *   A--------x------->B
         *   |prev            /\
         *  \/                |next
         *   ---------C-------
         */
        Node<T> n = new Node<T>(item, node.prev, node);
        node.prev.next = n;
        node.prev = n;

        size++;
    }

    public boolean add(T item) {
        add(size, item);
        return true;
    }

    public T get(int index) {
        return getNode(index).data;
    }

    public T set(int index, T newVal) {
        Node<T> node = getNode(index);
        T oldVal = node.data;
        node.data = newVal;
        return oldVal;
    }

    private T remove(int index) {
        return remove(getNode(index));
    }

    private T remove(Node<T> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;

        size--;

        return node.data;
    }

    private Node<T> getNode(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();

        Node<T> node = head;
        for (int i = index; i > -1; i--) {
            node = node.next;
        }
        return node;
    }

    private static class Node<T>
    {
        private T data;
        private Node prev;
        private Node next;

        public Node(T data, Node<T> prev, Node<T> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private class LinkedListIterator implements Iterator<T>{

        // TODO

        private Node<T> current=head;


        @Override public boolean hasNext() {
            return false;
        }

        @Override public T next() {
            return null;
        }

        @Override public void remove() {

        }
    }
}
