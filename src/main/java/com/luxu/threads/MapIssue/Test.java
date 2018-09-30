package com.luxu.threads.MapIssue;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Test {
    public static void main(String... args) throws InterruptedException {
        final Random random = new Random();
        final int max = 8;
        for (int j = 0; j < 100000; j++) {
            System.out.println(j);
            // final Map<String, Integer> map = Collections.synchronizedMap(new HashMap<String, Integer>());
            final Map<String, Integer> map = new HashMap<String, Integer>();
            Thread t = new Thread() {
                public void run() {
                    for (int i = 0; i < 100; i++) {
                        incrementCountInMap(map, random.nextInt(max));
                    }
                }
            };
            t.start();
            for (int i = 0; i < 100; i++) {
                incrementCountInMap(map, random.nextInt(max));
            }
            t.join();
            if (map.size() != max) {
                System.out.println("size: " + map.size() + " entries: " + map);
                FileOutputStream fileOut =
                        null;
                try {
                    fileOut = new FileOutputStream("employee.ser");
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    System.out.println("!!!!!!!!!!!!!!");
//                    out.writeObject(info);
                    Ne3stest.marshallMap(map,out);
                    out.close();
                    fileOut.close();
                    System.out.printf("Serialized data is saved in employee.ser");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }
    static void incrementCountInMap(Map<String, Integer> map, int id) {
        String key = "k" + id;
        synchronized (map) {
                    if (map.get(key) == null) {
            map.put(key, 0);
        }
        }
//        if (map.get(key) == null) {
//            map.put(key, 0);
//        }
//        synchronized (map) {
//            map.put(key, map.get(key).intValue() + 1);
        map.put("k1",12345);
//        }
    }

}