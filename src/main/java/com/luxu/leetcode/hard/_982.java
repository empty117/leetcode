package com.luxu.leetcode.hard;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xulu
 * @date 3/15/2019
 *
 * */
public class _982 {

    public int countTriplets(int[] A) {
        int sum = 0 ;
        for(int i = 0; i<A.length;i++){
            for(int j=0;j<A.length;j++){
                for(int k=0;k<A.length;k++){
                    if((A[i]&A[j]&A[k])==0){
                        sum++;
                    }
                }
            }
        }
        return sum;
    }

    public static void main(String[] args){
        System.out.println(((1<<11) - 1)&122);
//        System.out.println(new _982().countTriplets(new int[]{0,0,0,0}));
    }
}
