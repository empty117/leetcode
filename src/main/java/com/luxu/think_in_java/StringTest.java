package com.luxu.think_in_java;

/**
 * @author xulu
 * @date 9/29/2018
 */
public class StringTest {
    public static void main(String[] args){
        StringBuilder sb = new StringBuilder("test");
        sb.append("a");
        sb.append("b");
        System.out.println(sb.toString());
    }
}
