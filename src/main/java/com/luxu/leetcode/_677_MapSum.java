package com.luxu.leetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author xulu
 * @date 7/12/2018
 */
public class _677_MapSum {
    private Map<String,Integer> map = new HashMap<>();

    /** Initialize your data structure here. */
    public _677_MapSum() {
    }

    public void insert(String key, int val) {
        map.put(key,val);
    }

    public int sum(String prefix) {
        return map.keySet().stream().filter(k->k.startsWith(prefix))
                .map(k->map.get(k))
                .reduce(0,(sum,value)->sum+value,(a,b)->a+b);
    }

    public static void main(String[] args){
        _677_MapSum mapSum = new _677_MapSum();
        mapSum.insert("aa",3);
        System.out.println(mapSum.sum("a"));
        mapSum.insert("aa",2);
        System.out.println(mapSum.sum("a"));
    }
}
