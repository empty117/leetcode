package com.luxu.leetcode.medium;

import java.util.Arrays;

/**
 * @author xulu
 * @date 1/29/2019
 */
public class _848 {

    public String shiftingLetters(String S, int[] shifts) {
        int len = shifts.length;
        int[] arr = new int[len];
        int tmp = 0;
        for(int i = len-1;i>=0;i--){
            tmp +=shifts[i];
            tmp = tmp%26;
            arr[i] = tmp;
        }
        System.out.println(Arrays.toString(arr));
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<len;i++){
            char c = S.charAt(i);
            sb.append(move(c, arr[i]));
        }
        return sb.toString();
    }

    private char move(char c, int number){
        int max = 'z',min = 'a';

        int result = c + number;

        if(result > max){
            result = min + result%max - 1;
        }
        return (char) result;
    }

    public static void main(String[] args){
        _848 test = new _848();
        int[] arr = {3,5,9};
        System.out.println(test.shiftingLetters("abc",arr));
    }
}
