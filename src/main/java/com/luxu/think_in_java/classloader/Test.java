package com.luxu.think_in_java.classloader;

/**
 * @author xulu
 * @date 1/17/2018
 */
public class Test {
    public static void main(String[] args) throws ClassNotFoundException {
        ClassLoader classLoader = Object.class.getClassLoader();
//        Class clazz = classLoader.loadClass("java.lang.Thread");
        System.out.println(classLoader.toString());
    }
}
