package com.luxu.threads.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * @author xulu
 * @date 12/26/2017
 */
public class LatchTest {
    private static final int N = 10;
    static class Worker implements Runnable{

        private final CountDownLatch startLatch;
        private final CountDownLatch endlatch;

        Worker(CountDownLatch startLatch, CountDownLatch endlatch){
            this.startLatch = startLatch;
            this.endlatch = endlatch;
        }
        @Override
        public void run() {
            try {
                startLatch.await();
                work();
                endlatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
        private void work() throws InterruptedException {
            System.out.println("start work");
            Thread.sleep(2000L);
            System.out.println("finish work");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endlatch = new CountDownLatch(N);
        for(int i = 0;i<N;i++){
            new Thread(new Worker(startLatch,endlatch)).start();
        }
        startLatch.countDown();
        System.out.println("do something else");
        endlatch.await();
        System.out.println("do final thing");
    }
}
