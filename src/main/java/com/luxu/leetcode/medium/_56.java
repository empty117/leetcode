package com.luxu.leetcode.medium;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author xulu
 * @date 1/30/2019
 */
public class _56 {

    private List<Interval> mergedList = new ArrayList<>();

    public List<Interval> merge(List<Interval> intervals) {
        Collections.sort(intervals, Comparator.comparingInt(a -> a.start));
        for(int i=0;i<intervals.size();i++){
            mergeNext(intervals.get(i));
        }
        return mergedList;
    }

    private void mergeNext(Interval interval2){
        if(!mergedList.isEmpty()){
            Interval interval1 = mergedList.get(mergedList.size()-1);
            int left = Math.max(interval1.start,interval2.start);
            int right = Math.min(interval1.end,interval2.end);
            if(left<=right){
                mergedList.remove(interval1);
                mergedList.add(new Interval(Math.min(interval1.start,interval2.start),Math.max(interval1.end,interval2.end)));
            }
            else{
                mergedList.add(interval2);
            }
        }
        else{
            mergedList.add(interval2);
        }
    }


    public class Interval {
        int start;
        int end;

        Interval() {
            start = 0;
            end = 0;
        }

        Interval(int s, int e) {
            start = s;
            end = e;
        }
    }
}
