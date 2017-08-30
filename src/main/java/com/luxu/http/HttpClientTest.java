package com.luxu.http;


import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;


/**
 * Created by xulu on 2017/8/30.
 */
public class HttpClientTest {
    public static void downLoad(String remoteFileName, String localFileName) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet get = new HttpGet(remoteFileName);

        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(get);
            HttpEntity entity = response.getEntity();
            FileUtils.copyInputStreamToFile(entity.getContent(), new File(localFileName));
//            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
//            try {
//                httpclient.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }
    public static void main(String[] args){
        downLoad("https://esisoj70.emea.nsn-net.net:443/artifactory/netact-fast_pass-local/PMB/com.nokia.fcoss/V8.16.2/adaptation_com.nokia.fcoss.pmb-V8.16.2-20151008T131415.zip","hehe/aaa.txt");
        System.out.println("Download done");
        try {
            Thread.sleep(1000000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
