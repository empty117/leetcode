package com.luxu.leetcode.tree;

import com.luxu.leetcode.util.TreeNode;

/**
 * Created by xulu on 2017/8/23.
 */
public class TreeOrders {
    int sum = 0;
    public int findTilt(TreeNode root) {
        postOrder(root);
        return sum;
    }

    public void preOrder(TreeNode t){
        if(t!=null){
            System.out.print(t.val+" ");
            preOrder(t.left);
            preOrder(t.right);
        }
    }
    public int postOrder(TreeNode root){
        if(root == null){
            return 0;
        }
        int leftSum = postOrder(root.left);
        int rightSum = postOrder(root.right);
        sum += Math.abs(leftSum - rightSum);
        return root.val + leftSum + rightSum;
    }
    public static void main(String args[]){
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.left.left = new TreeNode(7);
        root.left.right = new TreeNode(6);
        root.right = new TreeNode(3);
//        root.right.left = new TreeNode(4);
//        root.right.right = new TreeNode(5);

        TreeOrders treeOrders = new TreeOrders();
        treeOrders.preOrder(root);
//        int leftSum = treeOrders.title(root);
//        System.out.println(treeOrders.findTilt(root));
    }
}
