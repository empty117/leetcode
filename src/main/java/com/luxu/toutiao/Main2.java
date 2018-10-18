package com.luxu.toutiao;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xulu
 * @date 10/15/2018
 */
public class Main2 {

    public static void main(String[] args){
        int k = 9;
        double biggest = 1.1d;
        int[] num = {1,2,3,5,7,11,13,17};
        List<Type> list = new ArrayList<>();
        int len = num.length;
        int round = Math.min(len,k);
        for(int i=0;i<round;i++){
            for(int j = len-1,r=0;j>i && r<round-i;j--,r++){
                double rr = (double) num[i]/num[j];
                if(rr<biggest){
                    list.add(new Type(num[i],num[j], rr));
                    if(r == round - i -1 || j == i+1){
                        biggest = rr;
                    }
                }
            }
        }
        list.sort(Type::compareTo);
        System.out.println(list);
        System.out.println(list.get(k-1));
        System.out.println(biggest);
    }
    static class Type implements Comparable<Type>{
        Type(int s ,int b, double result){
            small = s;
            big = b;
            this.result = result;
        }
        double result;
        int small;
        int big;

        public Double getResult() {
            return result;
        }

        @Override
        public int compareTo(Type o) {
            return getResult().compareTo(o.getResult());
        }

        @Override
        public String toString(){
            return small + " " + big + " " + result;
        }
    }
}
