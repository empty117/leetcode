package com.luxu.threads;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author xulu
 * @date 12/12/2017
 */
public class FileTest {
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(100);

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            EXECUTOR.submit(new Runnable() {
                @Override
                public void run() {
                    File file = new File("test.txt");
                    if (!file.exists()) {
                        synchronized (FileTest.class) {
                            if (!file.exists()) {
                                String cmd = "cmd /c echo a>test.txt";
                                Process exec = null;

                                try {
                                    exec = Runtime.getRuntime().exec(cmd);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    System.out.println(e);
                                }
                                try {
                                    exec.waitFor();
                                } catch (InterruptedException e) {
                                    System.out.println(e);
                                }
                                System.out.println("create file");
                            }
                        }
                        System.out.println("hahaha");
                    } else {
                        System.out.println("file already existing");
                    }
                }
            });
        }
        EXECUTOR.shutdown();
    }
}
