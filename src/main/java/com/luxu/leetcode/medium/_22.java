package com.luxu.leetcode.medium;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xulu
 * @date 1/8/2019
 */
public class _22 {

    private int n;
    public List<String> generateParenthesis(int n) {
        this.n = n;
        List<String> result = new ArrayList<>();
        backtrack(result,"",0,0);
        return result;
    }

    private void backtrack(List<String> result, String s, int left, int right){
        if(s.length() == n *2){
            result.add(s);
            return;
        }
        if(left<n){
            backtrack(result, s + "(",left+1,right);
        }
        if(right<left){
            backtrack(result, s + ")",left,right+1);
        }
    }
}
