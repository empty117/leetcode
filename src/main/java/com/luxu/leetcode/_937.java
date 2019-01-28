package com.luxu.leetcode;

import java.util.*;

/**
 * @author xulu
 * @date 1/25/2019
 */
public class _937 {
    public String[] reorderLogFiles(String[] logs) {
//        List<String[]> strList = new ArrayList<>();
        Map<String,String> map = new HashMap<>();
        List<String> digList = new ArrayList<>();
        for(String s: logs){
            String removedStr = removeTag(s);
            if(!isDiginal(removedStr)){
                map.put(removedStr, s);
            }
            else{
                digList.add(s);
            }
        }
        System.out.println(digList);

        Set<String> set = map.keySet();
        List<String> strList = new ArrayList<>(set);
        Collections.sort(strList);
        List<String> result = new ArrayList<>();
        for(String s : strList){
            result.add(map.get(s));
        }
        result.addAll(digList);
        String[] array = new String[result.size()];
        String[] s=result.toArray(array);
        return s;
    }

    private String removeTag(String s){
        String tag = s.split(" ")[0];
        return s.replaceFirst(tag+" ","");
    }

    private boolean isDiginal(String s){
        for(char c: s.toCharArray()){
            if(Character.isSpaceChar(c)){
                continue;
            }
            if(!Character.isDigit(c)){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args){
        _937 test = new _937();
        String[] logs = {"a1 9 2 3 1","g1 act car","zo4 4 7","ab1 off key dog","a8 act zoo"};
        String[] result = test.reorderLogFiles(logs);
        System.out.println(Arrays.toString(result));
    }
}
