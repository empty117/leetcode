package com.luxu.design_pattern.adapter;

/**
 * Created by xulu on 2017/9/28.
 */
public class Adapter {
    private TwoPinSocket twoPinSocket;

    public Adapter(){
        if(twoPinSocket == null){
            this.twoPinSocket = new TwoPinSocket();
        }
    }

    public void plug(Object any){
        if(any!=null){
            twoPinSocket.plug(2);
        }
    }
}
