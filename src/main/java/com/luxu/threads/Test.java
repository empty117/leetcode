package com.luxu.threads;

import java.util.concurrent.*;

/**
 * @author xulu
 * @date 10/11/2018
 */
public class Test {

    public static void main(String[] args){
        ExecutorService executor = Executors.newFixedThreadPool(1);
        CompletionService<String> completionService = new ExecutorCompletionService<>
                (executor);
        completionService.submit(new TestTask());
        Future<String> future = null;

        try {
            future = completionService.take();
            String feedback = future.get();
            int exitCode = feedback.hashCode();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            future.cancel(true);
        }

        if (!executor.isShutdown() && !executor.isTerminated()){
            executor.shutdown();
        }

    }

    private static class TestTask implements Callable<String>{

        @Override
        public String call() throws Exception {
//            Thread.sleep(5000L);
            return null;
        }
    }
}
