package com.github.superzhc.datastruct.graph;

import java.util.LinkedList;

/**
 * 2020年07月28日 superz add
 */
public class Graph
{
    private final int V;// 顶点数目
    private int E;// 边的数目
    private LinkedList<Integer> adj[];// 邻接表

    public Graph(int V) {
        // 创建邻接表
        // 将所有链表初始化
        this.V = V;
        this.E = 0;
        adj = new LinkedList[V];
        for (int v = 0; v < V; ++v) {
            adj[v] = new LinkedList<>();
        }
    }

    /**
     * 获取顶点数目
     * @return
     */
    public int V() {
        return V;
    }

    /**
     * 获取边的数目
     * @return
     */
    public int E() {
        return E;
    }

    public void addEdge(int v, int w) {
        /* 将w添加到v的链表中 */
        adj[v].add(w);
        /* 将v添加到w的链表中 */
        adj[w].add(v);
        E++;
    }

    public Iterable<Integer> adj(int v) {
        return adj[v];
    }
}
