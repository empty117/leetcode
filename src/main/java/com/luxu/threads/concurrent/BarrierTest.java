package com.luxu.threads.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * @author xulu
 * @date 12/26/2017
 */
public class BarrierTest {
    private static final int N = 10;
    private static final CyclicBarrier barrier = new CyclicBarrier(N);
    static class Worker implements Runnable{

        @Override
        public void run() {
            try {
               work();
               barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("do something else");


        }
        private void work() throws InterruptedException {
            System.out.println("start work");
            Thread.sleep(2000L);
            System.out.println("finish work");
        }
    }

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        for(int i = 0;i<N;i++){
            new Thread(new BarrierTest.Worker()).start();
        }
        System.out.println("do final thing");
    }
}
