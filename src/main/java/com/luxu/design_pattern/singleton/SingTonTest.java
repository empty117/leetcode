package com.luxu.design_pattern.singleton;

/**
 * @author xulu
 * @date 11/27/2017
 */
public class SingTonTest {


    private static final SingTonTest INSTANCE = new SingTonTest();
    private String a = new String("");
    private SingTonTest(){
        a = "aaa";

    }

    public static SingTonTest getInstance(){
        return INSTANCE;
    }

    public static void main(String[] args){
        SingTonTest.getInstance();
        SingTonTest.getInstance();
        SingTonTest.getInstance();
        System.out.println(SingTonTest.class.getName());
    }

    public String getA() {
        return a;
    }

}
