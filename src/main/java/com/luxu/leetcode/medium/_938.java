package com.luxu.leetcode.medium;

import com.luxu.leetcode.util.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xulu
 * @date 1/25/2019
 */
public class _938 {

    private List<Integer> list = new ArrayList<>();

    public int rangeSumBST(TreeNode root, int L, int R) {
        midOrder(root);
        int sum = 0;
        boolean ongoing = false;
        System.out.println(list);
        for(int i=0;i<list.size();i++){
            int item = list.get(i);
//            System.out.println(sum);
            if(item == L){
                sum+=item;
                ongoing = true;
                continue;
            }
            if(ongoing){
                sum+=item;
                continue;
            }
            if(item == R){
                sum+=item;
                break;
            }
        }
        return sum;
    }

    public void midOrder(TreeNode t){
        if(t!=null){
            midOrder(t.left);
            list.add(t.val);
            midOrder(t.right);
        }
    }
}
