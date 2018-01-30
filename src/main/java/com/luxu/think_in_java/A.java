package com.luxu.think_in_java;

/**
 * Created by xulu on 2017/9/29.
 */
public class A {
    private B b;
    public String name = "A:";

    public A(){
        b = new B(this);
    }

    public void test(){
        b.test();
    }
    public static void main(String[] args){
        A a = new A();
        a.test();
    }
}


