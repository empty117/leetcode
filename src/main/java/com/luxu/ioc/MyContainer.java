package com.luxu.ioc;

import sun.reflect.misc.ReflectUtil;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xulu on 2017/8/31.
 */
public class MyContainer implements Container {
    private Map<String, Object> beans;
    private Map<String,String> beanKeys;

    public MyContainer(){
        this.beans = new ConcurrentHashMap();
        this.beanKeys = new ConcurrentHashMap();
    }

    public <T> T getBean(Class<T> clazz) {
        String name = clazz.getName();
        Object object = beans.get(name);
        return object==null?null:(T)object;
    }

    public Object registerBean(Class<?> clazz) throws IllegalAccessException, InstantiationException {
        String name = clazz.getName();
        beanKeys.put(name,name);
        Object bean = ReflectUtil.newInstance(clazz);
        beans.put(name,bean);
//        Field[] fields = bean.getClass().getDeclaredFields();
//        for(Field field: fields) {
//            Autowired autowired = field.getAnnotation(Autowired.class);
//            if(autowired!=null){
//                registerBean(field.getType());
//            }
//        }
        return bean;
    }

    public void initWired() {
        beans.forEach((k,v) -> {
            injection(v);
        });
    }

    private void injection(Object object){
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field: fields){
            Autowired autowired = field.getAnnotation(Autowired.class);
            if(autowired!=null){
//                System.out.println(field.getName());
                Object autoWiredField = null;
                String name = autowired.name();
//                if(!name.isEmpty()){
                    String className = beanKeys.get(name);
                    if(className!=null&&!className.isEmpty()){
                        autoWiredField = beans.get(className);
                    }
                    if(null==autoWiredField){
                        try {
                            autoWiredField = recursiveAssembly(field.getType());
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                if (null == autoWiredField) {
                    throw new RuntimeException("Unable to load " + field.getType().getCanonicalName());
                }
//                }
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                try {
                    field.set(object, autoWiredField);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                field.setAccessible(accessible);
            }

        }
    }
    private Object recursiveAssembly(Class<?> clazz) throws InstantiationException, IllegalAccessException {
        if (null != clazz) {
            Object bean = registerBean(clazz);
            injection(bean);
            return bean;
        }
        return null;
    }
}
