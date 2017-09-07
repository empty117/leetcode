package com.luxu.ioc;

/**
 * Created by xulu on 2017/8/31.
 */
public interface Container {

    <T>T getBean(Class<T> clazz);

    Object registerBean(Class<?> clazz) throws IllegalAccessException, InstantiationException;

    void initWired();
}
