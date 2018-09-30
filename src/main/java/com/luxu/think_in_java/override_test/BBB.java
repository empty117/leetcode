package com.luxu.think_in_java.override_test;

/**
 * @author xulu
 * @date 7/30/2018
 */
public class BBB extends AAA{

    @Override
    public void test(){
        throw new UnsupportedOperationException("test");
    }

    @Override
    public void test(String value){
        throw new UnsupportedOperationException("test :" + value);
    }

    public void help(){
        super.test("help");
    }
    public static void main(String[] args){
        new BBB().help();
    }
}
