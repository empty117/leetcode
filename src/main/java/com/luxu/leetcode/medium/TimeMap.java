package com.luxu.leetcode.medium;

import java.util.*;

/**
 * @author xulu
 * @date 3/19/2019
 * 981
 * https://leetcode-cn.com/contest/weekly-contest-121/problems/time-based-key-value-store/
 */
public class TimeMap {

    private Map<String, Value> values;

    /** Initialize your data structure here. */
    public TimeMap() {
        values = new HashMap<>();
    }

    public void set(String key, String value, int timestamp) {
        Value cValue = values.get(key);
        if(cValue==null){
            values.put(key, new Value(value,timestamp));
        }
        else{
            cValue.set(value,timestamp);
        }
    }

    public String get(String key, int timestamp) {
        return values.get(key).get(timestamp);
    }

    private class Value{
        private Map<Integer, String> timeBasedValue = new TreeMap<>();

        private int max;

        Value(){
            timeBasedValue.put(0,"");
            max = 0;
        }

        Value(String value, int timestamp){
            timeBasedValue.put(0,"");
            timeBasedValue.put(timestamp,value);
            max = timestamp;
        }

        void set(String value, int timestamp){
            timeBasedValue.put(timestamp,value);
            max = timestamp;
        }

        public String get(int timestamp){
            String value = timeBasedValue.get(timestamp);
            if(value != null){
                return value;
            }
            if(timestamp>=max){
                return timeBasedValue.get(max);
            }
            int current = 0;
            for(int i : timeBasedValue.keySet()){
                if(timestamp>i){
                    current = i;
                    continue;
                }
                return timeBasedValue.get(current);
            }
            return "";
        }
    }

    public static void main(String[] args){
        TimeMap timeMap = new TimeMap();
        timeMap.set("love","high",10);
        timeMap.set("love","low",20);
        System.out.println(timeMap.get("love",5));
        System.out.println(timeMap.get("love",10));
        System.out.println(timeMap.get("love",15));
        System.out.println(timeMap.get("love",20));
        System.out.println(timeMap.get("love",25));

    }
}
