package com.luxu.leetcode.tree;

import com.luxu.leetcode.util.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by xulu on 2017/8/23.
 */
public class TreeOrders {
    /*
    中左右
     */
    public void preOrder(TreeNode t){
        if(t!=null){
            System.out.print(t.val+" ");
            preOrder(t.left);
            preOrder(t.right);
        }
    }

    public void preOrderNoRecursion(TreeNode t){
        LinkedList<TreeNode> stack = new LinkedList();
        while(t!=null || !stack.isEmpty()){
            while(t!=null){
                System.out.print(t.val + " ");
                stack.push(t);
                t = t.left;
            }
            if(!stack.isEmpty()){
                t = stack.pop();
                t = t.right;
            }
        }
    }

    /*
    左右中
     */
    public void postOrder(TreeNode t){
        if(t!=null){
            postOrder(t.left);
            postOrder(t.right);
            System.out.print(t.val+" ");
        }
    }



    public void postOrderNoRecursion(TreeNode t){
        LinkedList<TreeNode> stack = new LinkedList<>();
        LinkedList<TreeNode> temp = new LinkedList<>();
        while(t!=null || !stack.isEmpty()){
            while(t!=null){
                stack.push(t);
                temp.push(t);
                t = t.right;
            }
            if(!stack.isEmpty()){
                t = stack.pop();
                t = t.left;
            }
        }
        temp.stream().forEach(item -> System.out.print(item.val+" "));
    }


    /*
    左中右
     */
    public void midOrder(TreeNode t){
        if(t!=null){
            midOrder(t.left);
            System.out.print(t.val+" ");
            midOrder(t.right);
        }
    }

    public void midOrderNoRecursion(TreeNode t){
        LinkedList<TreeNode> stack = new LinkedList();
        while(t!=null || !stack.isEmpty()){
            while(t!=null){
                stack.push(t);
                t = t.left;
            }
            if(!stack.isEmpty()){
                t = stack.pop();
                System.out.print(t.val + " ");
                t = t.right;
            }
        }
    }

    public void levelOrderNoRecursion(TreeNode t){
        Queue<TreeNode> queue = new LinkedList();
        if(t!=null){
            queue.add(t);
        }
        while (!queue.isEmpty()){
            t = queue.poll();
            if(t!=null){
                System.out.print(t.val+ " ");
                queue.offer(t.left);
                queue.offer(t.right);
            }
        }
    }

    public int getLevel(TreeNode t){
        if(t==null){
            return 0;
        }
        int l = getLevel(t.left);
        int r = getLevel(t.right);
        return l>r?l+1:r+1;
    }

    public static void main(String args[]){
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(0);
//        root.left.left = new TreeNode(7);
        root.left.right = new TreeNode(3);
        root.right = new TreeNode(0);
//        root.right.left = new TreeNode(4);
//        root.right.right = new TreeNode(5);

        TreeOrders treeOrders = new TreeOrders();
        System.out.print("前序:");
        treeOrders.preOrder(root);
        System.out.println();
        System.out.print("后序:");
        treeOrders.postOrder(root);
        System.out.println();
        System.out.print("中序:");
        treeOrders.midOrder(root);
        System.out.println();
        System.out.print("前序:");
        treeOrders.preOrderNoRecursion(root);
        System.out.println();
        System.out.print("后序:");
        treeOrders.postOrderNoRecursion(root);
        System.out.println();
        System.out.print("中序:");
        treeOrders.midOrderNoRecursion(root);
        System.out.println();
        System.out.print("层:");
        treeOrders.levelOrderNoRecursion(root);
        System.out.println();
        System.out.println(treeOrders.getLevel(root));
//        int leftSum = treeOrders.title(root);
//        System.out.println(treeOrders.findTilt(root));
    }
}
