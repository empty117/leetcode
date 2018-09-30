package com.luxu.classloader;

import com.luxu.codility.Task1;
import com.sun.nio.zipfs.ZipInfo;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xulu
 * @date 2/23/2018
 */
public class Test {
//    private static final E e = new E();
//    static{
//        if(true){
//            throw new IllegalStateException("ClassA.static{}: Internal Error!");
//        }
//    }
    public static void haha(){

    }
    public static void main(String[] args){
        System.out.println(Map.class.isAssignableFrom(Map.class));
        System.out.println(Map.class.isAssignableFrom(HashMap.class));
    }
}
