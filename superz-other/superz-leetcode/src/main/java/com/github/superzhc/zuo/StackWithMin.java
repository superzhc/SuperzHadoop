package com.github.superzhc.zuo;

import java.util.Stack;

/**
 * 实现栈的功能再返回栈中最小元素的功能
 *
 * @author superz
 * @create 2021/10/20 13:19
 */
public class StackWithMin {
    /**
     * 实现方式：通过两个栈结构实现此功能，一个栈为正常元素，一个栈保存最小值
     */

    private Stack<Integer> stack = new Stack<>();
    private Stack<Integer> minStack = new Stack<>();

    public Integer push(Integer item) {
        stack.push(item);
        if (minStack.empty()) {
            minStack.push(item);
        } else if (item <= minStack.peek()) {
            minStack.push(item);
        }
        return item;
    }

    public Integer pop() {
        Integer num = stack.pop();
        if (num.equals(minStack.peek())) {
            minStack.pop();
        }
        return num;
    }

    public Integer peek() {
        return stack.peek();
    }

    public Integer min() {
        return minStack.peek();
    }

    public static void main(String[] args) {

    }
}
