package com.luxu.leetcode.medium;

import com.luxu.leetcode.util.TreeNode;

/**
 * @author xulu
 * @date 3/14/2019
 */
public class _979 {

    private int sum;
    public int distributeCoins(TreeNode root) {
        dfs(root);
        return sum;
    }

    private int dfs(TreeNode t){
        if(t==null){
            return 0;
        }
        int L = dfs(t.left);
        int R = dfs(t.right);
        sum+= Math.abs(L);
        sum+= Math.abs(R);
        return L+R+t.val-1;
    }

}
