package com.luxu.threads;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xulu on 2017/8/30.
 */
public class DataSharing {
    Map<String,String> map = new HashMap();

    boolean tag = false;

    public DataSharing(){
        map = new HashMap();
        map.put("1","test");
    }

    public String get(){
        return map.get("1");
    }
    public  void set(String value){
//        for(int i=0;i<10000;i++){
            map.put("1",value);
//        }
    }

    public void setTag(){
        tag = true;
    }

    public void shutDonw(){
        while (tag){
            System.out.println("shutdonw!!!!!!");
            System.exit(0);
        }
    }


    public static void main(String args[]) throws InterruptedException {
        Thread t = new Thread(new Runnable()
        {
            public void run()
            {
                System.out.println("First task started");
                System.out.println("Sleeping for 2 seconds");
                try
                {
                    Thread.sleep(2000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                System.out.println("First task completed");
            }
        });
        Thread t1 = new Thread(new Runnable()
        {
            public void run()
            {
                System.out.println("Second task completed");
            }
        });
        t.start(); // Line 15
        t.join(); // Line 16
        t1.start();
    }
}
