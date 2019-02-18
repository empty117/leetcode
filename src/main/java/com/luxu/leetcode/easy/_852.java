package com.luxu.leetcode.easy;

/**
 * @author xulu
 * @date 1/31/2019
 */
public class _852 {

    public int peakIndexInMountainArray(int[] A) {
        int len = A.length;

        for(int i=0;i<len-1;i++){
            if(A[i+1]<A[i]){
                return i;
            }
        }
        return -1;
    }
}
