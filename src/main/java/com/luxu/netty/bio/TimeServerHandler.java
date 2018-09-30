package com.luxu.netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * Created by xulu on 2017/7/28.
 */
public class TimeServerHandler implements Runnable {
    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader((new InputStreamReader(this.socket.getInputStream())));
            out = new PrintWriter(this.socket.getOutputStream(), true);
//            String currentTime = null;
            String body = null;
            while (true) {
                body = in.readLine();
                System.out.println("The time server receive order: " + body);
//                currentTime = " quert time order".equalsIgnoreCase(body)?new Date()

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
