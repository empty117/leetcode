package com.luxu.leetcode.hard;

import java.util.*;

/**
 * @author xulu
 * @date 12/27/2018
 */
public class _850 {

    public int rectangleArea(int[][] rectangles) {
        int OPEN = 1, CLOSE = -1;
        int[][] events = new int[rectangles.length * 2][];
        Set<Integer> Xvals = new HashSet();
        int t = 0;
        for (int[] rec: rectangles) {
            events[t++] = new int[]{rec[1], OPEN, rec[0], rec[2]};
            events[t++] = new int[]{rec[3], CLOSE, rec[0], rec[2]};
            Xvals.add(rec[0]);
            Xvals.add(rec[2]);
        }

        Arrays.sort(events, (a, b) -> Integer.compare(a[0], b[0]));

        Integer[] X = Xvals.toArray(new Integer[0]);
        //所有点的横坐标排序
        Arrays.sort(X);
        System.out.println(Arrays.toString(X));
        Map<Integer, Integer> Xi = new HashMap();
        for (int i = 0; i < X.length; ++i){
            Xi.put(X[i], i);
        }
        System.out.println(Xi);

        Node active = new Node(0, X.length - 1, X);
        long ans = 0;
        long cur_x_sum = 0;
        int cur_y = events[0][0];

        for (int[] event: events) {
            int y = event[0], typ = event[1], x1 = event[2], x2 = event[3];
            /*
            events按y坐标升序排列，当type=-1时，则计算面积
            此时横坐标总线段长度已计算完成,则可统计出指定Y区间的面积
             */
            ans += cur_x_sum * (y - cur_y);
            cur_x_sum = active.update(Xi.get(x1), Xi.get(x2), typ);
            cur_y = y;

        }

        ans %= 1_000_000_007;
        return (int) ans;
    }

class Node {
    int start, end;
    Integer[] X;
    Node left, right;
    int count;
    long total;

    public Node(int start, int end, Integer[] X) {
        this.start = start;
        this.end = end;
        this.X = X;
        left = null;
        right = null;
        count = 0;
        total = 0;
    }

    public int getRangeMid() {
        return start + (end - start) / 2;
    }

    public Node getLeft() {
        if (left == null) left = new Node(start, getRangeMid(), X);
        return left;
    }

    public Node getRight() {
        if (right == null) right = new Node(getRangeMid(), end, X);
        return right;
    }

    /*
    赋值时，更新所有左子线段和右子线段
    线段长度为所有子线段之和
    如i,j在X的坐标里(原始输入),则认为两点之间有线段
    重复的点已经被去重，所有子线段无法重复
    */
    public long update(int i, int j, int val) {
        if (i >= j) return 0;
        if (start == i && end == j) {
            count += val;
        } else {
            getLeft().update(i, Math.min(getRangeMid(), j), val);
            getRight().update(Math.max(getRangeMid(), i), j, val);
        }

        //open
        if (count > 0) total = X[end] - X[start];
        else total = getLeft().total + getRight().total;

        return total;
    }

}

    public static void main(String[] args){
        int[][] input = {{0,0,4,3},{1,0,4,12},{1,0,4,1},{1,9,4,1},{2,5,4,7}};
        System.out.println(new _850().rectangleArea(input));
//        System.out.println(1 & 5);
    }
}
