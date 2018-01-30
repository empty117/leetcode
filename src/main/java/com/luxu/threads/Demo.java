package com.luxu.threads;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xulu on 2017/10/11.
 */
public class Demo implements Runnable{
    private static final ExecutorService pool = Executors.newFixedThreadPool(10);
    public static void main(String[] args){
        for(int i = 0;i<50;i++){
            pool.submit(new Demo());
        }
        pool.shutdown();
    }

    @Override
    public void run() {
//        Person person = Person.getInstance();
        Person person = getPerson();
//        PersonThreadLocal.set(person);
//        System.out.println(Thread.currentThread().getName() + "  aaaaaaaaaaaaaaa  " +PersonThreadLocal.get().getId());
//        System.out.println(Thread.currentThread().getName() + "  bbbbbbbbbbbbbbb  " +person.getId());
        try {
            long age = new Random().nextInt(1000);
            Thread.sleep(age);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println(Thread.currentThread().getName() + "  AAAAAAAAAAAAAAA  " +PersonThreadLocal.get().getId());
        System.out.println(Thread.currentThread().getName() + "  BBBBBBBBBBBBBBB  " +person.getId()+"***"+person);

    }

    private Person getPerson(){
        Person person = PersonThreadLocal.get();
        if(person==null || !person.getId().equals(Thread.currentThread().getName())){
            person = new Person();
            person.setId(Thread.currentThread().getName());
            PersonThreadLocal.set(person);
        }
        return person;
//        return new Person();
    }
}
