package com.luxu.threads;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xulu
 * @date 12/25/2017
 */
public class MapTest {
    public static Map<String,String> getMap(){
        Map<String,String> map = new HashMap<>();
        map.put("a","aaa");
        return Collections.unmodifiableMap(map);
    }
    public static void main(String[] args){
        Map<String,String> map = getMap();
//        map.put("b","bbb");
        System.out.println(map.getClass().getName());
    }
}
