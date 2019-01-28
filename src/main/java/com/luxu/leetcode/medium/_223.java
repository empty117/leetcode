package com.luxu.leetcode.medium;

/**
 * @author xulu
 * @date 12/27/2018
 */
public class _223 {

    public int computeArea(int A, int B, int C, int D, int E, int F, int G, int H) {
        int a,b;

        int x_tmp = Math.max(A,E);
        int y_tmp = Math.max(B,F);

        if(C<=E || G<=A){
            a = 0;
        }
        else{
            a = Math.min(C,G);
            a = Math.abs(a - x_tmp);
        }

        if(D<=F || H<=B){
            b = 0;
        }
        else{
            b = Math.min(D,H);
            b = Math.abs(b - y_tmp);
        }


        int first = computeArea(Math.abs(A-C),Math.abs(B-D));

        int second = computeArea(Math.abs(E-G),Math.abs(F-H));

        return first + second - computeArea(a,b);


    }

    private int computeArea(int a, int b){
        return a*b;
    }
}
