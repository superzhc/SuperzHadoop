package com.github.superzhc.datastruct.graph;

import java.util.Stack;

/**
 * 2020年07月28日 superz add
 */
public class DepthFirstPaths
{
    private boolean[] marked;
    private int[] edgeTo;// 从起点到一个顶点的已知路径上的最后一个顶点
    private final int s;// 查找的起点

    public DepthFirstPaths(Graph G, int s) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        this.s = s;
        dfs(G, s);
    }

    private void dfs(Graph G, int v) {
        marked[v] = true;

        for (int w : G.adj(v)) {
            if (!marked[w]) {
                edgeTo[w] = v;// w的前一个顶点是v
                dfs(G, w);
            }
        }
    }

    public void pathTo(int v) {
        if (!marked[v]) {
            System.out.println(v + "不可达");
            return;
        }

        Stack<Integer> path = new Stack<>();
        for (int x = v; // 终点
                x != s; // 起点
                x = edgeTo[x])// 逆向，获取上一个节点的位置
        {
            path.push(x);
        }

        // 加上起点
        path.push(s);

        // 打印栈中的元素
        StringBuilder sb = new StringBuilder();
        while (!path.empty()) {
            sb.append("->").append(path.pop());
        }
        System.out.println(sb.substring(2).toString());
    }

    public static void main(String[] args) {
        DepthFirstPaths dfp = new DepthFirstPaths(G(), 0);
        dfp.pathTo(1);
        dfp.pathTo(2);
        dfp.pathTo(3);
        dfp.pathTo(4);
        dfp.pathTo(5);
        dfp.pathTo(6);
    }

    private static Graph G() {
        Graph G = new Graph(7);
        G.addEdge(0, 1);
        G.addEdge(0, 4);
        G.addEdge(0, 6);
        G.addEdge(1, 0);
        G.addEdge(1, 4);
        G.addEdge(2, 4);
        G.addEdge(2, 5);
        G.addEdge(2, 6);
        G.addEdge(4, 0);
        G.addEdge(4, 1);
        G.addEdge(4, 2);
        G.addEdge(5, 2);
        G.addEdge(5, 6);
        G.addEdge(6, 0);
        G.addEdge(6, 2);
        G.addEdge(6, 5);

        return G;
    }
}
