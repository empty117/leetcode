package com.luxu.leetcode.medium;

/**
 * @author xulu
 * @date 2/19/2019
 */
public class _856 {
    char[] chars;

    public int scoreOfParentheses(String S) {
        this.chars = S.toCharArray();
        return score(0,chars.length-1);
    }

    private int score(int start , int end){
        if(start>=end){
            return 0 ;
        }
        if(chars[start]==')'){
            return 0;
        }
        if(chars[start+1]==')'){
            return 1 + score(start+2,end);
        }
        int left = 1,index = start+1;
        while(index < end && left>0){
            if(chars[index]=='('){
                left++;
            }
            else{
                left--;
            }
            index++;
        }
        return 2 * score(start+1,index-1) + score(index,end);
    }

    public static void main(String[] args){
        _856 test = new _856();
        System.out.println(test.scoreOfParentheses("((()"));
    }
}
