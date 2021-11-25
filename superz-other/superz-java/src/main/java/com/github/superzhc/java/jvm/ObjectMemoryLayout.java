package com.github.superzhc.java.jvm;

/**
 * @author superz
 * @create 2021/11/25 11:56
 */
public class ObjectMemoryLayout {

    static class Header{
        /**
         * 对象头
         *
         * 1. 存储对象本身的运行时数据，如哈希码（HashCode）、GC 分代年龄、锁状态标志、线程持有的锁、偏向线程 ID、偏向时间戳，这部分数据的长度在 32 位和 64 位虚拟机（未开启压缩指针）中分别为 32bit(4 bytes) 和 64bit(8 bytes)，官方称它为 Mark Word
         * 2. 类型指针，即对象指向它的类元数据的指针，虚拟机通过这个指针来确定这个对象是哪个类的实例
         */
    }

    static class InstanceData{
        /**
         * 实例数据
         *
         * 实例数据部分是对象真正存储的有效信息，也就是在程序代码中所定义的各种类型的字段内容
         */
    }

    static class Padding{
        /**
         * 对齐填充
         */
    }
}
