package com.luxu.codility;

public class CommonDivisor{
    public static void main(String args[])
    {
        LCM(24,32);
    }
    static int commonDivisor(int M, int N)
    {
        if(N<0||M<0)
        {
            System.out.println("ERROR!");
            return -1;
        }
        if(N==0)
        {
            System.out.println("the biggest common divisor is :"+M);
            return M;
        }
        return commonDivisor(N,M%N);
    }

    static int LCM(int a, int b) {
        int r =  a * b/ commonDivisor(a,b);
        System.out.println(r);
        return r;
    }
}