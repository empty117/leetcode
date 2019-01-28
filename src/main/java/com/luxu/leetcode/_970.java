package com.luxu.leetcode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xulu
 * @date 1/8/2019
 */
public class _970 {
    public List<Integer> powerfulIntegers(int x, int y, int bound) {
        Set<Integer> result = new HashSet<>();
        int i=0;
        while(true){
            int sumX = (int) (Math.pow(x,i));
            if(sumX>=bound){
                break;
            }
            i++;
            int j=0;
            while(true){
                int sumY = (int) (Math.pow(y,j));
                if(sumX + sumY <= bound){
                    result.add(sumX + sumY);
                    j++;
                    if(y==1){
                        break;
                    }
                }
                else{
                    break;
                }
            }
            if(x==1){
                break;
            }
        }
        return new ArrayList<>(result);
    }

    public static void main(String[] args){
        _970 test = new _970();
        System.out.println(test.powerfulIntegers(1,1,2));
    }
}
