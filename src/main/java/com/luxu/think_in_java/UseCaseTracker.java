package com.luxu.think_in_java;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xulu on 2017/8/1.
 */
public class UseCaseTracker {
    public static void trackUseCase(List<Integer> useCases, Class<?> cl){
        for(Method m:cl.getDeclaredMethods()){
            UseCase uc = m.getAnnotation(UseCase.class);
            if(uc!=null){
                System.out.println("Found use case:" + uc.id() + " " + uc.description());
                useCases.remove(new Integer(uc.id()));
            }
        }
        for(int i: useCases){
            System.out.println("Warning: Missing use case-" + i);
        }
    }
    public static void main(String[] args){
        List<Integer> useCases = new ArrayList<Integer>();
        Collections.addAll(useCases,47,48,49,50);
        trackUseCase(useCases,PasswordUtils.class);
    }
}
