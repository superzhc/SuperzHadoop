package com.github.superzhc.datastruct.graph;

/**
 * 深度优先搜索
 * 2020年07月28日 superz add
 */
public class DepthFirstSearch
{
    private boolean[] marked;// 用来标记顶点

    public DepthFirstSearch(Graph G, int s) {
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    private void dfs(Graph G, int v) {
        System.out.println(v);
        marked[v] = true;// 标记顶点

        for (int w : G.adj(v)) {
            if (!marked[w])
                dfs(G, w);
        }
    }

    public static void main(String[] args) {
        new DepthFirstSearch(G(),0);
    }

    private static Graph G(){
        Graph G=new Graph(7);
        G.addEdge(0,1);
        G.addEdge(0,4);
        G.addEdge(0,6);
        G.addEdge(1,0);
        G.addEdge(1,4);
        G.addEdge(2,4);
        G.addEdge(2,5);
        G.addEdge(2,6);
        G.addEdge(4,0);
        G.addEdge(4,1);
        G.addEdge(4,2);
        G.addEdge(5,2);
        G.addEdge(5,6);
        G.addEdge(6,0);
        G.addEdge(6,2);
        G.addEdge(6,5);

        return G;
    }
}
