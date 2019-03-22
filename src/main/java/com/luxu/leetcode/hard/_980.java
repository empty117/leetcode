package com.luxu.leetcode.hard;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author xulu
 * @date 3/15/2019
 * 深度搜索回溯法
 */
public class _980 {

    private Point[] points ;
    int count = 0;
    private Point startPoint, endPoint;
    public int uniquePathsIII(int[][] grid) {
        List<Point> list = new ArrayList<>();
        for(int i=0;i<grid.length;i++){
            for(int j=0;j<grid[i].length;j++){
                int value = grid[i][j];
                if(value == 0){
                    list.add(new Point(i,j));
                }
                else if(value == 1){
                    startPoint = new Point(i,j);
                }
                else if(value == 2){
                    endPoint = new Point(i,j);
                }
            }
        }


        return count;
    }

    public static void main(String[] args){
        int x = new _980().uniquePathsIII(new int[][]{{1,0,0,0},{0,0,0,0},{0,0,2,-1}});
        System.out.println(x);
    }


    private class Point{
        int x,y;
        Point(int x, int y){
            this.x = x ;
            this.y = y;
        }

        @Override
        public boolean equals(Object o){
            if(o instanceof Point){
                Point p = (Point) o;
                return p.x == x && p.y == y;
            }
            return false;
        }

        @Override
        public int hashCode(){
            return Objects.hash(x,y);
        }

        public boolean near(Point target){
            int xGap = Math.abs(x - target.x);
            int yGap = Math.abs(y - target.y);
            if((xGap==0 && yGap==1) || (xGap==1 && yGap==0)){
                return true;
            }
            return false;
        }
    }


}
