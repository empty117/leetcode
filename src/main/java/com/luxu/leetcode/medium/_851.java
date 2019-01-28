package com.luxu.leetcode.medium;

import java.util.*;

/**
 * @author xulu
 * @date 12/21/2018
 */
public class _851 {

//    ArrayList<Integer>[] graph;
    Map<Integer,List<Integer>> graph;
    int[] answer;
    int[] quiet;

    public int[] loudAndRich(int[][] richer, int[] quiet) {
        int N = quiet.length;
        /*
        图
         */
//        graph = new ArrayList[N];
        graph  = new HashMap<>();
        answer = new int[N];
        this.quiet = quiet;

        for (int node = 0; node < N; node++){
            graph.put(node, new ArrayList<>());
        }


        for (int[] edge: richer){
//            graph[edge[1]].add(edge[0]);
            graph.get(edge[1]).add(edge[0]);
        }


        Arrays.fill(answer, -1);

        for (int node = 0; node < N; ++node){
            dfs(node);
        }
        return answer;
    }

    public int dfs(int node) {
        if (answer[node] == -1) {
            answer[node] = node;
            for (int child: graph.get(node)) {
                int cand = dfs(child);
                if (quiet[cand] < quiet[answer[node]]){
                    answer[node] = cand;
                }
            }
        }
        return answer[node];
    }
}
