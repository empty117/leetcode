package com.luxu.ioc;

/**
 * Created by xulu on 2017/8/31.
 */
public class IoCTest {
    private static Container container = new MyContainer();

    @Autowired
    private Claire claire;

    private void test(){
        claire.say();
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        container.registerBean(IoCTest.class);
        container.initWired();
        IoCTest ioCTest = container.getBean(IoCTest.class);
        ioCTest.test();
//        Mouth mouth = container.getBean(Mouth.class);
//        mouth.cry();
    }
}
