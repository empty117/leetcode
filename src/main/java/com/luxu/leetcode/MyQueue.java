package com.luxu.leetcode;

import java.util.Stack;

public class MyQueue {
    
    private Stack<Integer> stack;

    /** Initialize your data structure here. */
    public MyQueue() {
        stack = new Stack();
    }
    
    /** Push element x to the back of queue. */
    public void push(int x) {
        int[] tmp = new int[stack.size()];
        for(int i= tmp.length;i>=0;i--){
            tmp[i] = stack.pop();
        }
        stack.push(x);
        for(int item: tmp){
            stack.push(item);
        }
    }
    
    /** Removes the element from in front of queue and returns that element. */
    public int pop() {
        int[] tmp = new int[stack.size()];
        for(int i= tmp.length;i>=0;i--){
            tmp[i] = stack.pop();
        }
        for(int i=1;i<tmp.length;i++){
            stack.push(i);
        }
        return tmp[0];
    }
    
    /** Get the front element. */
    public int peek() {
        int[] tmp = new int[stack.size()];
        for(int i= tmp.length;i>=0;i--){
            tmp[i] = stack.pop();
        }
        for(int item: tmp){
            stack.push(item);
        }
        return tmp[0];
    }
    
    /** Returns whether the queue is empty. */
    public boolean empty() {
        return stack.empty();
    }
    public static void main(String[] args){
        MyQueue obj = new MyQueue();
        obj.push(123);
        int param_2 = obj.pop();
        int param_3 = obj.peek();
        boolean param_4 = obj.empty();
    }
}