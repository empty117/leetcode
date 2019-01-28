package com.luxu.leetcode;

import com.luxu.leetcode.util.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xulu
 * @date 1/4/2019
 */
public class _872 {
    private StringBuilder sb = new StringBuilder();
    public boolean leafSimilar(TreeNode root1, TreeNode root2) {
        addLeaf(root1);
        addLeaf(root2);
        int len = sb.length();
        return sb.substring(0,len/2).equals(sb.substring(len/2,len));
    }

    private void addLeaf(TreeNode node){
        if(node!=null){
            if(node.left ==null && node.right == null){
                sb.append(node.val);
            }
            else{
                addLeaf(node.left);
                addLeaf(node.right);
            }
        }
    }

    public static void main(String[] args){
        StringBuilder sb = new StringBuilder();
        sb.append("123123");
        int len = sb.length();
        System.out.println(sb.substring(0,len/2).equals(sb.substring(len/2,len)));
    }
}
