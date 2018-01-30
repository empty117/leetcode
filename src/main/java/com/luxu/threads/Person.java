package com.luxu.threads;

/**
 * Created by xulu on 2017/10/11.
 */
public class Person {
    private static final Person person = new Person();
//    private Person(){}
    public static Person getInstance(){
        return person;
    }
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

