package com.luxu.leetcode.medium;

import java.util.*;

/**
 * @author xulu
 * @date 1/25/2019
 */
public class _939 {

    public int minAreaRect(int[][] points) {
        int oSize = Integer.MAX_VALUE, mSize = oSize;
        List<Point> pointList = new ArrayList<>();
        Map<Point,Point> pointMap= new HashMap<>();
        for(int i = 0;i<points.length;i++){
            Point point = new Point(points[i][0],points[i][1]);
            pointList.add(point);
            pointMap.put(point,point);
        }

        for(int i = 0;i<pointList.size()-1;i++){
            Point point1 = pointList.get(i);
            for(int j = i+1;j<pointList.size();j++){
                Point point2 = pointList.get(j);
                int size = getPotentianlSize(point1,point2);
                if(size>0 && size < mSize){
                    if(pointMap.containsKey(getPoint(point1,point2)) && pointMap.containsKey(getPoint(point2,point1))){
                        mSize = size;
                    }
                }
            }
        }

        if(mSize == oSize){
            return 0;
        }
        return mSize;
    }

    private Point getPoint(Point point1,Point point2){
        return new Point(point1.x,point2.y);
    }

    private int getPotentianlSize(Point point1,Point point2){
        int l = Math.abs(point1.x - point2.x);
        int h = Math.abs(point1.y - point2.y);
        return l * h;
    }

    private static class Point{
        int x,y;
        Point(int x, int y){
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString(){
            return "[x="+x+",y="+y+"]";
        }

        @Override
        public boolean equals(Object obj){
            if(obj instanceof Point){
                Point p = (Point) obj;
                return x == p.x && y==p.y;
            }
            return false;
        }
        @Override
        public int hashCode(){
            return Objects.hash(x,y);
        }
    }
}
