package com.luxu.codinggame;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xulu
 * @date 10/12/2018
 */
public class Test {
    public static void main(String[] args) {
        Product product = new Product();
        List<Product> list = new ArrayList<>();
        list.add(product);
        product.decrease();
        product.decrease();
//        System.out.println(list.get(product).getNumber());
    }

    private static class Product {
        private int number = 10;

        public int getNumber() {
            return number;
        }

        public void decrease() {
            if (number > 0) {
                number--;
            }
        }
    }
}
