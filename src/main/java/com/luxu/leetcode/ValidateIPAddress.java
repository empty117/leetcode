package com.luxu.leetcode;

/**
 * Created by xulu on 2017/8/25.
 */
public class ValidateIPAddress {
    private static final String IPV4 = "IPv4";
    private static final String IPV6 = "IPv6";
    private static final String NEITHER = "Neither";

    public String validIPAddress(String IP) {
        if(isIPv4(IP)){
            return "IPv4";
        }
        if(isIPv6(IP)){
            return "IPv6";
        }
        return "Neither";
    }
    public int compareVersion(String version1, String version2) {
        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");
        int len = Math.min(v1.length,v2.length);
        for(int i=0;i<len;i++){
            int gap = Integer.parseInt(v1[i]) - Integer.parseInt(v2[i]);
            if(gap > 0){
                return 1;
            }
            if(gap < 0){
                return -1;
            }
        }
        if(v1.length>v2.length){
            for(int i=len;i<v1.length;i++){
                if(Integer.parseInt(v1[i])!=0){
                    return 1;
                }
            }
        }
        if(v1.length<v2.length){
            for(int i=len;i<v2.length;i++){
                if(Integer.parseInt(v2[i])!=0){
                    return -1;
                }
            }
        }
        return 0;
    }

    private boolean isIPv4(String ip){
        String[] items = ip.split("\\.");
        if(items.length!=4 || ip.endsWith(".")){
            return false;
        }
        else{
            for(String item : items){
                try{
                    int i = Integer.parseInt(item);
                    if(i<0||i>255){
                        return false;
                    }
                    if(item.startsWith("0")&&item.length()>1){
                        return false;
                    }
                    if(item.startsWith("-")){
                        return false;
                    }
                }
                catch (RuntimeException e){
                    return false;
                }

            }
            return true;
        }
    }

    private boolean isIPv6(String ip){
        String[] items = ip.split(":");
        if(items.length!=8 || ip.endsWith(":")){
            return false;
        }
        for(String item : items){
            try{
                if(item.length()>4){
                    return false;
                }
                int i = Integer.parseInt(item,16);
            }
            catch (RuntimeException e){
                return false;
            }

        }
        return true;
    }

    public static void main(String[] args){
        ValidateIPAddress validateIPAddress = new ValidateIPAddress();
        System.out.println(validateIPAddress.isIPv6("2001:0db8:85a3:0:0:8A2E:0370:7334:"));
        System.out.println(validateIPAddress.isIPv4("192.168.1.1............"));
    }

}
