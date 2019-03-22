package com.luxu.leetcode.medium;

import java.util.*;

/**
 * @author xulu
 * @date 3/20/2019
 * DP
 */
public class _983 {
    int[] days, costs;
    Integer[] memo;
    int[] durations = new int[]{1, 7, 30};

    public int mincostTickets(int[] days, int[] costs) {
        this.days = days;
        this.costs = costs;
        memo = new Integer[days.length];

        return dp(0);
    }

    public int dp(int i) {
        if (i >= days.length){
            return 0;
        }
        if (memo[i] != null){
            return memo[i];
        }

        int minCost = Integer.MAX_VALUE;
        int j = i;
        for (int k = 0; k < 3; ++k) {
            //Find largest j
            while (j < days.length && days[j] < days[i] + durations[k]){
                j++;
            }
            minCost = Math.min(minCost, dp(j) + costs[k]);
        }

        memo[i] = minCost;
        return minCost;
    }

    public static void main(String[] args){
        System.out.println(new _983().mincostTickets(new int[]{1,4,6,7,8,20},new int[]{7,2,15}));
    }
}
