package com.luxu.design_pattern.adapter;

/**
 * Created by xulu on 2017/9/28.
 */
public class TwoPinSocket {

    public void plug(int pinNumber){
        if(pinNumber==2){
            System.out.println("ok,plugged");
            return;
        }
        throw new RuntimeException("Unsupported pin number");
    }
}
