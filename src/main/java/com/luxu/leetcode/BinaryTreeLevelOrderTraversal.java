package com.luxu.leetcode;

import com.luxu.leetcode.util.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xulu on 2016/12/23.
 */
public class BinaryTreeLevelOrderTraversal {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        setChildList(result,root,1);
        return result;
    }

    //递归遍历每层,时间复杂度为O(2^n) n=level
    private void setChildList(List<List<Integer>> list, TreeNode node, int level){
        if(node == null){
            return;
        }
        if(list.size()< level){
            list.add(new ArrayList<Integer>());
        }
        //每层的list加上相应node的val
        list.get(level-1).add(node.val);
        setChildList(list,node.left,level+1);
        setChildList(list,node.right,level+1);
    }
}
