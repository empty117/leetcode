package com.luxu.netty.nio;

import java.io.IOException;

/**
 * @author xulu
 * @date 9/27/2018
 */
public class MyTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        MyReactor myReactor = new MyReactor(9999);
        new Thread(myReactor).start();
        Thread.sleep(11111111111L);
    }
}
