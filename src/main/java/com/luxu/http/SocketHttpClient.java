package com.luxu.http;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author xulu
 * @date 1/2/2018
 */
public class SocketHttpClient {
    public static void main(String[] args) throws Exception {
        // 地址localhost，端口号8080
        Socket socket = new Socket("10.92.72.98", 30508);

        // 请求服务器
        OutputStream out = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(out);
        pw.println("GET /swmhttp/SBTS16.10_ENB_A50_DUMMY_PKG.zip HTTP/1.1");  // 请求的第一行Request-Line，需要写请求的URL(/Test/test.jsp)
        pw.println("Host: 10.92.72.98:30508");  // 请求头，Host是必须的
        pw.println();  // 一定要有个空行表示请求结束
        pw.flush();  // 提交请求
        // 获取服务器响应
        InputStream is = socket.getInputStream();
        InputStreamReader reader = new InputStreamReader(is);

        // 输出响应内容
        while (true) {
            System.out.print((char)reader.read());
            Thread.sleep(1000L);
        }
    }
}
