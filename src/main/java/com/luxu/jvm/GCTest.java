package com.luxu.jvm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by xulu on 2017/7/20.
 */
public class GCTest {
    private static final int _1MB = 1024 * 1024;

    public static void test() {
        byte[] a1, a2, a3, a4, a5;
        a1 = new byte[2 * _1MB];
        a2 = new byte[2 * _1MB];
        a3 = new byte[2 * _1MB];
        a4 = new byte[3 * _1MB];
        a5 = new byte[3 * _1MB];
//        System.gc();
//        a5 = new byte[2*_1MB];
//        a6 = new byte[2*_1MB];
    }
//
    public static void main(String[] args) {

        test();
//        System.out.println(Arrays.toString(new Integer[5]));

    }
}

