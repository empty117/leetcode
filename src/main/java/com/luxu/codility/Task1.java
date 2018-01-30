package com.luxu.codility;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by xulu on 2017/9/27.
 */
public class Task1 {
    public int solution(int N){
        int a=-1,b=-1;
        while (N>1){
            int x = (int) Math.floor(Math.log(N)/Math.log(2));
            if(a==-1){
                a=x;
            }
            else{
                if(b==-1){
                    a=a-x-1;
                    b=0;
                    continue;
                }
                if(a-x-1>a){
                    a= a-x-1;
                }
            }
            N = N - (2<<(x-1));
        }
        return a;
    }
    public static void main(String[] args){
        Task1 task1 = new Task1();
//        int x = (int) Math.floor(Math.log(1041)/Math.log(2));
        System.out.println(task1.solution(101));
    }
}
