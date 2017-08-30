package com.luxu.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xulu on 2016/12/27.
 */
public class FindDisappearedNumbers {
    public List<Integer> findDisappearedNumbers(int[] nums) {
        List<Integer> list = new ArrayList<Integer>();
        if(nums.length==0){
            return list;
        }
        Arrays.sort(nums);
        for(int i=0,j=1;i<nums.length;i++,j++){
            if(nums[i]==j){
                continue;
            }
            if(nums[i]==j-1){
                j--;
                continue;
            }
            if(nums[i]>j){
                int tmp = j;
                while(tmp<nums[i]){
                    list.add(tmp);
                    tmp++;
                }
                j = nums[i];
            }
        }
        if(nums[nums.length-1]<nums.length){
            int tmp=nums[nums.length-1]+1;
            while(tmp<=nums.length){
                list.add(tmp);
                tmp++;
            }
        }
        return list;
    }
}

