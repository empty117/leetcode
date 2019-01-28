package com.luxu.leetcode.medium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xulu
 * @date 1/8/2019
 */
public class _969 {
    public List<Integer> pancakeSort(int[] A) {
        List<Integer> result = new ArrayList<>();
        int len = A.length;
        int pos = len-1;
        while(pos>0){
            int max =0,maxIndex=-1;
            for(int i=0;i<=pos;i++){
                if(A[i]>max){
                    max = A[i];
                    maxIndex = i;
                }
            }
            if(maxIndex!=pos){
                if(maxIndex!=0){
                    result.add(maxIndex+1);
                    sort(A,maxIndex);
                }
                result.add(pos+1);
                sort(A,pos);
            }
            pos--;
        }
        return result;
    }

    private void sort(int[] a, int index){
//        System.out.println(Arrays.toString(a));
        int[] copyA = Arrays.copyOf(a,index+1);
        for(int i=0;i<=index;i++){
            a[i] = copyA[index-i];
        }
    }

    public static void main(String[] args){
        _969 test = new _969();
        System.out.println(test.pancakeSort(new int[]{5,1,3,4,9}));
    }
}
