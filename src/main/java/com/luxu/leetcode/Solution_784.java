package com.luxu.leetcode;

import java.util.*;

/**
 * @author xulu
 * @date 7/27/2018
 */
public class Solution_784 {

    public List<String> letterCasePermutation(String S) {
        List<String> list = new ArrayList<>();
        list.add(S);
        int idx = 0;
        while(idx < S.length()) {
            char ch = S.charAt(idx);
            if(Character.isLetter(ch)) {
                List<String> nextList = new ArrayList<>();
                for(String str : list) {
                    StringBuilder sb = new StringBuilder(str);
                    sb.setCharAt(idx, Character.toUpperCase(ch));
                    nextList.add(sb.toString());
                    sb.setCharAt(idx, Character.toLowerCase(ch));
                    nextList.add(sb.toString());
                }
                list = nextList;
            }
            idx++;
        }
        return list;
    }

    public static void main(String[] args){
        Solution_784 solution_784 = new Solution_784();
        System.out.println(solution_784.letterCasePermutation("asI"));
    }
}
