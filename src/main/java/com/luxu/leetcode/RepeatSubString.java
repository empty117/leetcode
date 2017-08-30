package com.luxu.leetcode;

/**
 * Created by xulu on 2017/8/28.
 */
public class RepeatSubString {

    public boolean detectCapitalUse(String word) {
        boolean firstLetterLow =  Character.isLowerCase(word.charAt(0));
        word = word.substring(1,word.length());
        if(firstLetterLow){
          return word.equals(word.toLowerCase());
        }
        else{
           return word.equals(word.toLowerCase())|| word.equals(word.toUpperCase());
        }
    }

    public boolean repeatedSubstringPattern(String s) {
        int len = s.length();
        for(int i=1;i<=len/2;i++){
            if(len%i==0){
                if(isSub(s,i)){
                    return true;
                }
            }
        }
        return false;
    }
    private boolean isSub(String s, int l){
        String sub = s.substring(0,l);
        // System.out.println(l);
        for(int i=l;i<=s.length();i+=l){
            // System.out.println(s.substring(i,i+l));
            if(i+l<=s.length()&&!sub.equals(s.substring(i,i+l))){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args){
        new RepeatSubString().repeatedSubstringPattern("ababac");
    }
}
