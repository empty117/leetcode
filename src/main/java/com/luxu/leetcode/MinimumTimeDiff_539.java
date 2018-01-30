package com.luxu.leetcode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xulu on 2017/9/11.
 */
public class MinimumTimeDiff_539 {
    public int findMinDifference(List<String> timePoints) {
        int len = timePoints.size();
        Integer[] nums = timePoints.stream().map(s->Integer.parseInt(s.replace(":", ""))).collect(Collectors.toList()).toArray(new Integer[len]);
        Arrays.sort(nums);
        int gap = Integer.MAX_VALUE;
        for (int i = 0; i < len - 1; i++) {
            gap = Math.min(gap, gapInMinutes(nums[i], nums[i + 1]));
        }
        return Math.min(gap, gapInMinutes(nums[len - 1], nums[0]));
    }

    private int gapInMinutes(int input1, int input2) {
        if (input2 < input1) {
            input2 = input2 + 2400;
        }
        return (input2 % 100 - input1 % 100) + (input2 / 100 - input1 / 100) * 60;
    }

    public static void main(String[] args) {
        MinimumTimeDiff_539 minimumTimeDiff_539 = new MinimumTimeDiff_539();
        int r = minimumTimeDiff_539.findMinDifference(Arrays.asList("21:50", "23:45", "00:00"));
        System.out.println(r);
//        System.out.println(minimumTimeDiff_539.gapInMinutes(2359,0000));
    }
}
