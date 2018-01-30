//package com.luxu.leetcode;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
///**
// * Created by xulu on 2017/9/26.
// */
//public class LRUCache {
//    private Map<Integer,Integer> cache;
//    public LRUCache(int capacity) {
//        cache = new LinkedHashMap<>(capacity, 0.75f,true){
//            protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
//                return size() > capacity;
//            }
//        };
//    }
//
//    public int get(int key) {
//        if(cache.containsKey(key)){
//            return cache.get(key);
//        }
//        return -1;
//    }
//
//    public void put(int key, int value) {
//        cache.put(key,value);
//    }
//}
