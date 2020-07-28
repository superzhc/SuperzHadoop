package com.github.superzhc.datastruct.graph;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 广度优先搜索
 * 2020年07月28日 superz add
 */
public class BreadthFirstSearch
{
    private boolean[] marked;
    private final int s;

    public BreadthFirstSearch(Graph G, int s) {
        marked = new boolean[G.V()];
        this.s = s;
        bfs(G, s);
    }

    private void bfs(Graph G, int v) {
        Queue<Integer> queue = new LinkedList<>();

        marked[v] = true;
        queue.add(v);// 将起点加入队列
        while (!queue.isEmpty()) {
            int t = queue.poll();
            System.out.println(t);
            for (int w : G.adj(t)) {
                if (!marked[w]) {
                    marked[w] = true;
                    queue.add(w);
                }
            }
        }
    }

    public static void main(String[] args) {
        new BreadthFirstSearch(G(), 0);
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
