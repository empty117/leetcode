package com.luxu.threads;

/**
 * Created by xulu on 2017/8/30.
 */
public class HelloWorld {
    public static void main(String... args){
        System.out.println("Hello, world!");
        try {
            Thread.sleep(10000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
