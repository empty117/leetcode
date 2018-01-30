package com.luxu.leetcode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xulu on 2017/9/12.
 * Given n pairs of parentheses, write a function to generate all combinations of well-formed parentheses.
 For example, given n = 3, a solution set is:
 [
 "((()))",
 "(()())",
 "(())()",
 "()(())",
 "()()()"
 ]
 */
public class GenerateParentheses_22 {
    private Map<Integer,Set<String>> result = new HashMap<>();
    public List<String> generateParenthesis(int n) {
        result.put(1,new HashSet<>());
        result.get(1).add("()");
        for(int i=1;i<n;i++){
            int finalI = i+1;
            result.get(i).stream().forEach(item-> test(finalI,item));
        }
        return result.get(n).stream().collect(Collectors.toList());
    }

    private void test(int i, String base){
        String s1 = "(" + base + ")";
        String s2 = "()" + base;
        String s3 = base + "()";
        if(result.get(i)==null){
            result.put(i,new TreeSet<>());
        }
        result.get(i).add(s1);
        result.get(i).add(s2);
        result.get(i).add(s3);
    }

    public static void main(String[] args){
        GenerateParentheses_22 generateParentheses_22 = new GenerateParentheses_22();
        System.out.println(generateParentheses_22.generateParenthesis(3));
    }
}
