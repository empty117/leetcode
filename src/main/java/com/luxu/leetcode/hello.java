package com.luxu.leetcode;

/**
 * Created by xulu on 2016/12/26.
 */
public class hello{

    public boolean isIsomorphicSub(String s, String t) {

        for(int i=0;i<s.length();i++){
            char _s = s.charAt(i);
            char _t = t.charAt(i);
            if(_s!=_t){
                s = s.replace(s.charAt(i),t.charAt(i));
            }
        }
        return s.equals(t);
    }

    public boolean isIsomorphic(String s, String t) {
        return isIsomorphicSub(s,t)&&isIsomorphicSub(t,s);
    }

    public static void main(String[] args){

        System.out.println(new hello().isIsomorphic("ab","ca"));
    }
}
