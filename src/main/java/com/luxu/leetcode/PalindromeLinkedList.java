package com.luxu.leetcode;

import com.luxu.leetcode.util.ListNode;

/**
 * Created by xulu on 2017/3/28.
 */
public class PalindromeLinkedList {

//    public boolean isPalindrome2(ListNode head) {
//        if(head == null) {
//            return true;
//        }
//        //1.遍历确定长度
//        int length = 0;
//        ListNode p = head;
//        while(p != null) {
//            System.out.println(head);
//            System.out.println(p);
//            length ++;
//            p = p.next;
//        }
//        p = head;//用完之后, p归位
//        System.out.println(head);
//        System.out.println(p);
//        if(length == 1) {
//            return true;
//        }
//        //2.将后半部分链反转
//        int half = (length + 1) / 2;
//        ListNode q = head;
//        for(int i = 0; i < half; i ++) {
//            q = q.next;
//        }
//        //开始反转
//        ListNode r = q.next;
//        q.next = null;
//        ListNode m;
//        while(r != null) {
//            m = r.next;
//            r.next = q;
//            q = r;
//            r = m;
//        }
//        //3.依次比较,直到其中一个或者两个链遍历完
//        while(q != null && p != null) {
//            if(p.val == q.val) {
//                q = q.next;
//                p = p.next;
//            }else {
//                return false;
//            }
//
//        }
//        return true;
//    }
public boolean isPalindrome(ListNode head) {
    ListNode p =head;
    int length = 0;
    while(p!=null){
        p = p.next;
        length++;
    }
    p = head;
    if(length == 1) {
        return true;
    }
    int half = (length + 1) / 2;
    ListNode q = head;
    for(int i = 0; i < half; i ++) {
        q = q.next;
    }
//    reverse(q);

    q = reverse2(q);
    System.out.println(p);
    System.out.println(q);
    while(p!=null && q!=null){
        if(p.val == q.val){
            p=p.next;
            q=q.next;
        }
        else{
            return false;
        }
    }
    return true;

}

    private ListNode reverse2(ListNode q) {
       ListNode r = q.next;
       q.next = null;
       while(r!=null){
           ListNode m = r.next;
           r.next = q;
           q = r;
           r = m;
       }
       return q;
    }

    public ListNode reverse(ListNode a) {
        ListNode previous = null;
        while (a != null) {
            ListNode nextNode = a.next;
            a.next=previous;
            previous = a;
            a = nextNode;
        }
        return previous;
    }
    public static void main(String[] args){
        ListNode head = new ListNode(1);
        head.next=new ListNode(2);
        head.next.next=new ListNode(1);
        head.next.next.next=new ListNode(1);
        System.out.println(new PalindromeLinkedList().isPalindrome(head));
    }
}
