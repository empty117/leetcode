package com.luxu.leetcode;

/**
 * Created by xulu on 2017/8/29.
 */
public class ComplexNumMultiplication {
    public String complexNumberMultiply(String a, String b) {
        int r1 = getReal(a);
        int r2 = getReal(b);
        int v1 = getVirtual(a);
        int v2 = getVirtual(b);

        int pow_i = -1;

        int real = r1*r2 + v1*v2*pow_i;
        int virtual = r1*v2+r2*v1;
        return real+"+"+virtual+"i";
    }

    private int getReal(String s){
        return Integer.parseInt(s.split("\\+")[0]);
    }

    private int getVirtual(String s){
        String tmp = s.split("\\+")[1];
        return Integer.parseInt(tmp.substring(0,tmp.length()-1));
    }

    public static void main(String[] args){
        ComplexNumMultiplication complexNumMultiplication = new ComplexNumMultiplication();
        String s = complexNumMultiplication.complexNumberMultiply("1+-1i","0+0i");
        System.out.println(s);
    }
}
