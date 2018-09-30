package com.luxu.leetcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xulu
 * @date 7/12/2018
 * not done
 */
public class _638_Solution {
    private static final String PREFIX = "P_";
    public int shoppingOffers(List<Integer> price, List<List<Integer>> special, List<Integer> needs) {
        Map<String,Integer> result = new HashMap<>();
        needs.stream().forEach( item ->{
            result.put(PREFIX+needs.indexOf(item), item);
        });
        Map<String,Integer> priceMap = new HashMap<>();
        price.stream().forEach(item->{
            priceMap.put(PREFIX+price.indexOf(item),item);
        });
        Map<String,Item> content = new HashMap<>();
        special.stream().forEach(item->{
            int _price = item.get(item.size()-1);
            for(int i=0;i<item.size()-1;i++){
                Item item1 = new Item();
                item1.setName(PREFIX+i);
                item1.setPrice(_price);
                item1.setNumber(item.get(i));
            }
        });
        return 0;
    }
    private static final class Item{
        private String name;
        private int price;
        private int number;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }
}
