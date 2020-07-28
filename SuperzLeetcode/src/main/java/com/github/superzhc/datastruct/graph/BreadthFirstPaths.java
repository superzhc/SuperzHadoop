package com.github.superzhc.datastruct.graph;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 2020年07月28日 superz add
 */
public class BreadthFirstPaths
{
    private boolean[] marked;
    private int[] edgeTo;
    private final int s;

    public BreadthFirstPaths(Graph G, int s) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        this.s = s;
        bfs(G, s);
    }

    private void bfs(Graph G, int v) {
        Queue<Integer> queue = new LinkedList<>();

        marked[v] = true;
        queue.add(v);// 将起点加入队列
        while (!queue.isEmpty()) {
            int t = queue.poll();
            for (int w : G.adj(t)) {
                if (!marked[w]) {
                    marked[w] = true;
                    edgeTo[w] = t;
                    queue.add(w);
                }
            }
        }
    }

    public void pathTo(int v) {
        if (!marked[v]) {
            System.out.println(v + "路径不存在");
            return;
        }

        Stack<Integer> path = new Stack<>();

        for (int x = v; x != s; x = edgeTo[x]) {
            path.push(x);
        }

        path.push(s);

        // 打印栈中的元素
        StringBuilder sb = new StringBuilder();
        while (!path.empty()) {
            sb.append("->").append(path.pop());
        }
        System.out.println(sb.substring(2).toString());
    }

    public static void main(String[] args) {
        BreadthFirstPaths dfp = new BreadthFirstPaths(G(), 0);
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
