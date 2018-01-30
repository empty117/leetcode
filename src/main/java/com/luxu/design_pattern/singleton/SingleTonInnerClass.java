package com.luxu.design_pattern.singleton;

/**
 * Created by xulu on 2017/9/28.
 * static inner class singleton
 */
public class SingleTonInnerClass {
    private static class inner{
        public static final SingleTonInnerClass INSTANCE = new SingleTonInnerClass();
    }
    private SingleTonInnerClass(){}
    public static SingleTonInnerClass getInstance(){
        return inner.INSTANCE;
    }
}
