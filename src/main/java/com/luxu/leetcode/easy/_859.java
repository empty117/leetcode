package com.luxu.leetcode.easy;

import java.util.HashSet;
import java.util.Set;

/**
 * @author xulu
 * @date 2/19/2019
 */
public class _859 {
    public boolean buddyStrings(String A, String B) {
        int len = A.length();
        if(len!= B.length()){
            return false;
        }
        char tmp1 = '0', tmp2='0';
        boolean result = false;
        int left =2, same = 0;
        Set<Character> set = new HashSet<>();
        for(int i =0;i<len;i++){
            if(A.charAt(i)!=B.charAt(i)){
                if(tmp1=='0'){
                    tmp1 = A.charAt(i);
                    tmp2 = B.charAt(i);
                }
                else{
                    result = (B.charAt(i) == tmp1) && (A.charAt(i)==tmp2);
                }
                left--;
            }
            else{
                set.add(A.charAt(i));
                same++;
            }
        }
        if(left==2){
            return same>set.size();
        }
        return result && left==0;
    }

    public static void main(String[] args){
        _859 test = new _859();
        System.out.println(test.buddyStrings("ab","ca"));
    }
}
