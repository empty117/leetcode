package com.luxu.leetcode;

class Solution_917 {
    public String reverseOnlyLetters(String s) {
        int len = s.length();
        char[] chars = new char[len];
        int i=0,j=len-1;
        while(i<len && j>i){
           char c = s.charAt(i);
           if(!Character.isAlphabetic(c)){
               chars[i] = c;
           }
           else{
               char c1 = s.charAt(j);
               if(!Character.isAlphabetic(c1)){
                   chars[j]=c1;
                   j--;
                   continue;
               }
               chars[i]=s.charAt(j);
               chars[j]=c;
               j--;
           }
           i++;
        }
        return String.valueOf(chars);
    }
    public static void main(String[] args){
        String s = "a-bC-dEf-ghIj";
        System.out.println(new Solution_917().reverseOnlyLetters(s));
    }
}