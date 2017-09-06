package com.luxu.leetcode;

import java.util.Arrays;

/**
 * Created by xulu on 2017/8/31.
 *
 Given a list of positive integers, the adjacent integers will perform the float division. For example, [2,3,4] -> 2 / 3 / 4.
 However, you can add any number of parenthesis at any position to change the priority of operations. You should find out how to add parenthesis to get the maximum result,
 and return the corresponding expression in string format. Your expression should NOT contain redundant parenthesis.

 Example:
 Input: [1000,100,10,2]
 Output: "1000/(100/10/2)"
 Explanation:
 1000/(100/10/2) = 1000/((100/10)/2) = 200
 However, the bold parenthesis in "1000/((100/10)/2)" are redundant,
 since they don't influence the operation priority. So you should return "1000/(100/10/2)".

 Other cases:
 1000/(100/10)/2 = 50
 1000/(100/(10/2)) = 50
 1000/100/10/2 = 0.5
 1000/100/(10/2) = 2
 */
public class _553_Optimal_Division {
    public String optimalDivision(int[] nums) {
        String result;
        result = bruteForce(nums);
        return result;
    }

    private String bruteForce(int[] nums){
        T t = optimal(nums,0,nums.length-1,"");
        return t.max_str;
    }

    class T{
        float max_val,min_val;
        String min_str, max_str;
    }
    public T optimal(int[] nums, int start, int end, String res){
        T t = new T();
        if(start == end){
            t.max_val = nums[start];
            t.min_val = nums[start];
            t.min_str = nums[start]+"";
            t.max_str = nums[start]+"";
            return t;
        }
        t.min_val = Float.MAX_VALUE;
        t.max_val = Float.MIN_VALUE;
        t.min_str = t.max_str = "";
        for(int i=start;i<end;i++){
            T left = optimal(nums, start, i,"");
            T right = optimal(nums, i+1,end,"");
            if(t.min_val>left.min_val/right.max_val){
                t.min_val = left.min_val/right.max_val;
                t.min_str = left.min_str + "/" + (i+1==end?"":"(")+right.max_str+(i+1==end?"":")");
            }
            if(t.max_val<left.max_val/right.min_val){
                t.max_val = left.max_val/right.min_val;
                t.max_str = left.max_str + "/" + (i+1==end?"":"(")+right.min_str+(i+1==end?"":")");
            }
        }
        return t;
    }
    public static void main(String[] args){
        _553_Optimal_Division optimal_division = new _553_Optimal_Division();
        String s = optimal_division.optimalDivision(new int[]{1000,100,10,2,3,1,2,5,65});
        System.out.println(s);
    }
}
