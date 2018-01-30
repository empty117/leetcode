package com.luxu.threads;

/**
 * Created by xulu on 2017/10/11.
 */
public class PersonThreadLocal {
    public static final ThreadLocal<Person> PERSON_THREAD_LOCAL = new ThreadLocal<>();
    public static void set(Person person){
        PERSON_THREAD_LOCAL.set(person);
    }
    public static Person get(){
        return PERSON_THREAD_LOCAL.get();
    }
}
