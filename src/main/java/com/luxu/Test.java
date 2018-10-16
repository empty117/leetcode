package com.luxu;

import java.util.*;

/**
 * Created by Administrator on 2018-10-16.
 */
public class Test {

    public static void main(String[] args){
        MyTest[] tests = {
                new MyTest(1),new MyTest(3),new MyTest(2)
        };
        Arrays.sort(tests);
        for(MyTest myTest:tests){
            System.out.println(myTest);
        }
        Map<String,Integer> map = new HashMap<>();
        map.put("a",2);
        map.put("b",1);
        map.put("c",3);
        List<Map.Entry<String, Integer>> list=new ArrayList<>();
        list.addAll(map.entrySet());
        ValueComparator vc=new ValueComparator();
        Collections.sort(list,vc);
        for(Iterator<Map.Entry<String,Integer>> it=list.iterator();it.hasNext();)
        {
            System.out.println(it.next());
        }

    }

    private static class ValueComparator implements Comparator<Map.Entry<String,Integer>>
    {
        public int compare(Map.Entry<String, Integer> m, Map.Entry<String, Integer> n)
        {
            return m.getValue().compareTo(n.getValue());
        }
    }
    static class MyTest implements Comparable<MyTest>{
        int value;
        MyTest(int value){
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }

        @Override
        public String toString(){
            return "My test:" + value;
        }

        @Override
        public int compareTo(MyTest o) {
            return getValue().compareTo(o.getValue());
        }
    }
}
