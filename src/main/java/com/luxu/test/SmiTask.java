package com.luxu.test;

import java.util.concurrent.CountDownLatch;

/**
 * @author xulu
 * @date 3/4/2019
 */
public class SmiTask implements Runnable {

    private SmiPerformance smiPerformance;
    private CountDownLatch countDownLatch;

    public SmiTask(CountDownLatch countDownLatch){
        this.smiPerformance = new SmiPerformance();
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        String s = smiPerformance.checkOut();
        try{
//            Thread.sleep(50L);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            smiPerformance.checkIn(s);
            countDownLatch.countDown();
        }

    }
}
