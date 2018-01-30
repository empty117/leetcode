package com.luxu.design_pattern.chain;

/**
 * @author xulu
 * @date 11/16/2017
 */
public abstract class BaseHandler<T>{
    protected BaseHandler successor;

    public BaseHandler getSuccessor() {
        return successor;
    }

    public void setSuccessor(BaseHandler successor) {
        this.successor = successor;
    }

    public abstract void handleRequest(T t);
}
