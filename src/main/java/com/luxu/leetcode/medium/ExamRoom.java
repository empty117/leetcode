package com.luxu.leetcode.medium;

import java.util.*;

/**
 * 855
 */
public class ExamRoom {

    private int len;
    private Set<Integer> seatSet;

    public ExamRoom(int N) {
        this.len = N;
        seatSet = new HashSet<>();
    }
    
    public int seat() {
        if(seatSet.isEmpty()){
            seatSet.add(0);
            return 0;
        }
        List<Integer> seatList = new ArrayList<>(seatSet);
        Collections.sort(seatList);
        int start =0, end=len-1;
        int gap = -1,startIndex = -1;
        int index;
        if(seatList.size()==1){
            int seatIndex = seatList.get(0);
            int lGap = getMaxGap(start,seatIndex-start);
            int rGap = getMaxGap(seatIndex+1, end - seatIndex);
            if(rGap>lGap){
                startIndex = seatIndex+1;
                gap = rGap;
            }
            else{
                startIndex = 0;
                gap = lGap;
            }
        }
        else{
            int size = seatList.size();
            if(seatList.get(0) > start){
                gap = seatList.get(0) - start;
                startIndex = 0;
            }

            for(int i=0;i<size-1;i++){
                int a = seatList.get(i);
                int b = seatList.get(i+1);
                if(a-b==1){
                    continue;
                }
                int tmpGap = getMaxGap(a + 1,b-a-1);
                if(tmpGap > gap){
                    startIndex = a + 1;
                    gap = tmpGap;
                }
            }
            if(seatList.get(size-1) < end){
                int tmpGap = getMaxGap(seatList.get(size-1)+1, end - seatList.get(size-1));
                if(tmpGap > gap){
                    startIndex = seatList.get(size-1)+1;
                    gap = tmpGap;
                }
            }
        }
        index = startIndex;
        if(startIndex>0){
            index +=gap;
        }
        seatSet.add(index);
        return index;
    }

    private int getMaxGap(int index, int gap){
        if(index == 0 || index == len - gap){
            return gap - 1;
        }
        if(gap%2==0){
            return gap/2 - 1;
        }
        else{
            return gap/2;
        }
    }


    public void leave(int p) {
        seatSet.remove(p);
    }



    public static void main(String[] args){
        ExamRoom examRoom = new ExamRoom(8);
        System.out.println(examRoom.seat());
        System.out.println(examRoom.seat());
        System.out.println(examRoom.seat());
        examRoom.leave(0);
        examRoom.leave(7);
        System.out.println(examRoom.seat());

    }
}