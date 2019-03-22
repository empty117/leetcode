package com.luxu.test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xulu
 * @date 3/4/2019
 */
public class SmiPerformance {

    private static ExecutorService pool = Executors.newFixedThreadPool(64);
    private static int TIME = 10000000;
    private static int FACTOR = 513;
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    private List<String> list = new LinkedList<>();

    public String checkOut(){
        String s = null;
        synchronized (list){
            if (list.size() > 0) {
                s = list.get(0);
                list.remove(0);
            }
        }
        if(s==null || s.isEmpty()){
            return create();
        }
        return s;
    }

    public String checkOut2(){
        if(threadLocal.get()==null){
            create();
        }
        return threadLocal.get();
    }

    public void checkIn2(String s){
       threadLocal.set(s);
    }




    public void checkIn(String s){
        boolean isCheckedIn=false;
        synchronized(list){
            if (list.size() < 512) { // max pool size is 512
                list.add(s);
                isCheckedIn=true;
            }
        }
    }

//    private static class ThreadLocal {
//        static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();
//
//        static void set(String ne3SRegistrationPort) {
//            THREAD_LOCAL.set(ne3SRegistrationPort);
//        }
//
//        static String get() {
//            return THREAD_LOCAL.get();
//        }
//
//        static void remove(){
//            THREAD_LOCAL.remove();
//        }
//    }

    private String create(){
        return String.valueOf(System.currentTimeMillis()%FACTOR);
    }

    public static void main(String[] args) throws InterruptedException {
        long before = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(TIME);
        for(int i=0;i<TIME;i++){
            pool.submit(new SmiTask(countDownLatch));
        }
        countDownLatch.await();
        System.out.println(System.currentTimeMillis() - before + "ms");
    }
}
