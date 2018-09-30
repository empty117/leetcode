package com.luxu.lambda;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author xulu
 * @date 4/27/2018
 */
public class Test {
    public static void main(String[] args){
        Supplier<String> stringSupplier = String::new;
        Function<Integer,Integer> function1 = (x)-> Math.abs(x);

        Function<Integer,Integer> function11 = Math::abs;

        Function<String,String> function2 = String::valueOf;


        Function<String,Supplier<Computer>> consumer1 = (x) -> {
            return Dell::new;
        };

        Consumer<String> consumer2 = (x) -> {
            System.out.println(" after consumer 1");
        };
        int score = consumer1.apply("a").get().doPerformanceTestAndGetScore();
        System.out.println(score);
    }
}
