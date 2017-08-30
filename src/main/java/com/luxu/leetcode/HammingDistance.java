package com.luxu.leetcode;

import java.util.*;

/**
 * Created by xulu on 2016/12/25.
 */
public class HammingDistance {
    public int hammingDistance(int x, int y) {
        //亦或， 相同为0，不同为1
        int result = x^y;
        int i=0;
        while(result>0){
            i += result%2;
            result = result/2;
        }
        return i;
    }
    public static void main(String[] args){
//        int n = (int)(Math.pow((double)2,double(1/num)));
        int aaa = (int)(Math.log(2147483647) / Math.log(2));
//        String a = new String("aaa");
//        String b = "aaa";
//        System.out.println(b == a.intern());
        String s3 = new String("1");
        s3.intern();
        String s4 = "1";
        System.out.println(s4 == s3);
        String s = "A man, a plan, a canal: Panama";
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<s.length();i++){
           char c = s.charAt(i);
           if(Character.isAlphabetic(c)|| Character.isDigit(c)){
                sb.append(c);
            }
        }
//        return sb.toString().equalsIgnoreCase().equals(sb.reverse().toString());
    }
}
