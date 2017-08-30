package com.luxu.leetcode;

import java.util.Arrays;

/**
 * Created by xulu on 2017/8/17.
 */
public class MaxSubArray_1 {

    public double findMaxAverage(int[] nums, int k) {
        int n = nums.length;
        double result = 0;

        int start=0, end=k-1;
        for(int i = start;i<n-k;i++){
            if(i>0){
                int removed=nums[start-1], added=nums[end+1];
                if(removed>=added){
                    continue;
                }
            }
            int[] subArray = Arrays.copyOfRange(nums,i,i+k-1);
            start=i;
            end=i+k-1;
            int sum = 0;
            for(int x: subArray){
                sum +=x;
            }
            result = Math.max(result, (double)sum/subArray.length);
        }
        return result;
    }
    public static void main(String[] args){

    }
}
