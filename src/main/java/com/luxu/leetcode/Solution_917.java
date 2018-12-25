package com.luxu.leetcode;

class Solution_917 {
    public String reverseOnlyLetters(String s) {
        if(s == null || s.isEmpty()){
            return s;
        }
        char[] chars = s.toCharArray();
        int len = s.length();

        int i=0,j=len-1;
        while(i<len && j>i){
            boolean left=Character.isAlphabetic(chars[i]),right=Character.isAlphabetic(chars[j]);
            if(left && right){
                swap(chars,i,j);
                i++;
                j--;
            }
            else if(!left && right){
                i++;
            }
            else if(left && !right){
                j--;
            }
            else{
                i++;
                j--;
            }
        }
        return String.valueOf(chars);
    }

    private void swap(char[] chars, int a, int b){
        char tmp = chars[a];
        chars[a] = chars[b];
        chars[b] = tmp;
    }
    public static void main(String[] args){
        String s = "a-bC-dEf-ghIj";
        System.out.println(new Solution_917().reverseOnlyLetters(s));
    }
}