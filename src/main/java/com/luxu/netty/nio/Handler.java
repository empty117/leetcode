package com.luxu.netty.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author xulu
 * @date 9/27/2018
 */
public final class Handler implements Runnable {
    final SocketChannel socket;
    final SelectionKey sk;
    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer out = ByteBuffer.allocate(1024);
    static final int READING = 0, SENDING = 1;
    int state = READING;

    Handler(Selector sel, SocketChannel c) throws IOException {
        socket =c;
        c.configureBlocking(false);
        sk = socket.register(sel,0);
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);
        sel.wakeup();
    }

    boolean inputIsComplete(){
        return true;
    }

    boolean outputIsComplete(){
        return true;
    }

    @Override
    public void run() {
        try {
            if (state == READING) {
                read();

            } else if (state == SENDING) {
                send();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void process(){}

    void read() throws IOException {
        int i = socket.read(input);
        while (i>0){
            input.flip();
            byte[] data = input.array();
            String info = new String(data).trim();
            System.out.println("从客户端发送过来的消息是："+info);
            input.clear();
            i = socket.read(input);
        }
        if (i==-1){
            socket.close();
        }
        if(inputIsComplete()){
            process();
            state = SENDING;
            sk.interestOps(SelectionKey.OP_WRITE);
        }
    }

    void send() throws IOException {
        socket.write(out);
        if(outputIsComplete()){
            sk.cancel();
        }
    }
}
