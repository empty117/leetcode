package com.luxu.leetcode.medium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xulu
 * @date 12/24/2018
 */
public class _46 {

    private List<List<Integer>> result = new ArrayList<>();

    public List<List<Integer>> permute(int[] nums) {
        test(nums,0);
        return result;
    }

    public void test(int[] a,int b){
        if(b>=a.length){
            List<Integer> item = Arrays.stream(a).boxed().collect(Collectors.toList());
            System.out.println(item);
            result.add(item);
        }
        for (int i = b; i < a.length; i++) {
            if(i!=b){
                swap(a,b,i);
            }
            test(a,b+1);//用递归进行下一个数的交换
            swap(a,b,i);
        }
    }

    private void swap(int[] arr, int i, int j ){
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
    public static void main(String[] args){
        new _46().permute(new int[]{1,2,3,4});
    }
}
