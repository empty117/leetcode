package com.luxu.leetcode;

import com.luxu.leetcode.util.ListNode;

/**
 * Created by xulu on 2017/10/9.
 */
public class ReverseList {

    public ListNode reverse(ListNode head){
        if(head==null || head.next==null){
            return head;
        }
        ListNode re = reverse(head.next);
        head.next.next = head;
        head.next = null;
        return re;
    }
    public static void main(String[] args){
        ReverseList reverseList = new ReverseList();
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        ListNode re = reverseList.reverse(head);
        System.out.print(head);
    }
}
