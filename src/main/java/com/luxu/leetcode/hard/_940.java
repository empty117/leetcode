package com.luxu.leetcode.hard;

/**
 * @author xulu
 * @date 1/25/2019
 * 单词查找树
 */
public class _940 {
    private int factor = (int) (Math.pow(10,9) + 7);
    public int distinctSubseqII(String S) {
        int[] arr = new int[26];
        for(char c:S.toCharArray()){
            arr[c - 97] = total(arr) + 1;
        }
        return total(arr);
    }

    private int total(int[] arr){
        int sum = 0;
        for(int i :arr){
            sum+=i;
            sum = sum%factor;
        }
        return sum%factor;
    }


    public static void main(String[] args){
        _940 test = new _940();
        System.out.println(test.distinctSubseqII("zchmliaqdgvwncfatcfivphddpzjkgyygueikthqzyeeiebczqbqhdytkoawkehkbizdmcnilcjjlpoeoqqoqpswtqdpvszfaksn"));
    }
}
