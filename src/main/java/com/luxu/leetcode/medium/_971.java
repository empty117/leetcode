package com.luxu.leetcode.medium;

import com.luxu.leetcode.util.TreeNode;

import java.util.*;

/**
 * @author xulu
 * @date 1/9/2019
 * dfs遍历，遍历到每个节点时，尝试check左子节点:
 *  如果左子节点不匹配，则交换后再check
 *  如果交换后仍不匹配则返回-1
 *  否则依次将交换的节点添加进结果
 */
public class _971 {

    private int index = 0; int[] voyage;
    private List<Integer> result = new ArrayList<>();
    public List<Integer> flipMatchVoyage(TreeNode root, int[] voyage) {
        this.voyage = voyage;
        return checkValue(root)?result:Arrays.asList(-1);
    }

    private boolean checkValue(TreeNode t){
        if(t!=null){
            if(t.val != voyage[index]){
                return false;
            }
            index++;
            if(t.left!=null && t.left.val!=voyage[index]){
                result.add(t.val);
                //尝试交换左右
                TreeNode tmp = t.left;
                t.left = t.right;
                t.right = tmp;
            }
            return checkValue(t.left) && checkValue(t.right);
        }
        return true;
    }
}
