package com.luxu.leetcode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by xulu on 2016/12/23.
 */
public class LongestSubstringWithoutRepeatingCharacters {
    public int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int j=0;
        int max_length=0;
        char[] c_array = s.toCharArray();
        Queue q = new LinkedList();
        while(j<c_array.length){
            while(q.contains(c_array[j])){
                q.poll();
            }
            q.add(c_array[j]);
            max_length = Math.max(max_length, q.size());
            j++;

        }
        return max_length;
    }
    public int countSegments(String s) {
        String[] arr = s.split(" ");
        int len = arr.length;
        for(String a:arr){
            if(a.trim().length()==0){
                len--;
            }
        }
        return len;
    }
}
