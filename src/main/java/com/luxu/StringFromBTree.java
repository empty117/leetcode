package com.luxu;

import com.luxu.leetcode.util.TreeNode;

/**
 * Created by xulu on 2017/8/28.
 */
public class StringFromBTree {

    public String tree2str(TreeNode t) {

        String s = tree2str2(t);
        return s.substring(1,s.length()-1);
    }

    public String tree2str2(TreeNode t) {
        String s = "";
        s+="("+(t==null?"":t.val);
        if(t!=null){
            if(t.left!=null || (t.left==null&&t.right!=null)){
                s += tree2str2(t.left);
            }
            if(t.right!=null){
                s += tree2str2(t.right);
            }

        }
        s +=")";
        return s;
    }

    public static void main(String args[]){
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
//        root.left.left = new TreeNode(7);
        root.left.right = new TreeNode(4);
        root.right = new TreeNode(3);
//        root.right.left = new TreeNode(4);
//        root.right.right = new TreeNode(5);

        StringFromBTree stringFromBTree = new StringFromBTree();
//        treeOrders.preOrder(root);
//        int leftSum = treeOrders.title(root);
        System.out.println(stringFromBTree.tree2str2(root));
    }
}
