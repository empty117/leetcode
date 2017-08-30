package com.luxu.leetcode;

/**
 * Created by xulu on 2017/8/28.
 */
public class ReverseString2 {
    public String reverseStr(String s, int k) {
        StringBuilder sb = new StringBuilder();
        int len = s.length();
        int t = len/(k*2);
        int d = len%(k*2);
        int i=0;
        for(;i<=len-k*2;i+=k*2){
            sb.append(getReversedStr(s.substring(i,i+k*2),k));
        }
        sb.append(getReversedStr(s.substring(i,i+d),k));
        return sb.toString();
    }
    private StringBuilder getReversedStr(String s, int k){
//        System.out.println(s);
        int r_len = Math.min(s.length(),k);
//        System.out.println(new StringBuilder(s.substring(0,r_len)).reverse().append(s.substring(r_len,s.length())));
        return new StringBuilder(s.substring(0,r_len)).reverse().append(s.substring(r_len,s.length()));
    }

    public boolean checkRecord(String s) {
        int num_A = 0;
        int num_L = 0;
        for(int i=0;i<s.length();i++){
            char c = s.charAt(i);
            if(c=='A'){
                num_A++;
            }
            if(i+2<s.length() && c == 'L' && 'L' == s.charAt(i+1) && s.charAt(i+2)=='L'){
                return false;
            }
        }
        return num_A<=1;
    }

    public static void main(String[] args){
        System.out.println(new ReverseString2().reverseStr("abcdefg",2));
        String s = "aaa";

    }
}
