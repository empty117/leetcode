package com.luxu.leetcode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xulu on 2017/9/26.
 */
public class LFUCache {
    class HitRate implements Comparable<HitRate> {
        Integer key;
        Integer hitCount; // 命中次数
        Long lastTime; // 上次命中时间

        public HitRate(Integer key, Integer hitCount, Long lastTime) {
            this.key = key;
            this.hitCount = hitCount;
            this.lastTime = lastTime;
        }

        public int compareTo(HitRate o) {
            int hr = hitCount.compareTo(o.hitCount);
            return hr != 0 ? hr : lastTime.compareTo(o.lastTime);
        }
    }

    private int capacity;
    private Map<Integer,Integer> cache;
    private Map<Integer,HitRate> hitRecord;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>(capacity);
        this.hitRecord = new HashMap<>(capacity);
    }

    private Integer getKickedKey(){
        HitRate min = Collections.min(hitRecord.values());
        return min.key;
    }

    public int get(int key) {
        if(cache.containsKey(key)){
            HitRate v = hitRecord.get(key);
            v.hitCount++;
            v.lastTime = System.nanoTime();
            return cache.get(key);
        }
        return -1;
    }

    public void put(int key, int value) {
        if(capacity > 0){
            if(!cache.containsKey(key)){
                //size is full
                if(cache.size() == capacity){
                    Integer k = getKickedKey();
                    cache.remove(k);
                    hitRecord.remove(k);
                }
                cache.put(key, value);
                hitRecord.put(key, new HitRate(key,0,System.nanoTime()));
            }
            else {
                cache.put(key,value);
                get(key);
            }
        }
    }

    public static void main(String[] args){
        LFUCache lfuCache = new LFUCache(2);
        lfuCache.put(1,1);
        lfuCache.put(2,2);
        System.out.println(lfuCache.get(1));
    }
}
