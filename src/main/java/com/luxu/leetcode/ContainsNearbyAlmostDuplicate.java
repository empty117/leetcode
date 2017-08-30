package com.luxu.leetcode;

import java.util.concurrent.atomic.AtomicBoolean;

public class ContainsNearbyAlmostDuplicate {
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        // Arrays.sort(nums);
        int len = nums.length;
        k = getAbs(k);
        if(k==0 || len <2){
            return false;
        }
        Math.sqrt(1);
        boolean x = nums.length>k;
        if(x){
            for(int j = 0; j<nums.length-k;j++){
                for(int i=j+1;i<=j+k;i++){
                    System.out.println("j="+j);
                    System.out.println(nums[i] + "##" + i + ", " + nums[j]+"##"+j);
                    if(getAbs(nums[i]-nums[j])> t){
                        return false;
                    }
                }
            }
        }
        else{
            for(int j = 0; j<nums.length;j++){
                for(int i=j+1;i<=nums.length-1;i++){
                    System.out.println("j="+j);
                    System.out.println(nums[i] + "##" + i + ", " + nums[j]+"##"+j);
                    if(getAbs(nums[i]-nums[j])> t){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int getAbs(int a){
        return Math.abs(a);
    }

    public static void main(String[] args){
        ContainsNearbyAlmostDuplicate solution = new ContainsNearbyAlmostDuplicate();
        int[] nums = {2,2};
        System.out.println(solution.containsNearbyAlmostDuplicate(nums,3,0));
    }
}