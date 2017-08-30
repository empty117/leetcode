package com.luxu.leetcode;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xulu on 2016/12/26.
 */
public class FindAllNumbersDisappeared_in_an_Array {
    public List<Integer> findDisappearedNumbers(int[] nums) {
        List<Integer> list = new LinkedList<Integer>();
        for(int i=1;i<=nums.length;i++){
            list.add(i);
        }
        for(int i=0;i<nums.length;i++){
            if(list.contains(nums[i])){
                list.remove(nums[i]);
            }
        }
        return list;
    }
}
