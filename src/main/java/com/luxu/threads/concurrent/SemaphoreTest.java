package com.luxu.threads.concurrent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author xulu
 * @date 12/26/2017
 */
public class SemaphoreTest {
    private final Set<String> set;
    private final Semaphore semaphore;

    public SemaphoreTest(int bound){
        set = Collections.synchronizedSet(new HashSet<>());
        semaphore = new Semaphore(bound);
    }

    public boolean add(String s) throws InterruptedException {
        semaphore.acquire();
        boolean wasAdded = false;
        try{
            wasAdded = set.add(s);
            return wasAdded;
        }
        finally {
            if(!wasAdded){
                semaphore.release();
            }
        }
    }
    public boolean remove(String s){
        boolean wasRemoved = set.remove(s);
        if(wasRemoved){
            semaphore.release();
        }
        return wasRemoved;
    }

    public boolean contains(String s){
        return set.contains(s);
    }
    public static void main(String[] args) throws InterruptedException {
        SemaphoreTest semaphoreTest = new SemaphoreTest(5);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i = 0;i<20;i++){
            int finalI = i;
            executorService.submit(()->{
                try {
                    semaphoreTest.add("a" + finalI);
                    System.out.println(finalI + " added");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        Thread.sleep(2000L);
        for(int i = 0;i<20;i++){
            int finalI = i;
            executorService.submit(()->{
                if(semaphoreTest.contains("a" + finalI)) {
                    semaphoreTest.remove("a" + finalI);
                    System.out.println(finalI + " removed");
                }
            });
        }
    }

}
