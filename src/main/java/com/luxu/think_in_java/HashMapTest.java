package com.luxu.think_in_java;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by xulu on 2017/9/26.
 */
public class HashMapTest {
    public static void main(String[] args){
        Map<Integer, String> test = new ConcurrentSkipListMap<>();
        test.put(2,"b");
        test.put(4,"d");
        test.put(1,"a");
        System.out.println(test);

    }
}
