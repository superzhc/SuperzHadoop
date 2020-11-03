package com.github.superzhc.bit;

/**
 * 2020年07月03日 superz add
 */
public class BitUtils
{
    public static byte[] byte2bit(byte b) {
        byte[] arr = new byte[8];

        return arr;
    }

    // 原码
    // public static byte[] sourceCode(int num) {
    //
    //
    // int quotient = num / 2;// 商
    // int remainder = num % 2;// 余数
    // }

    // 反码

    // 补码

    public static int and(int num1, int num2) {
        return num1 & num2;
    }

    public static int or(int num1, int num2) {
        return num1 | num2;
    }

    public static int not(int num) {
        return ~num;
    }

    /**
     * 异或
     * @param num1
     * @param num2
     * @return
     */
    public static int xor(int num1, int num2) {
        return num1 ^ num2;
    }

    /**
     * 右移运算
     * 该方法可以实现：num/(2的space次方)
     * @param num
     * @param space
     * @return
     */
    public static int right(int num, int space) {
        return num >> space;
    }

    /**
     * 左移运算
     * 该方法可实现：num*(2的space次方)
     * @param num
     * @param space
     * @return
     */
    public static int left(int num, int space) {
        return num << space;
    }

    public static void main(String[] args) {
        System.out.println(and(3, 5));
        System.out.println(or(3, 5));
        System.out.println(not(5));
        System.out.println(xor(3, 5));
        System.out.println(right(5, 1));
        System.out.println(left(1, 8));
    }
}
