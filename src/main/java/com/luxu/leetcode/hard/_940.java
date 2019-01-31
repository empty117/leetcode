package com.luxu.leetcode.hard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xulu
 * @date 1/25/2019
 * 动态规划
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


    public int distinctSubseqIIDP(String S) {
        int MOD = 1_000_000_007;
        int N = S.length();
        int pre=1;
        int cur = pre;

        int[] last = new int[26];

        for (int i = 0; i < N; ++i) {
            int x = S.charAt(i) - 'a';
            cur = pre * 2 % MOD;
            cur -= last[x];
            last[x] = pre;
            cur %= MOD;
            pre = cur;
        }

        //减去一个空值
        cur--;
        if (cur < 0){
            cur += MOD;
        }
        return cur;
    }

    private static class TreeNode{
        private Map<Character, TreeNode> treeNodeMap;

        TreeNode(){
            treeNodeMap = new HashMap<>();
        }

        void insert(String s){
            for(char c: s.toCharArray()){
                this.treeNodeMap.put(c, new TreeNode());
            }
        }

        void insertRecursive(char c){

        }
    }


    public static void main(String[] args){
        _940 test = new _940();
        System.out.println(test.distinctSubseqII("zchmliaqdgvwncfatcfivphddpzjkgyygueikthqzyeeiebczqbqhdytkoawkehkbizdmcnilcjjlpoeoqqoqpswtqdpvszfaksn"));
    }
}
