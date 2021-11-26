package com.github.superzhc.java.jvm;

/**
 * @author superz
 * @create 2021/11/25 13:45
 */
public class HeapMain {
    int Eden = 8;
    int From_Survivor = 1;
    int To_Survivor = 1;
    int old = 20;

    int young() {
        return Eden + From_Survivor + To_Survivor;
    }
}
