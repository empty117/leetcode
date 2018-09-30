package com.luxu.leetcode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by xulu on 2017/9/26.
 */
public class LRUCache {
    private Map<Integer,Integer> cache;
    public LRUCache(int capacity) {
        cache = new MyMap<>(capacity);
    }

    public int get(int key) {
        if(cache.containsKey(key)){
            return cache.get(key);
        }
        return -1;
    }

    public void put(int key, int value) {
        cache.put(key,value);
    }

    private static class MyMap<K,V> extends LinkedHashMap<K,V>{

        private int capacity;

        public MyMap(int capacity){
            super(capacity, 0.75f, true);
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }

    public static void main(String[] args){
        LRUCache lruCache = new LRUCache(3);
        lruCache.put(1,1);
        lruCache.put(2,2);
        lruCache.put(3,3);
        lruCache.get(1);
        lruCache.put(4,4);
        System.out.println(lruCache.cache.size());
    }
}
