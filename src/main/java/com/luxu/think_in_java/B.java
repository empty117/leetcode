package com.luxu.think_in_java;

/**
 * Created by xulu on 2017/9/29.
 */
public class B {
    private A a;
    public B(A a){
        this.a = a;
    }

    public void test(){
        System.out.println(a.name + "hehe");
    }
}
