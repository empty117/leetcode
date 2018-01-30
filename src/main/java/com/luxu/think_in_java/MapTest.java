package com.luxu.think_in_java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xulu on 2017/9/20.
 */
public class MapTest {
    public static void main(String[] args){
        List<Integer> list = new CopyOnWriteArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
//        for(Integer i: list){
//            list.remove(2);
//        }
//        for(int i=0;i<list.size();i++){
//            list.remove(2);
//        }
        System.out.println(Runtime.getRuntime().freeMemory());
    }
}
