package com.luxu.leetcode;

import com.luxu.leetcode.util.ListNode;

/**
 * Created by xulu on 2016/12/23.
 */
public class AddTwoNumbers {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode r = new ListNode(0);
        ListNode result = r;
        int carry = 0;
        while (l1!=null||l2!=null||carry==1) {
            int i = l1==null?0:l1.val;
            int j = l2==null?0:l2.val;
            result.val = (i + j + carry) % 10;
            carry = (i + j + carry) / 10;
            if(l1!=null){
                l1 = l1.next;
            }
            if(l2!=null){
                l2 = l2.next;
            }
            if(l1!=null||l2!=null||carry==1){
                result.next = new ListNode(0);
                result = result.next;
            }
        }
        return r;
    }
}
