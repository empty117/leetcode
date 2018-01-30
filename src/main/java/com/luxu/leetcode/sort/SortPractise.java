package com.luxu.leetcode.sort;

import java.util.Arrays;

/**
 * Created by xulu on 2017/8/25.
 */
public class SortPractise {
    private static int[] data = {5,6,2,1,4,8,-1,23,4311,-123,4112,44,52,511,1,0,12};

    private void swap(int i, int j, int[] data){
        int tmp = data[i] ;
        data[i] = data[j];
        data[j] = tmp;
    }

    /*
    冒泡排序
    依次把最大值挪到最右边
     */
    public void bubbleSort(int[] data){
        for(int i=0;i<data.length;i++){
            for(int j=0;j<data.length-i-1;j++){
                if(data[j]>data[j+1]){
                    swap(j,j+1,data);
                }
            }
        }
    }

    /*
    选择排序
    每次遍历都把最小的元素放在最左边
     */
    public void selectSort(int[] data){
        for(int i=0;i<data.length;i++){
            for(int j=i+1;j<data.length;j++){
                if(data[i] > data[j]){
                    swap(i,j,data);
                }
            }
        }
    }

    /*
    插入排序
    每次遍历将tmp插入已排序的i个元素的队列中
     */
    public void insertSort(int[] data){
        for(int i=1;i<data.length;i++){
            int tmp = data[i];
            for(int j=i-1;j>=0;j--){
                if(tmp < data[j]){
                    swap(j,j+1,data);
                }
            }
        }
    }

    public void quickSort(int[] data){
        quickSort(data,0,data.length-1);
    }

    private void quickSort(int[] data, int start, int end){
        int pivot;
        if(start<end){
            pivot = partition(data,start,end);
            quickSort(data,start,pivot-1);
            quickSort(data,pivot+1,end);
        }
    }
    private int partition(int[] data, int left,int right){
        int pivot = data[left];
        while(left<right){
            for(;left<right;right--){
                if(data[right]<pivot){
                    break;
                }
            }
            data[left]=data[right];
            for(;left<right;left++){
                if(data[left]>pivot){
                    break;
                }
            }
            data[right]=data[left];
        }
        data[left]=pivot;
        return left;
    }


    public static void main(String[] args){
        SortPractise sortPractise = new SortPractise();
        sortPractise.quickSort(data);
        System.out.println(Arrays.toString(data));
    }
}
