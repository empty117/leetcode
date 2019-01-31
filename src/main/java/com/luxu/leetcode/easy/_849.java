package com.luxu.leetcode.easy;

/**
 * @author xulu
 * @date 1/29/2019
 */
public class _849 {
    public int maxDistToClosest(int[] seats) {
        boolean lc = true;
        int c = 0, lcc = 0, rcc = 0, cc = 0;
        int i=0;

        while(i<seats.length){
            if(seats[i]==0){
                c++;
                if(i==seats.length-1){
                    rcc = c;
                }
            }
            else if(lc){
                lc = false;
                lcc = c;
            }
            else{
                cc = Math.max(cc,c);
                c = 0;
            }
            i++;
        }
        System.out.println("cc="+cc+",lcc="+lcc+",rcc="+rcc);
        if(cc%2==0){
            cc = cc/2;
        }
        else{
            cc = cc/2+1;
        }
        int max = Math.max(lcc,rcc);
        max = Math.max(max,cc);
        return max;
    }
}
