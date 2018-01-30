package com.luxu.leetcode.util;

/**
 * Created by xulu on 2016/12/23.
 */
public class ListNode {
    public int val;
    public ListNode next;
    public ListNode(int x){
        val = x;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(val+"");
        ListNode node = next;
        while (node!=null){
            sb.append("->");
            sb.append(node.val);
            node = node.next;
        }
        return sb.toString();
    }
}
