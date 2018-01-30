package com.luxu.think_in_java;

/**
 * @author xulu
 * @date 1/15/2018
 */
public class NullTest {
    public static void main(String[] args) throws Exception {
        Object a = null;
        String b = (String) a;
        System.out.println(Boolean.valueOf(b));
    }
}
