package com.luxu.leetcode.hard;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author xulu
 * @date 12/27/2018
 */
public class _850 {

    private int threshold = (int) (Math.pow(2,10) + 7);

    public int rectangleArea(int[][] rectangles) {
        int len = rectangles.length, sum =0, missing = 0;
        int minx = Integer.MAX_VALUE,miny=Integer.MAX_VALUE,maxx=Integer.MIN_VALUE,maxy=Integer.MIN_VALUE;

        for(int i=0;i < len; i++){
            minx = Math.min(minx, rectangles[i][0]);
            miny = Math.min(miny, rectangles[i][1]);
            maxx = Math.max(maxx,rectangles[i][2]);
            maxy = Math.max(maxy,rectangles[i][3]);
        }

        System.out.println("minx="+minx+",maxx="+maxx+",miny="+miny+",maxy="+maxy);


        //遍历每一行
        for(int y = miny;y<=maxy;y++){
            //每一行记录横线段
            Set<Integer> tmp = new HashSet<>();
            for(int i = 0; i< len;i++){
                for(int j= rectangles[i][0];j<rectangles[i][2];j++){
                    if(rectangles[i][1] <= y && rectangles[i][3] >=y){
                        tmp.add(j);
                    }
                }
            }
            int tmp_miss= maxx - minx - tmp.size();
            missing += tmp_miss;
            System.out.println("第"+y+"行：横线缺少了"+tmp_miss);
        }


        //遍历每一列
        for(int x = minx;x<=maxx;x++){
            //每一列记录竖线段
            Set<Integer> tmp = new HashSet<>();
            for(int i = 0; i< len;i++){
                for(int j= rectangles[i][1];j<rectangles[i][3];j++){
                    if(rectangles[i][0] <= x && rectangles[i][2] >=x){
                        tmp.add(j);
                    }
                }
            }
            int tmp_miss = maxy - miny - tmp.size();
            missing += tmp_miss;
            System.out.println("第"+x+"列：竖线缺少了"+tmp_miss);
        }

        System.out.println("总共缺了"+ missing +"条线");

        if(missing%2 == 0){
            missing = missing/2;
        }
        else{
            missing = missing/2 + 1;
        }

        int total = (maxx - minx) * (maxy - miny);

        total = total  - missing;

        return total;
    }



    public static void main(String[] args){
        int[][] input = {{0,0,2,2},{1,0,2,3},{1,0,8,1},{9,100,100,200}};
        System.out.println(new _850().rectangleArea(input));
    }
}
