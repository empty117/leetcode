package com.luxu.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * @author xulu
 * @date 9/27/2018
 */
public class MyReactor implements Runnable {

    final Selector selector;
    final ServerSocketChannel serverSocket;

    public MyReactor(int port) throws IOException {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        SelectionKey selectionKey = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        selectionKey.attach(new Acceptor());
    }

    @Override
    public void run() {
        while (!Thread.interrupted()){
            try {
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                selected.stream().forEach( item ->{
                    dispatch(item);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    void dispatch(SelectionKey k){
        Runnable r = (Runnable) k.attachment();
        if(r!=null){
            r.run();
        }
    }
    class Acceptor implements Runnable{

        @Override
        public void run() {
            try {
                SocketChannel c = serverSocket.accept();
                if(c !=null){
                    new Handler(selector, c);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
