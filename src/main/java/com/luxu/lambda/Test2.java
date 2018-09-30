package com.luxu.lambda;


import java.util.concurrent.*;

import static java.lang.System.out;

/**
 * @author xulu
 * @date 4/27/2018
 */
public class Test2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        Future<String> future = executorService.submit(()->"aaa");

        Future<String> future1 = executorService.submit("aaa"::toString);
        out.println(future1.get());
    }
}
