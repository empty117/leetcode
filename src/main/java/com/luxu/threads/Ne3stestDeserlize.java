package com.luxu.threads;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xulu
 * @date 12/23/2017
 */
public class Ne3stestDeserlize {
    public static int readUnsignedInt(ObjectInput in) throws IOException {
        byte b = in.readByte();
        int i = b & 127;

        for(int shift = 7; (b & 128) != 0; shift += 7) {
            b = in.readByte();
            i = (int)((long)i | ((long)b & 127L) << shift);
        }

        return i;
    }
    public static void main(String[] args){
        Map<String,Object> info = new HashMap<>();
        try
        {
            FileInputStream fileIn = new FileInputStream("employee.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
//            info = (HashMap) in.readObject();

            int size = readUnsignedInt(in);

            for(int i = 0; i < size; ++i) {
                info.put((String) in.readObject(), in.readObject());
            }
            in.close();
            fileIn.close();
        }catch(IOException i)
        {
            i.printStackTrace();
            return;
        }catch(ClassNotFoundException c)
        {
            System.out.println("Employee class not found");
            c.printStackTrace();
            return;
        }
        System.out.println("Deserialized Employee...");
        System.out.println("size: " + info.size());
        System.out.println("info: " + info);
    }
}
