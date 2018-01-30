package com.luxu.design_pattern.singleton;

/**
 * Created by xulu on 2017/9/28.
 * Double check
 */
public class SingleTonDoubleCheck {
    private static volatile SingleTonDoubleCheck INSTANCE;

    private SingleTonDoubleCheck(){
    }

    public static SingleTonDoubleCheck getINSTANCE(){
        if(INSTANCE ==null){
            synchronized(SingleTonDoubleCheck.class){
                if (INSTANCE ==null){
                    INSTANCE = new SingleTonDoubleCheck();
                }
            }
        }
        return INSTANCE;
    }
}
