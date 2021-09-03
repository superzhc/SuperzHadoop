package com.github.superzhc.dataming;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * @author superz
 * @create 2021/5/8 17:32
 */
public class ND4JMain {
    public static void main(String[] args) {
        // 创建一个全0的矩阵
        INDArray zeros = Nd4j.zeros(3, 5);
        System.out.println(zeros);

        System.out.println();
        System.out.println("---------------------华丽分割线---------------------");
        System.out.println();

        // 创建一个全1的矩阵
        INDArray ones = Nd4j.ones(3, 5);
        System.out.println(ones);

        System.out.println();
        System.out.println("---------------------华丽分割线---------------------");
        System.out.println();

        // 创建一个随机矩阵
        INDArray rand = Nd4j.rand(3, 5);
        System.out.println(rand);
    }
}
