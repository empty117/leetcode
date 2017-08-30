//package com.luxu.netty.bio;
//
//import java.net.ServerSocket;
//import java.net.Socket;
//
///**
// * Created by xulu on 2017/7/28.
// */
//public class TimeServer {
//    public static void main(String[] args){
//        int port =8080;
//        if(args!=null&&args.length>0){
//            try{
//                port =Integer.valueOf(args[0]);
//            }catch (NumberFormatException e){
//
//            }
//        }
//        ServerSocket serverSocket = null;
//        serverSocket = new ServerSocket(port);
//        System.out.println("The time server is started in port:" + port);
//        Socket socket = null;
//        while (true){
//            socket = serverSocket.accept();
//            new Thread(new TimeServerHandler(socket)).start();
//
//        }
//    }}
