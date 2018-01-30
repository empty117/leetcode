package com.luxu.threads;

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

    public static void main(String[] args){
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
                    int mapSize = info.size();
                    System.out.println("to be write:" +mapSize);
                    writeUnsignedInt(out, mapSize);
                    try {
                        Thread.sleep(50L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(mapSize != 0) {
                        Iterator i$ = info.entrySet().iterator();

                        while(i$.hasNext()) {
                            Map.Entry<?, ?> me = (Map.Entry)i$.next();
                            System.out.println(me.getValue());
                            out.writeObject(me.getKey());
                            out.writeObject(me.getValue());
                        }
                        System.out.println(info.size());
                    }
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
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for(int i=11;i<50;i++){
                    System.out.println("add############### " + i);
//                    info.put("key"+i,"value"+i);
                    info.remove("key"+i);
                }
            }
        });
    }
}
