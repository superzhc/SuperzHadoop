package com.github.superzhc.dataming.smile;

import smile.math.matrix.Matrix;

/**
 * 矩阵相关操作
 * @author superz
 * @create 2021/5/11 16:33
 */
public class MatrixMain {
    public static void main(String[] args) {
        Matrix matrix=Matrix.randn(4,4);
        System.out.println(matrix);

        // 逆矩阵
        Matrix invMatrix=matrix.inverse();
        System.out.println(invMatrix);

        System.out.println(matrix.mm(invMatrix));
    }
}
