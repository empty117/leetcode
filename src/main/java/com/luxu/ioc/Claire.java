package com.luxu.ioc;

/**
 * Created by xulu on 2017/8/31.
 */
public class Claire implements Person{

    private String name;
    private int age;

    @Autowired
    private Mouth mouth;

    public Claire(){
        this.age=2;
        this.name="Claire";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public void say() {
        mouth.say();
    }

    @Override
    public void sing() {
        mouth.sing();
    }

    @Override
    public void cry() {
        mouth.cry();
    }
}
