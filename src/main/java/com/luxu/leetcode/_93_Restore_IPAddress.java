package com.luxu.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xulu on 2017/9/6.
 * Given a string containing only digits, restore it by returning all possible valid IP address combinations.
 * For example:
 * Given "25525511135",
 * return ["255.255.11.135", "255.255.111.35"]. (Order does not matter)
 */
public class _93_Restore_IPAddress {

    public List<String> restoreIpAddresses(String s) {
        List<String> result = new ArrayList();

        restorIp(s,result,0,"",0);

//         s.length() MUST between 4 and 12
//        bruteForce(s, result);
        return result;
    }

    private void bruteForce(String s, List<String> result) {
        int len = s.length();
        for(int i=1;i<4&&i<len-2;i++){
            for(int j=i+1;j<i+4&&j<len-1;j++){
                for(int k=j+1;k<j+4&&k<len;k++){
                    String s1 = s.substring(0,i),s2=s.substring(i,j),s3=s.substring(j,k),s4=s.substring(k,len);
                    if(isValid(s1)&&isValid(s2)&&isValid(s3)&&isValid(s4)){
                        result.add(s1+"."+s2+"."+s3+"."+s4);
                    }
                }
            }
        }
    }

    private void restorIp(String ip, List<String> solutions, int index, String restored, int count){
        if(count>4){
            return;
        }
        if(count == 4 && index == ip.length()){
            solutions.add(restored);
        }
        for(int i=1;i<4;i++){
            if(index+i>ip.length()){
                break;
            }
            String s = ip.substring(index,index+i);
            if(!isValid(s)){
                continue;
            }
            restorIp(ip, solutions, index+i, restored+s+(count==3?"":"."),count+1);
        }
    }

    private boolean isValid(String s){
        if(s.length()>3||s.length()==0||(s.charAt(0)=='0'&&s.length()>1)||Integer.parseInt(s)>255){
            return false;
        }
        return true;
    }

    public static void main(String[] args){
        _93_Restore_IPAddress restore_ipAddress = new _93_Restore_IPAddress();
        System.out.println(restore_ipAddress.restoreIpAddresses("123123123"));
    }
}
