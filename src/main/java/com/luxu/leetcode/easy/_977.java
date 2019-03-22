package com.luxu.leetcode.easy;

import java.util.Arrays;

/**
 * @author xulu
 * @date 3/15/2019
 */
public class _977 {
    public int[] sortedSquares(int[] A) {
        return Arrays.stream(A).map( i -> Math.abs(i)).sorted().toArray();
    }
}
