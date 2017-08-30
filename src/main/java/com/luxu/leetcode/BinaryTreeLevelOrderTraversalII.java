package com.luxu.leetcode;

import com.luxu.leetcode.util.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xulu on 2016/12/23.
 */
public class BinaryTreeLevelOrderTraversalII {
    public List<List<Integer>> levelOrderBottom(TreeNode root) {

        //LinkedList 中间插入效率更高
        List<List<Integer>> result = new LinkedList<List<Integer>>();
        setChildList(result,root,1);
        return result;
    }

    //递归遍历每层
    private void setChildList(List<List<Integer>> list, TreeNode node, int level){
        if(node == null){
            return;
        }
        if(list.size()< level){
            //LikedList在链表首插入元素
            list.add(0,new LinkedList<Integer>());
        }
        //LikedList在链表某个位置插入元素
        list.get(list.size()-level).add(node.val);
        setChildList(list,node.left,level+1);
        setChildList(list,node.right,level+1);
    }
}
