package com.luxu.threads;

/**
 * Reentrancy avoid deadlock
 * @author xulu
 * @date 12/21/2017
 */
public class DeadLock {
    synchronized void doSomething(){
        System.out.println("test");
    }

    public static class Child extends DeadLock{
        @Override
        synchronized void doSomething(){
            System.out.println("hehe");
            super.doSomething();
        }
    }
    public static void main(String[] args){
        DeadLock deadLock = new Child();
        deadLock.doSomething();
    }
}
