package com.luxu.leetcode;

import java.util.Arrays;

public class MinStack {

    private Integer[] array;
    private int current_index=0;
    private final int capacityIncrement = 100;
    private int min;

    /** initialize your data structure here. */
    public MinStack() {
        array = new Integer[capacityIncrement];
    }
    
    public void push(int x) {
        array[current_index]=x;
        if(current_index==0){
            min=x;
        }
        else{
            min = Math.min(x,array[current_index-1]);
        }
        current_index++;
        int len = array.length;
        if(current_index>=len){
            array = Arrays.copyOf(array, len+capacityIncrement);
        }
    }
    
    public void pop() {
        array[current_index]=null;
        current_index--;
    }
    
    public int top() {
        if(current_index==0){
            return array[0];
        }
        return array[current_index-1];
    }

    public int getMin() {
        int min_value = array[0];
        for(int i = 1;i<current_index;i++){
//            System.out.println(array[i]);
            min_value = Math.min(min_value,array[i]);
        }
        return min_value;
    }
    public static void main(String[] args){
        MinStack obj = new MinStack();
         obj.push(-2);
        obj.push(0);
        obj.push(-3);
        System.out.println(obj.getMin());
         obj.pop();
        System.out.println(obj.top());
        System.out.println(obj.getMin());
    }
}