package com.luxu.leetcode;

/**
 * Created by xulu on 2017/8/29.
 */
public class PalindromicSubstrings {
    public int countSubstrings(String s) {
        int result = 1;
        int len = s.length();
        for(int i=1;i<len;i++){
            result+=getPalindromicCount(result,s.substring(0,i), s.charAt(i));
        }
        return result;
    }

    public int countSubstrings2(String s) {
        int result = 0;
        int len = s.length();
        for(int i=1;i<=len;i++){
            for(int j=0;j<=len-i;j++){
                if(isPalindrome(s.substring(j,j+i))){
                    result++;
                }
            }
        }
        return result;
    }

    private int getPalindromicCount(int n, String s, char c){
        int result = 1;
        for(int i=s.length()-1;i>=0;i--){
            String sub_s = s.substring(i,s.length())+c;
            if(isPalindrome(sub_s)){
                result++;
            }
        }
        return result;
    }

    private boolean isPalindrome(String s){
        StringBuilder sb = new StringBuilder(s);
        return sb.reverse().toString().equals(s);
    }

    public static void main(String[] args){
        PalindromicSubstrings palindromicSubstrings = new PalindromicSubstrings();
        int n = palindromicSubstrings.countSubstrings2("aaaaa");
        System.out.println(n);
    }
}
