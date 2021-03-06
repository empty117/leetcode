package com.luxu.leetcode.medium;


/**
 * @author xulu
 * @date 12/20/2018
 * Backtracking
 */
public class _526 {

    int count = 0;

    public int countArrangement(int N) {
        if (N == 0){
            return 0;
        }
        helper(N, 1, new int[N + 1]);
        return count;
    }

    private void helper(int N, int pos, int[] used) {
        if (pos > N) {
            count++;
            return;
        }

        /*
        循环遍历每一个元素,如果遍历到最后一个,则满足
         */
        for (int i = 1; i <= N; i++) {
            if (used[i] == 0 && (i % pos == 0 || pos % i == 0)) {
                used[i] = 1;
                helper(N, pos + 1, used);
                used[i] = 0;
            }
        }
    }

    public static void main(String[] args){
        new _526().countArrangement(2);
    }
}
