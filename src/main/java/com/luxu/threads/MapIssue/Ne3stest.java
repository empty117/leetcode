package com.luxu.threads.MapIssue;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xulu
 * @date 12/23/2017
 */
public class Ne3stest {
    public static Map<String,Object> info;
    private static final ExecutorService pool = Executors.newFixedThreadPool(5);

    static{
        info = new HashMap<>();
        for(int i=1;i<60;i++){
            info.put("key"+i,"value"+i);
        }

    }

    public static void writeUnsignedInt(ObjectOutput out, int i) throws IOException {
        while((i & -128) != 0) {
            out.writeByte((byte)(i & 127 | 128));
            i >>>= 7;
        }

        out.writeByte((byte)i);
    }

    public Map read(){
        final Map<String,Object> readInfo = info;
        return readInfo;
    }

    public void write(Map newInfo){
        info = newInfo;
    }

    public static void marshallMap(Map<?, ?> map, ObjectOutput out) throws IOException {
        int mapSize = map.size();
        writeUnsignedInt(out, mapSize);
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(mapSize != 0) {
            Iterator i$ = map.entrySet().iterator();

            while(i$.hasNext()) {
                Map.Entry<?, ?> me = (Map.Entry)i$.next();
                out.writeObject(me.getKey());
                out.writeObject(me.getValue());
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {
        Ne3stest ne3stest = new Ne3stest();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                Map<String,Object> info = ne3stest.read();
                FileOutputStream fileOut =
                        null;
                try {
                    fileOut = new FileOutputStream("employee.ser");
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    System.out.println("!!!!!!!!!!!!!!");
//                    out.writeObject(info);
                    marshallMap(info,out);
                    out.close();
                    fileOut.close();
                    System.out.printf("Serialized data is saved in employee.ser");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        for(int k=0;k<5;k++){
            pool.execute(new Runnable() {
                @Override
                public void run() {
//                try {
//                    Thread.sleep(50L);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                    for(int i=0;i<50;i++){
                        System.out.println("add############### " + i);
//                        info.put("key"+i,"value"+i);
                                            info.remove("key"+i);
                    }
                }
            });
        }

        Thread.sleep(2000L);
        System.out.println(info.size());
        System.out.println(info);
        pool.shutdown();
    }
}
